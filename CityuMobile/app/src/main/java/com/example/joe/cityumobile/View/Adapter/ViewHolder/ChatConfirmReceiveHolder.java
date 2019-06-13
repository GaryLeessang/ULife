package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Core.Listener.SimpleListener;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobConfirmACKMessage;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.OrderManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Adapter.OrderAdapter;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 发送图片消息的Holder
 */
public class ChatConfirmReceiveHolder extends BaseViewHolder {

    ImageView avatar;
    Button confirmBtn;
    BmobIMMessage data;

    public ChatConfirmReceiveHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_recv_confirmation);

    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        confirmBtn = itemView.findViewById(R.id.orderCloseConfirmBtn);
        avatar = itemView.findViewById(R.id.chatAvatar);
    }

    /**
     * 设置数据元
     *
     * @param o
     */
    @Override
    public void setData(Object o) {
        data = (BmobIMMessage) o;
    }


    /**
     * 填充UI组件
     */
    @Override
    public void fillComponents() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.note("Handling...");
                //open dialog alert wheter to save chat log
                //close conversation
                OrderManager.getInstance().updateOrder(ChatManager.getInstance().getCurrentOrder(), new SimpleListener() {
                    @Override
                    public void done() {
                        ChatManager.getInstance().getCurrentConversation().sendMessage(new BmobConfirmACKMessage(), new MessageSendListener() {
                            @Override
                            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                                if (e == null){
                                    Utils.note("ACK send");

                                    OrderManager.getInstance().loadOrdersFromCloud(new SimpleListener() {
                                        @Override
                                        public void done() {
                                            MyApplication.finishCurrentActivity();
                                            ((OrderAdapter) AdapterFactory.getAdapter(OrderAdapter.class)).notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
        avatar.setImageResource(Utils.getAvatarResId(ChatManager.getInstance().getCurrentTargetUser().getAvatar()));
    }


}
