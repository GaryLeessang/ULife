package com.example.joe.cityumobile.Manager;

import android.text.TextUtils;
import android.util.Log;

import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.MyObservable;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.DataAccess.LocalDataAccess;
import com.example.joe.cityumobile.DataModel.BmobModel.ServiceOrder;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

//聊天管理器（在MainActivity创建时被初始化）
public class ChatManager implements MyObservable {

    /**
     * 单例内部类
     */
    private static class Holder{
        private final static ChatManager instance = new ChatManager();
    }

    /**
     * 获取单例
     * @return
     */
    public static ChatManager getInstance(){
        return Holder.instance;
    }

    private BmobIMConversation currentConversation;
    private User currentTargetUser;
    private ServiceOrder currentOrder;
    private List<BmobIMMessage> msgList;

    public boolean isConnected = false;

    private ChatManager(){
        msgList = new ArrayList<>();
    }

    /**
     * 初始化
     */
    public void init(){
        connectServer();
        registerConnectionListener();
    }

    public void setCurrentTargetUser(User targetUser){
        currentTargetUser = targetUser;
    }

    public User getCurrentTargetUser(){
        return currentTargetUser;
    }

    public void setCurrentOrder(ServiceOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    public ServiceOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentConversation(BmobIMConversation conversation){
        currentConversation = conversation;
        currentConversation.getConversationTitle();
    }

    public BmobIMConversation getCurrentConversation(){
        return currentConversation;
    }

    /**
     * 连接服务器
     */
    public void connectServer(){
        Log.e("My","正在连接服务器...");
        User user = BmobUser.getCurrentUser(User.class);
        if (!TextUtils.isEmpty(user.getUsername())){
            BmobIM.connect(user.getUsername(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        isConnected = true;
                        Utils.note("连接成功");
                        LocalDataAccess.getConversationDA().cleanAllMarkedConversation();
                    }else{
                        isConnected = false;
                        Utils.note("连接失败");
                    }
                }
            });
        }
    }

    /**
     * 监听服务器连接情况
     */
    private void registerConnectionListener(){
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                if (connectionStatus == ConnectionStatus.CONNECTED){
                    isConnected = true;
                }else{
                    isConnected = false;
                }
            }
        });
    }

    /**
     * 从数据库加载历史消息
     */
    public void loadMessages(){
        msgList = currentConversation.getMessages();
        Collections.reverse(msgList);
    }

    public void onMessageReceived(MessageEvent event){
        Log.e("My",event.getMessage().getMsgType());
        if (!msgList.contains(event.getMessage())){
            msgList.add(event.getMessage());
        }
        notifyObservers(EventMessageType.MESSAGE_ADD);
    }

    public void onMessageSendOut(BmobIMMessage message){
        if (!msgList.contains(message)){
            if (message.getToId().equals(MyUserManager.currentUser.getUsername())){
                return;
            }
            msgList.add(message);
        }
        notifyObservers(EventMessageType.MESSAGE_ADD);
    }

    public List<BmobIMMessage> getMsgList(){
        if (msgList == null){
            msgList = new ArrayList<>();
        }
        return msgList;
    }

    //------------------------观察者区域----------------------------//

    /**
     * 添加一个观察者
     * @param observer
     */
    @Override
    public void addObserver(MyObserver observer) {
        if (observers.contains(observer)){
            return;
        }
        observers.add(observer);
    }

    /**
     * 移除一个观察者
     * @param observer
     */
    @Override
    public void removeObserver(MyObserver observer) {
        if (!observers.contains(observer)){
            return;
        }
        observers.remove(observer);
    }

    /**
     * 通知观察者
     * @param eventMessageCode
     */
    @Override
    public void notifyObservers(Integer... eventMessageCode) {
        for (MyObserver observer:observers) {
            try {
                observer.update(eventMessageCode);
            }catch (Exception e){

            }
        }
    }


}
