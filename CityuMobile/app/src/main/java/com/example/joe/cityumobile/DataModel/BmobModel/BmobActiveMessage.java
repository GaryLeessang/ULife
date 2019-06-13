package com.example.joe.cityumobile.DataModel.BmobModel;

import cn.bmob.newim.bean.BmobIMExtraMessage;

public class BmobActiveMessage extends BmobIMExtraMessage {

    public static final String ACTIVE = "active";

    @Override
    public String getMsgType() {
        return ACTIVE;
    }

    @Override
    public boolean isTransient() {
        return true;
    }
}
