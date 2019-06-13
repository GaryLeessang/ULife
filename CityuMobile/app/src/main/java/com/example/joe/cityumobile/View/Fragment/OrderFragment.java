package com.example.joe.cityumobile.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.Listener.SimpleListener;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Manager.ApplyManager;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.OrderManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Activity.ApplyActivity;
import com.example.joe.cityumobile.View.Adapter.OrderAdapter;

public class OrderFragment extends Fragment implements MyObserver {

    private RecyclerView recyclerView;
    private TextView applyHint;
    private TextView applyCount;
    private FloatingActionButton refreshOrderBtn;

    OrderAdapter adapter;

    public OrderFragment(){
        //第一次创建这个Fragment的时候去服务器拉取一次所有和自己有关的Order
        OrderManager.getInstance().loadOrdersFromCloud(null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.conversationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderFragment.this.getContext()));
        adapter = (OrderAdapter) AdapterFactory.getAdapter(OrderAdapter.class);
        recyclerView.setAdapter(adapter);

        applyHint = view.findViewById(R.id.applyHint);
        applyHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.currentActivity(), ApplyActivity.class);
                startActivity(intent);
            }
        });

        applyCount = view.findViewById(R.id.applyCount);

        refreshOrderBtn = view.findViewById(R.id.refreshBtn);
        refreshOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderManager.getInstance().loadOrdersFromCloud(new SimpleListener() {
                    @Override
                    public void done() {
                        OrderManager.getInstance().notifyObservers(EventMessageType.ORDER_CHANGE);
                    }
                });
            }
        });

        eventCodes.add(EventMessageType.APPLY_MESSAGE_CHANGE);
        ApplyManager.getInstance().addObserver(this);
        ApplyManager.getInstance().loadAllApplyFromDB();

        OrderManager.getInstance().addObserver(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplyManager.getInstance().removeObserver(this);
        OrderManager.getInstance().removeObserver(adapter);
    }

    @Override
    public void update(Integer... eventMessageCode) {
        for (Integer code:eventMessageCode) {
            if (eventCodes.contains(code)){
                if (ApplyManager.getInstance().applyList.size()>0){
                    applyHint.setText("Current new apply:");
                    applyCount.setVisibility(View.VISIBLE);
                    applyCount.setText(String.valueOf(ApplyManager.getInstance().applyList.size()));
                }else{
                    applyCount.setVisibility(View.GONE);
                    applyHint.setText("Currently no new apply");
                }
            }
        }
    }
}
