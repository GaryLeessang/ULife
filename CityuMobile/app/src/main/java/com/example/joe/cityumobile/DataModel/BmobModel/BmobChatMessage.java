package com.example.joe.cityumobile.DataModel.BmobModel;

import com.example.joe.cityumobile.Core.MyObservable;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.DataModel.DaoModel.ChatObj;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;

public class BmobChatMessage extends BmobIMExtraMessage {

    public static final String CHAT = "chat";

    public static ChatObj convert(BmobIMMessage message,boolean isOffline){
        ChatObj chatObj = new ChatObj();
        chatObj.setSenderId(message.getFromId());
        chatObj.setReceiverId(message.getToId());
        chatObj.setContent(message.getContent());
        chatObj.setCreateTime(message.getCreateTime());
        chatObj.setChatId((long)chatObj.hashCode());
        chatObj.setIsOfflineMsg(isOffline);

        return chatObj;
    }


}
