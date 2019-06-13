package com.example.joe.cityumobile.View.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.Listener.MyTouchListener;
import com.example.joe.cityumobile.Manager.ChatManager;
import com.example.joe.cityumobile.Manager.MyFragmentManager;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Manager.OrderManager;
import com.example.joe.cityumobile.Manager.PostManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Fragment.OrderFragment;
import com.example.joe.cityumobile.View.Fragment.EditorFragment;
import com.example.joe.cityumobile.View.Fragment.ExpressFragment;
import com.example.joe.cityumobile.View.Fragment.UserFragment;
import com.example.joe.cityumobile.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

//主活动
public class MainActivity extends BaseActivity implements MyObserver {

    List<MyTouchListener> myTouchListeners = new ArrayList<>();

    /**
     * Fragment容器
     */
    private int fragmentContainer;
    /**
     * 底部导航栏
     */
    private BottomNavigationView navigation;
    private BottomNavigationMenuView menuView;
    /**
     * 聊天数量角标
     */
    private Badge conversationBadge;

    private ActivityMainBinding layout;

    public Context getContext(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setImmersiveStatusBar();
        findAndBindView();

        /**
         * 注册想监听的事件代码
         */
        eventCodes.add(EventMessageType.NEW_CONVERSATION_ADD);
        eventCodes.add(EventMessageType.CONVERSATION_DELETE);
        eventCodes.add(EventMessageType.UNREAD_COUNT_CHANGE);

        //初始化聊天管理器
        ChatManager.getInstance().init();
        //订阅聊天管理器
        ChatManager.getInstance().addObserver(this);
        OrderManager.getInstance().loadOrdersFromCloud(null);
        //默认加载第一个页面
        MyFragmentManager.loadFragment(fragmentContainer,ExpressFragment.class);
        //配置好角标视图
        setupBadgeView();

        PostManager.getInstance().loadPostHistoryFromCloud(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadMessageNotify();
    }

    //配置并绑定View
    void findAndBindView(){
        fragmentContainer = R.id.FragmentContainer;
        navigation = findViewById(R.id.navigation);
        menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 切换Fragment
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ViewPost:
                    MyFragmentManager.loadFragment(fragmentContainer,ExpressFragment.class);
                    return true;
                case R.id.navigation_dashboard:
                    MyFragmentManager.loadFragment(fragmentContainer,EditorFragment.class);
                    return true;
                case R.id.ConversationPage:
                    MyFragmentManager.loadFragment(fragmentContainer, OrderFragment.class);
                    return true;
                case R.id.UserPage:
                    MyFragmentManager.loadFragment(fragmentContainer, UserFragment.class);
                    return true;
            }
            return false;
        }
    };


    public void addTouchListener(MyTouchListener listener){
        myTouchListeners.add(listener);
    }

    public void removeTouchListener(MyTouchListener listener){
        myTouchListeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener: myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    //防止后退键一直退到根界面时程序退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //刷新未读消息数量角标提醒
    public void updateUnreadMessageNotify(){
        //conversationBadge.setBadgeNumber(ChatManager.getInstance().getTotalUnreadCount());
    }

    //初始化Conversation的角标
    private void setupBadgeView(){
        if (2 < menuView.getChildCount()) {
            // 获得viewIndex对应子tab
            View view = menuView.getChildAt(2);
            // 从子tab中获得其中显示图片的ImageView
            View icon = view.findViewById(android.support.design.R.id.icon);
            // 获得图标的宽度
            int iconWidth = icon.getWidth();
            // 获得tab的宽度/2
            int tabWidth = view.getWidth();
            // 计算badge要距离右边的距离
            float spaceWidth = tabWidth - iconWidth;

            conversationBadge = new QBadgeView(this).bindTarget(view).setGravityOffset(spaceWidth+50, 0, false);
        }
    }

    @Override
    public void update(Integer... eventMessageCode) {
        for (Integer code :eventMessageCode) {
            if (eventCodes.contains(code))
            {
                //conversationBadge.setBadgeNumber(ChatManager.getInstance().getTotalUnreadCount());
                break;
            }
        }

    }
    /**
     * 设置监听器
     */
    @Override
    public void setListener() {

    }
}
