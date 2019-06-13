package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Core.Listener.ConversationOpenListener;
import com.example.joe.cityumobile.DataModel.BmobModel.ServiceOrder;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.Manager.ConversationManager;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Activity.ChatActivity;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * 发送文本消息的Holder
 */
public class OrderHolder extends BaseViewHolder {

    ServiceOrder data;

    private TextView postTitle;
    private TextView postContent;
    private ImageView providerAvatar;
    private ImageView receiverAvatar;
    private TextView providerName;
    private TextView receiverName;
    private ConstraintLayout contact;

    public OrderHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_order);
    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        postTitle = itemView.findViewById(R.id.postTitle);
        postContent = itemView.findViewById(R.id.postContent);
        providerAvatar = itemView.findViewById(R.id.providerAvatar);
        receiverAvatar = itemView.findViewById(R.id.receiverAvatar);
        providerName = itemView.findViewById(R.id.providerName);
        receiverName = itemView.findViewById(R.id.receiverName);
        contact = itemView.findViewById(R.id.openConversationBtn);
    }

    /**
     * 设置数据元
     *
     * @param o
     */
    @Override
    public void setData(Object o) {
        data = (ServiceOrder) o;
    }

    /**
     * 填充UI组件
     */
    @Override
    public void fillComponents() {
        postTitle.setText(data.getPost().getTitle());
        postContent.setText(data.getPost().getContent());
        if (data.getServiceProvider().getUsername().equals(MyUserManager.currentUser.getUsername())){
            providerName.setText(MyUserManager.currentUser.getNickName());
            providerAvatar.setImageResource(Utils.getAvatarResId(MyUserManager.currentUser.getAvatar()));

            receiverName.setText(data.getServiceReceiver().getNickName());
            receiverAvatar.setImageResource(Utils.getAvatarResId(data.getServiceReceiver().getAvatar()));
        }else{
            receiverName.setText(MyUserManager.currentUser.getNickName());
            receiverAvatar.setImageResource(Utils.getAvatarResId(MyUserManager.currentUser.getAvatar()));

            providerName.setText(data.getServiceProvider().getNickName());
            providerAvatar.setImageResource(Utils.getAvatarResId(data.getServiceProvider().getAvatar()));
        }

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.note("Open conversation");
                final User target;
                if (data.getServiceProvider().getUsername().equals(MyUserManager.currentUser.getUsername())){
                    target = data.getServiceReceiver();
                }else{
                    target = data.getServiceProvider();
                }
                ConversationManager.getInstance().startConversation(target, new ConversationOpenListener() {
                    @Override
                    public void onStarted(BmobIMConversation conversation) {
                        ChatManager.getInstance().setCurrentConversation(conversation);
                        ChatManager.getInstance().setCurrentTargetUser(target);
                        ChatManager.getInstance().setCurrentOrder(data);
                        Intent intent = new Intent(MyApplication.currentActivity(), ChatActivity.class);
                        MyApplication.startActivityFromHere(intent);
                    }
                });
            }
        });
    }
}
