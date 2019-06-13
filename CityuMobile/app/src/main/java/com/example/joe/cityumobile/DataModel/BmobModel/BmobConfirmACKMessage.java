package com.example.joe.cityumobile.DataModel.BmobModel;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * 交易确认消息
 */
public class BmobConfirmACKMessage extends BmobIMExtraMessage {

    public static final String CONFIRM_ACK = "confirmAck";

    public BmobConfirmACKMessage(){
        setContent("confirmAck");
    }

    @Override
    public String getMsgType() {
        return CONFIRM_ACK;
    }

    @Override
    public boolean isTransient() {
        return true;
    }
    
}
