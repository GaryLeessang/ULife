package com.example.joe.cityumobile.View.Activity;

import android.os.Bundle;

import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.R;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setImmersiveStatusBar();
    }

    /**
     * 设置监听器
     */
    @Override
    public void setListener() {

    }
}
