package com.example.joe.cityumobile.DataModel.BmobModel;

import cn.bmob.v3.BmobObject;

public class ServiceOrder extends BmobObject {

    private Post Post;

    private User serviceProvider;

    private User serviceReceiver;

    private String state;

    public User getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(User serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public User getServiceReceiver() {
        return serviceReceiver;
    }

    public void setServiceReceiver(User serviceReceiver) {
        this.serviceReceiver = serviceReceiver;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Post getPost() {
        return Post;
    }

    public void setPost(Post post) {
        this.Post = post;
    }
}
