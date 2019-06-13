package com.example.joe.cityumobile.View.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.databinding.ActivitySettingBinding;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity {

    ActivitySettingBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        setImmersiveStatusBar();
        setListener();
    }

    /**
     * 设置监听器
     */
    @Override
    public void setListener() {
        layout.accountInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AccountActivity.class);
                startActivity(intent);
            }
        });

        layout.feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),FeedbackActivity.class);
                startActivity(intent);
            }
        });

        layout.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                MyApplication.startActivityFromHere(new Intent(v.getContext(),LoginActivity.class));
                finish();
                MyApplication.findActivity(MainActivity.class).finish();
            }
        });
    }
}
