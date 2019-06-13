package com.example.joe.cityumobile.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.R;

/**
 * 账号申请成功
 */
public class SubmitSuccessDialog extends Dialog {

    public SubmitSuccessDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private View mLayout;
        private SubmitSuccessDialog mDialog;

        private Button acceptBtn;

        public Builder(Context context){
            mDialog = new SubmitSuccessDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.dialog_regist_success,null,false);
            mDialog.addContentView(mLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            findAndBindView();
        }

        private void findAndBindView(){
            acceptBtn = mLayout.findViewById(R.id.acceptBtn);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    MyApplication.finishCurrentActivity();
                }
            });
        }

        public SubmitSuccessDialog create(){
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            return mDialog;
        }
    }

}
