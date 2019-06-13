package com.example.joe.cityumobile.View.Adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Core.Base.BaseSwipeRecyclerAdapter;
import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.ISingletonAdapter;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Manager.OrderManager;
import com.example.joe.cityumobile.View.Adapter.ViewHolder.OrderHolder;

//会话列表适配器
public class OrderAdapter extends BaseSwipeRecyclerAdapter implements MyObserver, ISingletonAdapter {

    public OrderAdapter(){
        //订阅ChatManager
        //ChatManager.getInstance().addObserver(this);

        eventCodes.add(EventMessageType.ORDER_CHANGE);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new OrderHolder(viewGroup.getContext(),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder viewHolder, final int i) {
        viewHolder.setData(OrderManager.getInstance().orderList.get(i));
        viewHolder.fillComponents();
    }

    @Override
    public int getItemCount() {
        return OrderManager.getInstance().orderList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }

    /**
     * 只关注对话的增删情况
     * @param eventMessageCode
     */
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
