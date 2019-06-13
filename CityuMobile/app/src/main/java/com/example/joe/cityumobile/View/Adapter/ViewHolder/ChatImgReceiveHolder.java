package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Core.ImageLoaderFactory;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Dialog.ImgDialog;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 发送图片消息的Holder
 */
public class ChatImgReceiveHolder extends BaseViewHolder {

    ImageView avatar;
    ImageView imagetContent;
    BmobIMMessage data;

    public ChatImgReceiveHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_recv_img);

    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        imagetContent = itemView.findViewById(R.id.imageContent);
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
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false,data);
        ImageLoaderFactory.getLoader().loadImage(imagetContent, TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(),R.mipmap.ic_launcher,null);
        avatar.setImageResource(Utils.getAvatarResId(ChatManager.getInstance().getCurrentTargetUser().getAvatar()));

        imagetContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImgDialog.Builder builder = new ImgDialog.Builder(v.getContext());
                ImageLoaderFactory.getLoader().loadImage(builder.getImg(), TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(),R.mipmap.ic_launcher,null);
                builder.dismiss();
                builder.create().show();

            }
        });
    }
}
