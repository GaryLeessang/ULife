package com.example.joe.cityumobile.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joe.cityumobile.R;

/**
 * 新聊天申请弹窗
 */
public class NotificationDialog extends Dialog {

    public NotificationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private View mLayout;
        private NotificationDialog mDialog;

        private Button acceptBtn;
        private Button refuseBtn;

        public Builder(Context context){
            mDialog = new NotificationDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.dialog_note,null,false);
            mDialog.addContentView(mLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            findAndBindView();
        }

        private void findAndBindView(){
            acceptBtn = mLayout.findViewById(R.id.acceptBtn);
            refuseBtn = mLayout.findViewById(R.id.refuseBtn);

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            refuseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }

        public NotificationDialog create(){
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            return mDialog;
        }
    }

}
