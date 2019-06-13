package com.example.joe.cityumobile.View.Adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.ISingletonAdapter;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.Core.Base.BaseRecyclerAdapter;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ExpressHolder;

public class ExpressAdapter extends BaseRecyclerAdapter implements MyObserver, ISingletonAdapter{

    public ExpressAdapter(){
        eventCodes.add(EventMessageType.POST_UPDATE);
        PostManager.getInstance().addObserver(this);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ExpressHolder(viewGroup.getContext(),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder postViewHolder, int i) {
        //postViewHolder.setData(PostManager.getInstance().getExpressPostList().get(i));
        postViewHolder.setData(PostManager.getInstance().getFilteredPostList().get(i));
        postViewHolder.fillComponents();
    }

    @Override
    public int getItemCount() {
        //return PostManager.getInstance().getExpressPostList().size();
        return PostManager.getInstance().getFilteredPostList().size();
    }

    @Override
    public void update(Integer... eventMessageCode) {
        for (Integer code:eventMessageCode) {
            if (eventCodes.contains(code)){
                notifyDataSetChanged();
                return;
            }
        }
    }
}
