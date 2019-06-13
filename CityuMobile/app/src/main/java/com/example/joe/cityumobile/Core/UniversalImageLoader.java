package com.example.joe.cityumobile.Core;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.joe.cityumobile.Utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图像加载器
 */
public class UniversalImageLoader implements ILoader {

    public UniversalImageLoader(){

    }

    private void display(ImageView iv, String url, boolean isCircle, int defaultRes, ImageLoadingListener listener){
        if (iv == null){
            return;
        }
        if(!url.equals(iv.getTag())){//增加tag标记，减少UIL的display次数
            iv.setTag(url);
            //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
            ImageAware imageAware = new ImageViewAware(iv, false);
            ImageLoader.getInstance().displayImage(url, imageAware, Utils.getDefaultOptions(isCircle,defaultRes),listener);
        }
    }

    /**
     * 初始化
     * @param context
     */
    public static void initImageLoader(Context context){
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public void loadAvatar(ImageView imageView, String url, int defaultRes) {
        if(!TextUtils.isEmpty(url)){
            display(imageView,url,true,defaultRes,null);
        } else {
            imageView.setImageResource(defaultRes);
        }
    }

    @Override
    public void loadImage(ImageView imageView, String url, int defaultRes, ImageLoadingListener listener) {
        if(!TextUtils.isEmpty(url)){
            display(imageView,url.trim(),false,defaultRes,listener);
        } else {
            imageView.setImageResource(defaultRes);
        }
    }
}
