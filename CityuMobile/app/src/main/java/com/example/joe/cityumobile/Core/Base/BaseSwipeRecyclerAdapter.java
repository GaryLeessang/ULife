package com.example.joe.cityumobile.Core.Base;

import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

public abstract class BaseSwipeRecyclerAdapter extends RecyclerSwipeAdapter<BaseViewHolder> {

    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(BaseViewHolder viewHolder, int position);

    @Override
    public abstract int getItemCount();

    @Override
    public abstract int getSwipeLayoutResourceId(int position);
}
