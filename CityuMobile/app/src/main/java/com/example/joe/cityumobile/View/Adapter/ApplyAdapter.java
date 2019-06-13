package com.example.joe.cityumobile.View.Adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Core.Base.BaseSwipeRecyclerAdapter;
import com.example.joe.cityumobile.Core.ISingletonAdapter;
import com.example.joe.cityumobile.Manager.ApplyManager;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ApplyHolder;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Core.MyObserver;

/**
 * 新聊天申请的适配器（支持滑动操作）
 */
public class ApplyAdapter extends BaseSwipeRecyclerAdapter implements MyObserver, ISingletonAdapter {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ApplyHolder(viewGroup.getContext(),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder viewHolder, final int i) {
        viewHolder.setData(ApplyManager.getInstance().applyList.get(i));
        viewHolder.fillComponents();
    }

    @Override
    public int getItemCount() {
        return ApplyManager.getInstance().applyList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }

    @Override
    public void update(Integer... eventMessageCode) {
        notifyDataSetChanged();
    }

}
