package com.example.joe.cityumobile.Utils;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.example.joe.cityumobile.Manager.MyApplication;

public class KeyBoardUtils {

    public static void hideSoftKeyboard(IBinder token){
        if (token != null){
            InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.currentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
