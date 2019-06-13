package com.example.joe.cityumobile.DataModel.BmobModel;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * 交易确认消息
 */
public class BmobConfirmMessage extends BmobIMExtraMessage {

    public static final String CONFIRM = "confirm";

    @Override
    public String getMsgType() {
        return CONFIRM;
    }

    @Override
    public boolean isTransient() {
        return false;
    }

}
