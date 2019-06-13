package com.example.joe.cityumobile.Core;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图像加载接口
 */
public interface ILoader {

    /**
     * 加载头像
     * @param imageView
     * @param url
     * @param defaultRes
     */
    void loadAvatar(ImageView imageView,String url,int defaultRes);

    /**
     * 加载图片
     * @param imageView
     * @param url
     * @param defaultRes
     * @param listener
     */
    void loadImage(ImageView imageView, String url, int defaultRes, ImageLoadingListener listener);

}
