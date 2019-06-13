package com.example.joe.cityumobile.DataModel.DaoModel;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import java.util.Objects;

@Entity
public class ChatObj {

    @Id
    private Long chatId;

    @Property(nameInDb = "senderId")
    private String senderId;

    @Property(nameInDb = "receiverId")
    private String receiverId;

    @Property(nameInDb = "createTime")
    private Long createTime;

    @Property(nameInDb = "content")
    private String content;

    @Property(nameInDb = "isOffline")
    private boolean isOfflineMsg;

    @Generated(hash = 1015832050)
    public ChatObj() {
    }

    @Generated(hash = 1380320052)
    public ChatObj(Long chatId, String senderId, String receiverId, Long createTime,
            String content, boolean isOfflineMsg) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createTime = createTime;
        this.content = content;
        this.isOfflineMsg = isOfflineMsg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId,createTime);
    }

    public Long getChatId() {
        return this.chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean getIsOfflineMsg() {
        return this.isOfflineMsg;
    }

    public void setIsOfflineMsg(boolean isOfflineMsg) {
        this.isOfflineMsg = isOfflineMsg;
    }


}