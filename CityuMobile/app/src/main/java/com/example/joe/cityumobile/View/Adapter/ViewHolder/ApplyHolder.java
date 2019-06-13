package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.DataModel.DaoModel.Apply;
import com.example.joe.cityumobile.DataModel.DaoModel.Express;
import com.example.joe.cityumobile.Manager.ApplyManager;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Dialog.NoteDialog;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 发送文本消息的Holder
 */
public class ApplyHolder extends BaseViewHolder {

    private TextView targetUserid;
    private TextView note;
    private TextView refPostTitle;
    private CircleImageView avatar;
    private SwipeLayout swipeLayout;
    private ConstraintLayout surfaceLayout;
    private Button deletBtn;
    private Button infoBtn;

    private Apply data;

    public ApplyHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_apply);
    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        refPostTitle = itemView.findViewById(R.id.refPostTitle);
        avatar = itemView.findViewById(R.id.applyAvatar);
        note = itemView.findViewById(R.id.noteMsg);
        targetUserid = itemView.findViewById(R.id.targetUid);
        swipeLayout = itemView.findViewById(R.id.sample1);
        surfaceLayout = itemView.findViewById(R.id.surface);
        deletBtn = itemView.findViewById(R.id.deleteBtn);
        infoBtn = itemView.findViewById(R.id.infoBtn);

        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right,itemView.findViewById(R.id.bottom_wrapper));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left,null);
    }

    /**
     * 设置数据元
     *
     * @param o
     */
    @Override
    public void setData(Object o) {
        data = (Apply) o;
    }

    /**
     * 填充UI组件
     */
    @Override
    public void fillComponents() {
        if (!data.getNote().isEmpty()){
            note.setText(data.getNote());
        }

        targetUserid.setText(data.getUserId());
        Express express = PostManager.getInstance().findExpressById(data.getReferencePostId());
        if (express != null){
            refPostTitle.setText(express.getTitle());
        }else{
            refPostTitle.setText("Loading...");
        }

        avatar.setImageResource(Utils.getAvatarResId(data.getAvatar()));

        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.note("Delete");
                swipeLayout.close();
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialog();

                //todo 批准此人申请，拒绝掉所有同一帖子ID的其他所有申请
                ApplyManager.getInstance().approveApply(data);

                swipeLayout.close();
            }
        });

        surfaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("My","Clicked");
            }
        });

    }

    private void openDialog(){
        NoteDialog dialog = new NoteDialog.Builder(MyApplication.currentActivity()).create();
        dialog.show();
    }
}
