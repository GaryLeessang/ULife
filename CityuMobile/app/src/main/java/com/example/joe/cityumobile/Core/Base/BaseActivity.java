package com.example.joe.cityumobile.Core.Base;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends AppCompatActivity{



    /**
     * 设置监听器
     */
    public abstract void setListener();

    /**
     * 设置状态栏颜色
     * @param colorResId
     */
    public void setWindowStatusBarColor(int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(this.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setImmersiveStatusBar(){
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 判断是否点击到了某个View上
     * @param view
     * @param event
     * @return
     */
    private boolean isClickedView(View view, MotionEvent event){
        if (view != null ){
            int[] pos ={0,0};
            view.getLocationInWindow(pos);
            int left = pos[0];
            int top = pos[1];
            int right = left + view.getWidth();
            int bottom = top + view.getHeight();

            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() <bottom){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

}
