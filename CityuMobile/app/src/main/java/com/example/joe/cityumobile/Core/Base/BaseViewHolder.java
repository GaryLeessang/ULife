package com.example.joe.cityumobile.Core.Base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * RecyclerView数据载体基类
 * @param <T>
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    protected Context context;

    public BaseViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        this.context=context;
        findViews();
    }

    public Context getContext() {
        return context;
    }

    /**
     * 找到并绑定所有UI组件
     */
    public abstract void findViews();

    /**
     * 设置数据元
     * @param t
     */
    public abstract void setData(T t);

    /**
     * 填充UI组件
     */
    public abstract void fillComponents();
}
