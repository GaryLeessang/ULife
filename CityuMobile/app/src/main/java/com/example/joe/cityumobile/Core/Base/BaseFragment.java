package com.example.joe.cityumobile.Core.Base;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class BaseFragment extends Fragment {

    /**
     * 设置监听器
     */
    public abstract void setListener();

    /**
     * 判断是否点击到了某个View上
     * @param view
     * @param event
     * @return
     */
    public boolean isClickedView(View view, MotionEvent event){
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

    //隐藏输入软键盘
    public void hideSoftKeyboard(IBinder token){
        if (token != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
