package com.example.joe.cityumobile.DataModel.DaoModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;
import java.util.Objects;

@Entity
public class Express {

    @Id
    private long expressId;

    @Property
    private String bmobExpressId;

    @Property
    private String Title;

    @Property
    private String Content;

    @Property
    private int PostType;

    @Property
    private String Size;

    @Property
    private String Priority;

    @Property
    private int Commission;

    @Property
    private Date createTime;

    @Property
    private Date expireTime;

    @Override
    public int hashCode() {
        return Objects.hash(Content,PostType,Size,Priority,Commission,createTime);
    }

    @Generated(hash = 431870770)
    public Express(long expressId, String bmobExpressId, String Title,
            String Content, int PostType, String Size, String Priority,
            int Commission, Date createTime, Date expireTime) {
        this.expressId = expressId;
        this.bmobExpressId = bmobExpressId;
        this.Title = Title;
        this.Content = Content;
        this.PostType = PostType;
        this.Size = Size;
        this.Priority = Priority;
        this.Commission = Commission;
        this.createTime = createTime;
        this.expireTime = expireTime;
    }

    @Generated(hash = 760607181)
    public Express() {
    }

    public long getExpressId() {
        return this.expressId;
    }

    public void setExpressId(long expressId) {
        this.expressId = expressId;
    }

    public String getContent() {
        return this.Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public int getPostType() {
        return this.PostType;
    }

    public void setPostType(int PostType) {
        this.PostType = PostType;
    }

    public String getSize() {
        return this.Size;
    }

    public void setSize(String Size) {
        this.Size = Size;
    }

    public String getPriority() {
        return this.Priority;
    }

    public void setPriority(String Priority) {
        this.Priority = Priority;
    }

    public int getCommission() {
        return this.Commission;
    }

    public void setCommission(int Commission) {
        this.Commission = Commission;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getBmobExpressId() {
        return this.bmobExpressId;
    }

    public void setBmobExpressId(String bmobExpressId) {
        this.bmobExpressId = bmobExpressId;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

}
