package com.example.joe.cityumobile.DataModel.DaoModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Objects;

@Entity
public class Conversation {

    @Id
    private Long conId;

    @Property(nameInDb = "targetUid")
    private String targetUid;

    @Property(nameInDb = "nickName")
    private String targetNickname;

    @Property(nameInDb = "avatar")
    private int avatar;

    @Property(nameInDb = "unreadCount")
    private int unreadCount;

    @Property(nameInDb = "flag")
    private int flag;

    @Generated(hash = 436160109)
    public Conversation(Long conId, String targetUid, String targetNickname,
            int avatar, int unreadCount, int flag) {
        this.conId = conId;
        this.targetUid = targetUid;
        this.targetNickname = targetNickname;
        this.avatar = avatar;
        this.unreadCount = unreadCount;
        this.flag = flag;
    }

    @Generated(hash = 1893991898)
    public Conversation() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetUid);
    }

    public Long getConId() {
        return this.conId;
    }

    public void setConId(Long conId) {
        this.conId = conId;
    }

    public String getTargetUid() {
        return this.targetUid;
    }

    public void setTargetUid(String targetUid) {
        this.targetUid = targetUid;
    }

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTargetNickname() {
        return this.targetNickname;
    }

    public void setTargetNickname(String targetNickname) {
        this.targetNickname = targetNickname;
    }

    public int getAvatar() {
        return this.avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}