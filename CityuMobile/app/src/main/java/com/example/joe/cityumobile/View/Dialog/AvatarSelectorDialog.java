package com.example.joe.cityumobile.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 头像选择弹窗
 */
public class AvatarSelectorDialog extends Dialog {

    public AvatarSelectorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private View mLayout;
        private AvatarSelectorDialog mDialog;

        private Button saveBtn;
        private Button cancelBtn;
        private Spinner selector;
        private CircleImageView avatar;

        private int currentSelect = -1;

        public Builder(Context context){
            mDialog = new AvatarSelectorDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.dialog_select_avatar,null,false);
            mDialog.addContentView(mLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            findAndBindView();
        }

        private void findAndBindView(){
            avatar = mLayout.findViewById(R.id.newAvatar);
            selector = mLayout.findViewById(R.id.avatar_selector);
            saveBtn = mLayout.findViewById(R.id.saveChange);
            cancelBtn = mLayout.findViewById(R.id.cancel);

            selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            avatar.setImageResource(R.drawable.boy);
                            currentSelect = 0;
                            break;
                        case 1:
                            avatar.setImageResource(R.drawable.boy_1);
                            currentSelect = 1;
                            break;
                        case 2:
                            avatar.setImageResource(R.drawable.girl);
                            currentSelect = 2;
                            break;
                        case 3:
                            avatar.setImageResource(R.drawable.girl_1);
                            currentSelect = 3;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    currentSelect = -1;
                    avatar.setImageResource(R.mipmap.ic_launcher_round);
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUserManager.currentUser.setAvatar(currentSelect);

                    MyUserManager.currentUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Utils.note("更新成功");
                                mDialog.dismiss();
                            }else{
                                Utils.note("更新失败");
                                mDialog.dismiss();
                            }
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

        public AvatarSelectorDialog create(){
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            return mDialog;
        }
    }


}
