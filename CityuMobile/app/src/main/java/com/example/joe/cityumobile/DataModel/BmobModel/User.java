package com.example.joe.cityumobile.DataModel.BmobModel;

import android.content.Context;
import android.widget.Toast;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class User extends BmobUser {

    private String nickName;

    private String motto;

    private String major;

    private int avatar;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public String getNickName(){
        return this.nickName;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getMotto(){
        return this.motto;
    }

    public void setMajor(String major){
        this.major = major;
    }

    public String getMajor(){
        return this.major;
    }

    @Override
    public String toString() {
        return getUsername()+" - "+nickName;
    }
}
