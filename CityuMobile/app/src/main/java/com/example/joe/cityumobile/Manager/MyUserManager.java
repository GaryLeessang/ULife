package com.example.joe.cityumobile.Manager;

import android.util.Log;

import com.example.joe.cityumobile.Core.Listener.QueryUserListener;
import com.example.joe.cityumobile.DataModel.BmobModel.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 用户管理器
 */
public class MyUserManager {

    public static User currentUser;

    public static List<User> userList;

    public static void setCurrentUser(User user){
        currentUser = user;
        Log.e("My","设置了当前用户："+user.toString());
    }

    /**
     * 登出用户
     */
    public static void logout(){
        BmobUser.logOut();
    }

    /**
     * 查询用户
     * @param userId
     * @param listener
     */
    public static void findUserById(String userId, final QueryUserListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username",userId);
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

    public static void updateUserInfo(){
        if (currentUser != null){
            currentUser.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {

                }
            });
        }
    }
}
