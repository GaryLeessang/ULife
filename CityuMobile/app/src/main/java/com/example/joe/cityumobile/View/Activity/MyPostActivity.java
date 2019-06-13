package com.example.joe.cityumobile.View.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.Core.Listener.PostRefreshListener;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Adapter.MyExpressRecordAdapter;
import com.example.joe.cityumobile.databinding.ActivityMyPostBinding;

public class MyPostActivity extends BaseActivity {

    private ActivityMyPostBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_my_post);
        setImmersiveStatusBar();
        setListener();
        PostManager.getInstance().loadPostHistoryFromDB();
    }

    /**
     * 设置监听器
     */
    @Override
    public void setListener() {
        layout.myPostHistory.setLayoutManager(new LinearLayoutManager(this));
        layout.myPostHistory.setAdapter((MyExpressRecordAdapter) AdapterFactory.getAdapter(MyExpressRecordAdapter.class));

        layout.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                layout.refreshBtn.setEnabled(false);
                v.animate().rotation(360);
                PostManager.getInstance().loadPostHistoryFromCloud(new PostRefreshListener() {
                    @Override
                    public void done() {
                        v.setRotation(0);
                        v.setEnabled(true);
                    }
                });
            }
        });
    }
}
