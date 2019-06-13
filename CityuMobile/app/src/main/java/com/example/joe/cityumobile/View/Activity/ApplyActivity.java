package com.example.joe.cityumobile.View.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Manager.ApplyManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Adapter.ApplyAdapter;
import com.example.joe.cityumobile.databinding.ActivityApplyBinding;

public class ApplyActivity extends BaseActivity {

    ActivityApplyBinding layout;
    ApplyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_apply);
        setImmersiveStatusBar();
        setListener();
    }


    /**
     * 设置监听器
     */
    @Override
    public void setListener() {
        layout.applyList.setLayoutManager(new LinearLayoutManager(this));
        adapter = (ApplyAdapter) AdapterFactory.getAdapter(ApplyAdapter.class);
        layout.applyList.setAdapter(adapter);
        ApplyManager.getInstance().addObserver(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplyManager.getInstance().removeObserver(adapter);
    }
}
