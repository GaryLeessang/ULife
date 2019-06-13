package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Dialog.EditNoteDialog;

/**
 * 发送文本消息的Holder
 */
public class ExpressHolder extends BaseViewHolder {

    private Post data;

    private ImageView publisherAvatar;
    private TextView postTitle;
    private TextView publisherName;
    private TextView postContent;
    private Button contactButton;
    private TextView helpTag;
    private TextView serviceTag;
    private TextView size;
    private TextView priority;
    private TextView commission;
    private TextView createTime;
    private TextView expireTime;

    public ExpressHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_post);
    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        postTitle = itemView.findViewById(R.id.postTitle);
        publisherAvatar = itemView.findViewById(R.id.postUserAvatar);
        publisherName = itemView.findViewById(R.id.publisherName);
        postContent = itemView.findViewById(R.id.postContent);
        contactButton = itemView.findViewById(R.id.contactBtn);
        helpTag = itemView.findViewById(R.id.needHelp);
        serviceTag = itemView.findViewById(R.id.provideService);
        size = itemView.findViewById(R.id.size);
        priority = itemView.findViewById(R.id.priority);
        commission = itemView.findViewById(R.id.commission);
        createTime = itemView.findViewById(R.id.createTime);
        expireTime = itemView.findViewById(R.id.expireTime);
    }

    /**
     * 设置数据元
     *
     * @param o
     */
    @Override
    public void setData(Object o) {
        data = (Post) o;
    }

    /**
     * 填充UI组件
     */
    @Override
    public void fillComponents() {
        if (data != null){
            postTitle.setText(data.getTitle());
            publisherName.setText(data.getPublisher().getNickName());
            if (data.getContent().trim().isEmpty()){
                postContent.setText("No extra message...");
            }else{
                postContent.setText(data.getContent());
            }

            publisherAvatar.setImageResource(Utils.getAvatarResId(data.getPublisher().getAvatar()));
            switch (data.getPostType()){
                case 1:
                    helpTag.setVisibility(View.VISIBLE);
                    serviceTag.setVisibility(View.GONE);
                    break;
                case 2:
                    helpTag.setVisibility(View.GONE);
                    serviceTag.setVisibility(View.VISIBLE);
                    break;
                default:
                    helpTag.setVisibility(View.GONE);
                    serviceTag.setVisibility(View.GONE);
                    break;
            }

            size.setText(data.getSize());
            priority.setText(data.getPriority());
            commission.setText("$"+String.valueOf(data.getCommission()));
            createTime.setText(data.getCreatedAt());
            expireTime.setText(data.getExpireDate().getDate());

            if (data.getPublisher().getUsername().equals(MyUserManager.currentUser.getUsername())){
                contactButton.setEnabled(false);
            }

            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //旧版本 ---  直接强行开启对话
                    //ChatManager.getInstance().startConversation(data.getPublisherID());

                    /**
                     * todo 新版本，向此人发送一条聊天申请，被批准后才可以聊天
                     */

                    //ChatManager.getInstance().sendChatApplication(data.getPublisherID(),"This is a test note");
                    //ApplyManager.getInstance().sendApply(data.getPublisherID(),data.getObjectId(),"Note test");
                    openNoteDialog();
                }
            });


        }
    }


    private void openNoteDialog(){
        EditNoteDialog.Builder dialog = new EditNoteDialog.Builder(itemView.getContext());
        dialog.setPost(data);
        dialog.create().show();
    }

}
