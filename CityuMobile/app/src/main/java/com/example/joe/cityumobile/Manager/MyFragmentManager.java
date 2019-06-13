package com.example.joe.cityumobile.Manager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.example.joe.cityumobile.View.Fragment.OrderFragment;
import com.example.joe.cityumobile.View.Fragment.EditorFragment;
import com.example.joe.cityumobile.View.Fragment.ExpressFragment;
import com.example.joe.cityumobile.View.Fragment.UserFragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//碎片管理器
public class MyFragmentManager {

    //当前活动
    private static AppCompatActivity currentActivity;
    //当前所有碎片以及状态
    private static Map<Fragment,Boolean> fragmentList = Collections.synchronizedMap(new HashMap<Fragment, Boolean>());
    //当前活动的碎片管理器
    private static FragmentManager fragmentManager;
    //当前碎片管理器的事物
    private static FragmentTransaction fragmentTransaction;

    private MyFragmentManager(){

    }

    //初始化
    public static void initial(){

        //初始化所有碎片，并且注册
        ExpressFragment expressFragment = new ExpressFragment();
        EditorFragment editorFragment = new EditorFragment();
        OrderFragment orderFragment = new OrderFragment();
        UserFragment userFragment = new UserFragment();

        fragmentList.put(expressFragment,false);
        fragmentList.put(editorFragment,false);
        fragmentList.put(orderFragment,false);
        fragmentList.put(userFragment,false);

    }

    //向指定容器中用类名装载碎片
    @TargetApi(Build.VERSION_CODES.N)
    public static void loadFragment(int container,Class<?> fragmentClass){
        if (!setFragmentManager()){
            return;
        }

        if (fragmentManager !=null){
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = findFragment(fragmentClass);
            if (fragment != null){
                fragmentTransaction.replace(container,fragment);
                fragmentTransaction.commit();
                fragmentList.replace(fragment,true);
            }
        }
    }

    //移除指定容器中的当前碎片
    @TargetApi(Build.VERSION_CODES.N)
    public static void removeFragment(int container){
        if (!setFragmentManager()){
            return;
        }

        if (fragmentManager !=null){
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentById(container);
            if (fragment != null){
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                fragmentList.replace(fragment,false);
            }
        }
    }

    //用类名找碎片
    public static Fragment findFragment(Class<?> fragment){
        for (Fragment key:fragmentList.keySet()) {
            if (key.getClass().equals(fragment)){
                return key;
            }
        }
        return null;
    }

    //获取指定碎片的状态
    public static boolean getFragmentState(Fragment fragment){
        if (fragment != null){
            if (fragmentList.containsKey(fragment)){
                return fragmentList.get(fragment);
            }else{
                return false;
            }
        }
        return false;
    }

    public static boolean getFragmentStateByClass(Class<?> fragment){
        Fragment frag = findFragment(fragment);
        if (frag != null){
            return getFragmentState(frag);
        }else{
            return false;
        }
    }

    //获取当前活动并设置碎片管理器
    private static boolean setFragmentManager(){
        try{
            currentActivity = (AppCompatActivity) MyApplication.currentActivity();
            fragmentManager = currentActivity.getSupportFragmentManager();
            return true;
        }catch (Exception e){
            Log.e("FragmentManager","failed to get current activity");
            return false;
        }
    }
}
