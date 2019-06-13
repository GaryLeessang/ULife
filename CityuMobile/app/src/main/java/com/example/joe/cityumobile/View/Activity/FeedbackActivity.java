package com.example.joe.cityumobile.View.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.databinding.ActivityFeedbackBinding;

public class FeedbackActivity extends BaseActivity {
    ActivityFeedbackBinding layout;

    @Override
    protected void onCreate(Bundle saveInsatanceState) {
        super.onCreate(saveInsatanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        setImmersiveStatusBar();
        setListener();
    }

    @Override
    public void setListener() {
        layout.idWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        layout.idWebView.getSettings().setJavaScriptEnabled(true);
        layout.idWebView.loadUrl("http://www.wjx.top/m/36928994.aspx");

    }
}