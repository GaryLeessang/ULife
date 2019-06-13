package com.example.joe.cityumobile.View.Adapter;

import android.util.Log;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Core.Base.BaseSwipeRecyclerAdapter;
import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.ISingletonAdapter;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.ExpressRecordHolder;

public class MyExpressRecordAdapter extends BaseSwipeRecyclerAdapter implements MyObserver,ISingletonAdapter {

    public MyExpressRecordAdapter(){
        PostManager.getInstance().addObserver(this);
        eventCodes.add(EventMessageType.HISTORY_POST_UPDATE);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressRecordHolder(parent.getContext(),parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        viewHolder.setData(PostManager.getInstance().getExpressHistoryList().get(position));
        viewHolder.fillComponents();
    }

    @Override
    public int getItemCount() {
        return PostManager.getInstance().getExpressHistoryList().size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    @Override
    public void update(Integer... eventMessageCode) {
        for (Integer code:eventMessageCode) {
            if (eventCodes.contains(code)){
                Log.e("My","Update history post");
                notifyDataSetChanged();
                return;
            }
        }
    }
}
