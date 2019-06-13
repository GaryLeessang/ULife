package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 发送图片消息的Holder
 */
public class ChatConfirmSendHolder extends BaseViewHolder {

    ImageView avatar;
    BmobIMMessage data;

    public ChatConfirmSendHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_send_confirmation);

    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
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
        avatar.setImageResource(Utils.getAvatarResId(MyUserManager.currentUser.getAvatar()));
    }
}
