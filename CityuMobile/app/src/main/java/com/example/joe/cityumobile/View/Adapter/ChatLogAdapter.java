package com.example.joe.cityumobile.View.Adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Core.Base.BaseRecyclerAdapter;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.ISingletonAdapter;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.DataModel.BmobModel.MessageType;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Activity.ChatActivity;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ChatConfirmReceiveHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ChatConfirmSendHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ChatImgReceiveHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ChatImgSendHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ChatTextReceiveHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ChatTextSendHolder;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 聊天记录RecyclerView适配器
 */
public class ChatLogAdapter extends BaseRecyclerAdapter implements MyObserver,ISingletonAdapter {

    /**
     * 监听消息增删事件
     */
    public ChatLogAdapter(){
        eventCodes.add(EventMessageType.MESSAGE_ADD);
        eventCodes.add(EventMessageType.MESSAGE_DELETE);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case 0:
                return new ChatTextSendHolder(viewGroup.getContext(),viewGroup);
            case 1:
                return new ChatTextReceiveHolder(viewGroup.getContext(),viewGroup);
            case 2:
                return new ChatImgSendHolder(viewGroup.getContext(),viewGroup);
            case 3:
                return new ChatImgReceiveHolder(viewGroup.getContext(),viewGroup);
            case 4:
                return new ChatConfirmSendHolder(viewGroup.getContext(),viewGroup);
            case 5:
                return new ChatConfirmReceiveHolder(viewGroup.getContext(),viewGroup);
            default:
                return new BaseViewHolder(viewGroup.getContext(),viewGroup,R.layout.item_empty) {
                    @Override
                    public void findViews() {

                    }

                    @Override
                    public void setData(Object o) {

                    }

                    @Override
                    public void fillComponents() {

                    }
                };
        }
    }

    /**
     * 根据消息类型更换视图类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        BmobIMMessage msg = ChatManager.getInstance().getMsgList().get(position);
        if (msg.getFromId().equals(MyUserManager.currentUser.getUsername())){
            //是自己发出去的消息
            if (msg.getMsgType().equals("txt")){
                return MessageType.TEXT_SEND_TYPE;
            }else if(msg.getMsgType().equals("confirm")){
                return MessageType.CONFIRM_SEND_TYPE;
            }else if(msg.getMsgType().equals("image")){
                return MessageType.IMG_SEND_TYPE;
            }else{
                return -1;
            }
        }else{
            //收到的消息
            if (msg.getMsgType().equals(("txt"))){
                return MessageType.TEXT_RECEIVE_TYPE;
            }else if(msg.getMsgType().equals("confirm")){
                return MessageType.CONFIRM_RECEIVE_TYPE;
            }else if(msg.getMsgType().equals("image")){
                return MessageType.IMG_RECEIVE_TYPE;
            }else{
                return -1;
            }
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int i) {
        viewHolder.setData(ChatManager.getInstance().getMsgList().get(i));
        viewHolder.fillComponents();
    }

    @Override
    public int getItemCount() {
        return ChatManager.getInstance().getMsgList().size();
    }

    @Override
    public void update(Integer... eventMessageCode) {
        for (Integer code :eventMessageCode) {
            if (eventCodes.contains(code)){
                notifyDataSetChanged();
                if (MyApplication.currentActivity() != null){
                    if (MyApplication.currentActivity().getClass().equals(ChatActivity.class)){
                        ((ChatActivity)MyApplication.currentActivity()).showLatestMessage();
                    }
                }
                return;
            }
        }
    }

}
