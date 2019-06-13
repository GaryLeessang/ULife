package com.example.joe.cityumobile.Core.Base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i);

    @Override
    public abstract void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i);

    @Override
    public abstract int getItemCount();
}
