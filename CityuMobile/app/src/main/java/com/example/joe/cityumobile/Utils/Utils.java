package com.example.joe.cityumobile.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class Utils {

    private static Toast toast;

    //快速发布一条Toast
    public static void note(String content){
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(MyApplication.currentActivity(),content,Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;

    }

    //判断当前程序是否处于后台运行
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("My", "处于后台");
                    return true;
                }else{
                    Log.i("My", "处于前台");
                    return false;
                }
            }
        }
        return false;
    }

    public static DisplayImageOptions getDefaultOptions(boolean hasRounded, int defaultRes){
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型:设置为RGB565比起默认的ARGB_8888要节省大量的内存
//                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .resetViewBeforeLoading(true);//设置图片在下载前是否重置，复位
        if(hasRounded){
            builder.displayer(new RoundedBitmapDisplayer(12));//是否设置为圆角，弧度为多少
        }
        if(defaultRes!=0){
            builder.showImageForEmptyUri(defaultRes)//设置图片Uri为空或是错误的时候显示的图片
//                            .showImageOnLoading(defaultRes) //设置图片在下载期间显示的图片-->应该去掉-会造成ListView中图片闪烁
                    .showImageOnFail(defaultRes);  //设置图片加载/解码过程中错误时候显示的图片
        }
        return builder.build();//构建完成
    }

    public static int getAvatarResId(int avatarId){
        switch (avatarId){
            case 0:
                return (R.drawable.boy);
            case 1:
                return (R.drawable.boy_1);
            case 2:
                return (R.drawable.girl);
            case 3:
                return (R.drawable.girl_1);
            default:
                return (R.mipmap.ic_launcher_round);
        }
    }

    public static void setViewTint(View view, String colorStr){
        view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorStr)));
    }
}
