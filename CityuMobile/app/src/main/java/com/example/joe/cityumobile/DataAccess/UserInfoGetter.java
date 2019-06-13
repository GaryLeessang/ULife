package com.example.joe.cityumobile.DataAccess;

import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.Core.Listener.QueryUserListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserInfoGetter{
    public User user;
    String uid;

    public UserInfoGetter(String uid){
        this.uid = uid;
        user = new User();
    }

    public void info(final QueryUserListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username",uid);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    if (list != null && list.size() > 0){
                        listener.done(list.get(0),null);
                    }else{
                        listener.done(null,new BmobException("No such user"));
                    }
                }else{
                    listener.done(null,e);
                }

            }
        });

    }


}
