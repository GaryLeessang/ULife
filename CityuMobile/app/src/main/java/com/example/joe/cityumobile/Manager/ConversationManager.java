package com.example.joe.cityumobile.Manager;

import android.util.Log;

import com.example.joe.cityumobile.Core.Listener.ConversationOpenListener;
import com.example.joe.cityumobile.DataModel.BmobModel.ServiceOrder;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.DataModel.DaoModel.Conversation;
import com.example.joe.cityumobile.Utils.Utils;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 会话管理器，负责开启会话，管理会话记录
 */
public class ConversationManager {

    /**
     * 单例内部类
     */
    private static class Holder{
        private final static ConversationManager instance = new ConversationManager();
    }

    /**
     * 获取单例
     * @return
     */
    public static ConversationManager getInstance(){
        return Holder.instance;
    }

    private ConversationManager(){

    }

    public void startConversation(final User targetUser, final ConversationOpenListener onStartListener){
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(targetUser.getUsername());
        info.setName(targetUser.getNickName());
        BmobIM.getInstance().startPrivateConversation(info,new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                if (e == null){
                    BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),bmobIMConversation);
                    conversation.setConversationTitle(targetUser.getNickName());
                    conversation.setConversationIcon(String.valueOf(targetUser.getAvatar()));
                    if (onStartListener != null){
                        onStartListener.onStarted(conversation);
                    }
                }else{
                    Utils.note("创建失败");
                    Log.e("My",e.toString());
                }
            }
        });
    }

    public void loadAllConv(){
        List<BmobIMConversation> conversationList = BmobIM.getInstance().loadAllConversation();
        Log.e("My","Conversation Count:"+conversationList.size());
        for (BmobIMConversation conv:conversationList) {
            Log.e("My","Conversation title:"+conv.getConversationTitle());
        }
    }

    private void addConversationToDB(Conversation conversation){

    }

    private void loadOrderConversationFromDB(List<ServiceOrder> orders){

    }

}
