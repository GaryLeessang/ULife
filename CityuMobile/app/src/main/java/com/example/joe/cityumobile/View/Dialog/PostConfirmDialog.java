package com.example.joe.cityumobile.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.joe.cityumobile.Core.Listener.PostPublishListener;
import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;

import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 发帖二次确认窗口
 */
public class PostConfirmDialog extends Dialog {

    public PostConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private View mLayout;
        private PostConfirmDialog mDialog;

        private Post post;

        private Button confirmBtn;
        private Button cancelBtn;
        private Spinner durationSelector;

        private int currentSelectDuration = 2;

        private PostPublishListener postPublishListener;

        public Builder(Context context){
            mDialog = new PostConfirmDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.dialog_post_confirm,null,false);
            mDialog.addContentView(mLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            findAndBindView();
        }

        private void findAndBindView(){
            confirmBtn = mLayout.findViewById(R.id.confirmBtn);
            cancelBtn = mLayout.findViewById(R.id.cancelBtn);
            durationSelector = mLayout.findViewById(R.id.durationSelector);

            durationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentSelectDuration = Integer.parseInt(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.HOUR, currentSelectDuration);
                    post.setExpireDate(calendar.getTime());

                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                Utils.note("上传帖子成功");
                                PostManager.getInstance().loadPostHistoryFromCloud(null);
                                if (postPublishListener != null){
                                    postPublishListener.done(true);
                                }
                            }else{
                                Utils.note("上传帖子失败");
                                Log.e("My",e.toString());
                                if (postPublishListener != null){
                                    postPublishListener.done(false);
                                }
                            }

                            mDialog.dismiss();
                        }
                    });
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

        }

        public Builder setPostData(Post postData){
            post = postData;
            return this;
        }

        public Builder setListener(PostPublishListener listener){
            postPublishListener = listener;
            return this;
        }

        public PostConfirmDialog create(){
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            return mDialog;
        }
    }

}
