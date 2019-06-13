package com.example.joe.cityumobile.View.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.Listener.ImageCompressListener;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobActiveMessage;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobConfirmMessage;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.ImageUtils;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Adapter.ChatLogAdapter;
import com.example.joe.cityumobile.databinding.ActivityChatBinding;

import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 单聊聊天界面
 */
public class ChatActivity extends AppCompatActivity implements View.OnLayoutChangeListener {

    private ActivityChatBinding layout;

    private BmobIMConversation conversation;
    private User targetUser;

    private ChatLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_chat);

        findAndBindView();
        RegisterListener();

        layout.extraFuncBar.setVisibility(View.GONE);

        //加载对话，加载对话对象
        conversation = ChatManager.getInstance().getCurrentConversation();
        targetUser = ChatManager.getInstance().getCurrentTargetUser();

        //填充对话对象的昵称和头像
        layout.chatTarget.setText(targetUser.getNickName());
        layout.avatar.setImageResource(Utils.getAvatarResId(targetUser.getAvatar()));

        //发送一条空消息，让conversation激活，并且加载该对话所有聊天记录
        final BmobActiveMessage msg = new BmobActiveMessage();
        msg.setIsTransient(true);
        msg.setContent("null");
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null){
                    Log.e("My","null");
                    conversation.deleteMessage(msg);
                    ChatManager.getInstance().loadMessages();
                }else{
                    Log.e("My",e.toString());
                }
            }
        });
    }

    void findAndBindView(){

        layout.chatLogView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        adapter = (ChatLogAdapter) AdapterFactory.getAdapter(ChatLogAdapter.class);
        layout.chatLogView.setAdapter(adapter);

        ChatManager.getInstance().addObserver(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ChatManager.getInstance().isConnected){
            ChatManager.getInstance().connectServer();
        }

    }

    @Override
    //检测鼠标点击事件
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            View view = findViewById(R.id.inputArea);
            if (clickedBlank(view,ev)){
                hideSoftKeyboard(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //判断是否点击到点击空白处（非输入区域）
    private boolean clickedBlank(View view,MotionEvent event){
        if (view != null ){
            int[] pos ={0,0};
            view.getLocationInWindow(pos);
            int left = pos[0];
            int top = pos[1];
            int right = left + view.getWidth();
            int bottom = top + view.getHeight();

            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() <bottom){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    //隐藏输入软键盘
    private void hideSoftKeyboard(IBinder token){
        if (token != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //注册监听器
    void RegisterListener() {
        //发送消息按钮注册监听器
        layout.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMessage();
                sendTextMsg();
            }
        });

        layout.extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.extraFuncBar.getVisibility() == View.GONE){
                    layout.extraFuncBar.setVisibility(View.VISIBLE);
                }else{
                    layout.extraFuncBar.setVisibility(View.GONE);
                }
            }
        });

        layout.sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                layout.extraFuncBar.setVisibility(View.GONE);
            }
        });

        layout.closeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChatManager.getInstance().isConnected){
                    BmobConfirmMessage message = new BmobConfirmMessage();
                    message.setContent("confirm");
                    layout.extraFuncBar.setVisibility(View.GONE);
                    conversation.sendMessage(message,sendListener);
                }
            }
        });

        //更布局添加布局变更监听器
        layout.getRoot().addOnLayoutChangeListener(this);
    }

    /**
     * 发送文本消息
     */
    void sendTextMsg(){
        if (ChatManager.getInstance().isConnected){
            BmobIMTextMessage message = new BmobIMTextMessage();
            message.setIsTransient(false);
            message.setContent(layout.inputField.getText().toString());
            message.setFromId(BmobIM.getInstance().getCurrentUid());
            message.setToId(targetUser.getUsername());

            conversation.sendMessage(message,sendListener);
        }
    }

    /**
     * 发送图片消息
     * @param path
     */
    void sendImageMsg(String path){
        if (ChatManager.getInstance().isConnected){
            layout.sendProgress.setVisibility(View.VISIBLE);
            layout.sendProgress.setProgress(0);
            BmobIMImageMessage imageMessage = new BmobIMImageMessage(path);
            Log.e("My",imageMessage.getMsgType());
            conversation.sendMessage(imageMessage,sendListener);
        }
    }

    /**
     * 选择一张要发送的图片
     */
    private void selectImage(){

        final CharSequence[] items={"打开照相机拍一张","从相册读取一张", "取消"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择一张图片");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("打开照相机拍一张")) {

//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, 0);

                } else if (items[i].equals("从相册读取一张")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);

                } else if (items[i].equals("取消")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==1){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                Uri selectedImageUri = data.getData();
                String path = ImageUtils.getImagePath(selectedImageUri,null);
                Log.e("My",path);

            }else if(requestCode==0){

                Uri selectedImageUri = data.getData();

                String imgPath = "";
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImageUri, proj, null, null, null);
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    imgPath = cursor.getString(column_index);
                }
                File file = new File(imgPath);

                Log.e("My","path: "+imgPath);

                ImageUtils.lubanCompress(file, new ImageCompressListener() {
                    @Override
                    public void start() {
                        Utils.note("正在压缩图片");
                    }

                    @Override
                    public void done(File path) {
                        Utils.note("压缩完成");
                        sendImageMsg(path.getPath());
                    }
                });

            }

        }
    }


    public MessageSendListener sendListener = new MessageSendListener() {
        @Override
        public void onProgress(int i) {
            layout.sendProgress.setProgress(i);
        }

        @Override
        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
            if (e == null){
                layout.inputField.setText("");
                ChatManager.getInstance().onMessageSendOut(bmobIMMessage);
                //显示最近发送的消息
                //showLatestMessage();
            }else{
                Toast.makeText(ChatActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
            }
            layout.sendProgress.setVisibility(View.GONE);
        }
    };


    //显示最新的消息，滚动到最后一条消息
    public void showLatestMessage(){
        if (adapter.getItemCount()>0){
            layout.chatLogView.requestLayout();
            layout.chatLogView.post(new Runnable() {
                @Override
                public void run() {
                    layout.chatLogView.scrollToPosition(adapter.getItemCount() - 1);
                }
            });
        }
    }

    @Override
    //当界面布局改变，滚动到最后一条消息
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != -1 && oldBottom > bottom){
            layout.chatLogView.requestLayout();
            layout.chatLogView.post(new Runnable() {
                @Override
                public void run() {
                    layout.chatLogView.scrollToPosition(adapter.getItemCount()-1);
                }
            });
        }
    }

}
