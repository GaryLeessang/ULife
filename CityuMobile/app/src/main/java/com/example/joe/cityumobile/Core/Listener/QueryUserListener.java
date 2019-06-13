package com.example.joe.cityumobile.Core.Listener;

import com.example.joe.cityumobile.DataModel.BmobModel.User;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

public abstract class QueryUserListener extends BmobListener1<User> {

    public abstract void done(User user, BmobException e);

    @Override
    protected void postDone(User user, BmobException e) {

    }
}
