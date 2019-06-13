package com.example.joe.cityumobile.Manager;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.joe.cityumobile.Core.UniversalImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.newim.BmobIM;

public class MyApplication extends Application {

    String[] Permission = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.FOREGROUND_SERVICE};

    //APP唯一实例
    public static MyApplication instance;

    //所有活动列表
    private static List<Activity> activityList = Collections.synchronizedList(new LinkedList<Activity>());

    public boolean isUserLogin = false;

    //初始化
    public void onCreate(){
        super.onCreate();
        registerActivityListener();
        if(getApplicationInfo().packageName.equals(getMyProcessName())){
            try{
                BmobIM.init(this);
                BmobIM.registerDefaultMessageHandler(new MessageHandler(getApplicationContext()));

                MyFragmentManager.initial();
                instance = this;
            }catch (Exception e){
                Log.e("My",e.toString());
            }
        }
        UniversalImageLoader.initImageLoader(this);
    }

    //获取当前进程名称
    public String getMyProcessName(){
        try{
            File file = new File("/proc/"+android.os.Process.myPid() + "/"+"cmdline");
            BufferedReader mBufferedRedaer = new BufferedReader(new FileReader(file));
            String processName = mBufferedRedaer.readLine().trim();
            mBufferedRedaer.close();
            return processName;
        }catch (Exception e){
            return null;
        }
    }

    public static void startActivityFromHere(Intent intent){
        if (currentActivity()!=null){
            currentActivity().startActivity(intent);
        }
    }

    //添加一个新的活动
    public void pushActivity(Activity activity){
        activityList.add(activity);
    }

    //删除一个现有活动
    public void popActivity(Activity activity){
        activityList.remove(activity);
    }

    //获取当前活动
    public static Activity currentActivity(){
        if (activityList == null || activityList.isEmpty()){
            return null;
        }

        Activity activity = activityList.get(activityList.size()-1);
        return  activity;
    }

    public static void finishAllActivity(){
        for (Activity act :activityList) {
            finishActivity(act);
        }
    }

    //结束当前活动
    public static void finishCurrentActivity(){
        if (activityList == null || activityList.isEmpty()){
            return;
        }

        Activity activity = activityList.get(activityList.size()-1);
        finishActivity(activity);
    }

    //结束一个活动
    public static void finishActivity(Activity activity){
        if (activityList == null || activityList.isEmpty()){
            return;
        }

        if (activity !=null){
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    //用类名结束一个活动
    public static void finishActivity(Class<?> className) {
        if (activityList == null||activityList.isEmpty()) {
            return;
        }
        for (Activity activity : activityList) {
            if (activity.getClass().equals(className)) {
                finishActivity(activity);
            }
        }
    }

    //用类名找到一个活动
    public static Activity findActivity(Class<?> className) {
        Activity targetActivity = null;
        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(className)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    //注册活动生命周期监听回调函数
    private void registerActivityListener(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                pushActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activityList == null || activityList.isEmpty()){
                    return;
                }
                if (activityList.contains(activity)){
                    popActivity(activity);
                }
            }
        });
    }

    //检查所有权限
    public void checkAllPermission(){
        if (MyApplication.currentActivity() != null){
            ActivityCompat.requestPermissions(MyApplication.currentActivity(),Permission,1);
        }
    }
}
