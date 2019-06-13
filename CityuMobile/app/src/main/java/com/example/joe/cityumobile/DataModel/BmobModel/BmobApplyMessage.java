package com.example.joe.cityumobile.DataModel.BmobModel;

import android.text.TextUtils;

import com.example.joe.cityumobile.DataModel.DaoModel.Apply;

import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

public class BmobApplyMessage extends BmobIMExtraMessage {

    public static final String APPLY = "apply";

    public static Apply convert(BmobIMMessage message){

        Apply apply = new Apply();
        apply.setUserId(message.getFromId());
        apply.setCreateTime(message.getCreateTime());
        apply.setNote(message.getContent());
        try{
            String extra = message.getExtra();
            if (!TextUtils.isEmpty(extra)){
                JSONObject jsonObject = new JSONObject(extra);
                String nickname = jsonObject.getString("nickname");
                int avatar = jsonObject.getInt("avatar");
                String postId = jsonObject.getString("postRef");
                apply.setReferencePostId(postId);
                apply.setUserName(nickname);
                apply.setAvatar(avatar);
                apply.setId((long) apply.hashCode());
                apply.setState(0);
            }else{
                return null;
            }

        }catch (Exception e){
            return null;
        }

        return apply;
    }

    @Override
    public String getMsgType() {
        return APPLY;
    }

    @Override
    public boolean isTransient() {
        return true;
    }
}
