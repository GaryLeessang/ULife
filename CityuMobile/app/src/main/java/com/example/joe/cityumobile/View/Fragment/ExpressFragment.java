package com.example.joe.cityumobile.View.Fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Core.AdapterFactory;
import com.example.joe.cityumobile.Core.Base.BaseFragment;
import com.example.joe.cityumobile.Core.Listener.MyTouchListener;
import com.example.joe.cityumobile.Core.Listener.PostRefreshListener;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Activity.MainActivity;
import com.example.joe.cityumobile.View.Adapter.ExpressAdapter;
import com.example.joe.cityumobile.databinding.FragmentExpressBinding;

/**
 * 论坛界面
 */
public class ExpressFragment extends BaseFragment implements MyTouchListener{

    private FragmentExpressBinding layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater,R.layout.fragment_express,container,false);
        return layout.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
    }

    @Override
    public void setListener(){
        ((MainActivity)getActivity()).addTouchListener(this);

        setFabRefreshMode();

        layout.filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    layout.clearBtn.setVisibility(View.VISIBLE);
                    setFabFilterMode();
                }else{
                    layout.clearBtn.setVisibility(View.GONE);
                    setFabRefreshMode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layout.clearBtn.setVisibility(View.GONE);
        layout.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.filter.setText("");
                PostManager.getInstance().getFilter().filter("");
            }
        });

        layout.postContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        layout.postContainer.setAdapter((RecyclerView.Adapter) AdapterFactory.getAdapter(ExpressAdapter.class));
        layout.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.animate().rotation(360);
                PostManager.getInstance().refreshPost(new PostRefreshListener() {
                    @Override
                    public void done() {
                        PostManager.getInstance().getFilter().filter(layout.filter.getText());
                        layout.postContainer.scrollToPosition(0);
                        v.setRotation(0);
                        v.setEnabled(true);
                    }
                });
                v.setEnabled(false);
            }
        });

        layout.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(PostManager.getInstance().getExpressPostList().isEmpty()){
                    PostManager.getInstance().refreshPost(new PostRefreshListener() {
                        @Override
                        public void done() {
                            PostManager.getInstance().getFilter().filter(layout.filter.getText());
                            layout.postContainer.scrollToPosition(0);
                            setFabRefreshMode();
                        }
                    });
                }else{
                    PostManager.getInstance().getFilter().filter(layout.filter.getText());
                    setFabRefreshMode();
                }
            }
        });

    }

    /**
     * 设置刷新按钮普通刷新模式
     */
    @SuppressLint("RestrictedApi")
    private void setFabRefreshMode(){
        layout.refreshBtn.setVisibility(View.VISIBLE);
        layout.filterBtn.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置刷新按钮为过滤器模式
     */
    @SuppressLint("RestrictedApi")
    private void setFabFilterMode(){
        layout.refreshBtn.setVisibility(View.INVISIBLE);
        layout.filterBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 活动销毁时移除监听器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).removeTouchListener(this);
    }

    /**
     * 点击空白处隐藏键盘
     * @param event
     */
    @Override
    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (!isClickedView(layout.filter,event)){
                hideSoftKeyboard(layout.getRoot().getWindowToken());
                layout.filter.clearFocus();
            }
        }
    }
}
