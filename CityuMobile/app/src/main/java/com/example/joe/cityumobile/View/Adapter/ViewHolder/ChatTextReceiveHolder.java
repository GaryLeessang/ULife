package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 接收文本消息的Holder
 */
public class ChatTextReceiveHolder extends BaseViewHolder {

    ImageView avatar;
    TextView textContent;
    BmobIMMessage data;

    public ChatTextReceiveHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_recv_text);
    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        textContent = itemView.findViewById(R.id.rcv_msgContent);
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
        textContent.setText(data.getContent());
        avatar.setImageResource(Utils.getAvatarResId(ChatManager.getInstance().getCurrentTargetUser().getAvatar()));
    }

}
