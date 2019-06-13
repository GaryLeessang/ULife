package com.example.joe.cityumobile.DataModel.DaoModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class Apply {

    @Id
    private Long id;

    @Property(nameInDb = "User Id")
    private String userId;

    @Property(nameInDb = "Username")
    private String userName;

    @Property(nameInDb = "Avatar")
    private int avatar;

    @Property(nameInDb = "Note")
    private String note;

    @Property(nameInDb = "Create Time")
    private Long createTime;

    @Property(nameInDb = "Ref Post")
    private String referencePostId;

    @Property
    private boolean isOffline;

    @Property
    private int state;

    @Generated(hash = 13862949)
    public Apply(Long id, String userId, String userName, int avatar, String note,
            Long createTime, String referencePostId, boolean isOffline, int state) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.note = note;
        this.createTime = createTime;
        this.referencePostId = referencePostId;
        this.isOffline = isOffline;
        this.state = state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(referencePostId,userId);
    }

    @Generated(hash = 1176249019)
    public Apply() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAvatar() {
        return this.avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getReferencePostId() {
        return this.referencePostId;
    }

    public void setReferencePostId(String referencePostId) {
        this.referencePostId = referencePostId;
    }

    public boolean getIsOffline() {
        return this.isOffline;
    }

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }


    
}
