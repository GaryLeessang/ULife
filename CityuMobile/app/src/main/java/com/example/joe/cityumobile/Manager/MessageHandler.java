package com.example.joe.cityumobile.Manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.Listener.SimpleListener;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobActiveMessage;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobApplyMessage;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobConfirmACKMessage;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobConfirmMessage;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Activity.ChatActivity;
import com.example.joe.cityumobile.View.Activity.MainActivity;
import com.example.joe.cityumobile.View.Adapter.OrderAdapter;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;

public class MessageHandler extends BmobIMMessageHandler {

    private Context context;

    public MessageHandler(Context context){
        this.context = context;
    }

    private Map<String,List<MessageEvent>> offlineEventMap;

    /**
     * 在线消息接收
     * @param event
     */
    public void onMessageReceive(final MessageEvent event){
        if (MyApplication.instance.isUserLogin){
            executeMessage(event);
        }
    }

    /**
     * 离线消息接收
     * @param event
     */
    public void onOfflineReceive(final OfflineMessageEvent event){

    }

    /**
     * 处理消息
     * @param event
     */
    private void executeMessage(MessageEvent event){
        BmobIMMessage msg = event.getMessage();
        if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0){
            processCustomMessage(event);
        }else{
            processSDKMessage(event);
        }
    }

    /**
     * 处理SDK内置消息类型
     * @param event
     */
    private void processSDKMessage(MessageEvent event){
        Intent pendingIntent = new Intent(context, MainActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        BmobIMUserInfo userInfo = event.getFromUserInfo();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(icon, userInfo.getUserId(), event.getMessage().getContent(), "您有一条新消息", pendingIntent);
        if (event.getMessage().getMsgType().equals((BmobApplyMessage.APPLY))){
            return;
        }
        ChatManager.getInstance().onMessageReceived(event);
    }

    /**
     * 处理自定义消息
     * @param event
     */
    private void processCustomMessage(MessageEvent event){
        if (event.getMessage().getMsgType().equals(BmobApplyMessage.APPLY)){
            ApplyManager.getInstance().onApplyReceived(event);
        }else if (event.getMessage().getMsgType().equals(BmobActiveMessage.ACTIVE)){

        }else if(event.getMessage().getMsgType().equals(BmobConfirmMessage.CONFIRM)){
            ChatManager.getInstance().onMessageReceived(event);
        }else if(event.getMessage().getMsgType().equals(BmobConfirmACKMessage.CONFIRM_ACK)){
            if (MyApplication.currentActivity() != null){
                if (MyApplication.currentActivity().getClass().equals(ChatActivity.class)){
                    OrderManager.getInstance().loadOrdersFromCloud(new SimpleListener() {
                        @Override
                        public void done() {
                            MyApplication.finishCurrentActivity();
                            ((OrderAdapter) AdapterFactory.getAdapter(OrderAdapter.class)).notifyDataSetChanged();
                        }
                    });
                }else{
                    ((OrderAdapter) AdapterFactory.getAdapter(OrderAdapter.class)).notifyDataSetChanged();
                }
            }
        }
    }







}

