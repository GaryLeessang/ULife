package com.example.joe.cityumobile.DataModel.BmobModel;

import com.example.joe.cityumobile.DataModel.DaoModel.Express;
import com.example.joe.cityumobile.Utils.TimeUtils;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Post extends BmobObject {

    private String Title;

    private String Content;

    private User Publisher;

    private int PostType;

    private String Size;

    private String Priority;

    private int Commission;

    private BmobDate ExpireDate;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public int getCommission() {
        return Commission;
    }

    public void setCommission(int commission) {
        Commission = commission;
    }

    public int getPostType() {
        return PostType;
    }

    public void setPostType(int postType) {
        PostType = postType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public BmobDate getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(Date expireDate) {
        ExpireDate = new BmobDate(expireDate);
    }

    public User getPublisher() {
        return Publisher;
    }

    public void setPublisher(User publisher) {
        Publisher = publisher;
    }

    public static Express Convert(Post expressPost){
        Express express = new Express();
        express.setTitle(expressPost.getTitle());
        express.setPostType(expressPost.getPostType());
        express.setCommission(expressPost.getCommission());
        express.setContent(expressPost.getContent());
        express.setPriority(expressPost.getPriority());
        express.setSize(expressPost.getSize());
        express.setCreateTime(TimeUtils.stringToDate(expressPost.getCreatedAt(),TimeUtils.FORMAT_DATE_TIME_SECOND));
        express.setExpireTime(TimeUtils.stringToDate(expressPost.getExpireDate().getDate(),TimeUtils.FORMAT_DATE_TIME_SECOND));
        express.setBmobExpressId(expressPost.getObjectId());
        express.setExpressId(express.hashCode());
        return express;
    }

}
