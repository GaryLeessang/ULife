package com.example.joe.cityumobile.Manager;

import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.Listener.ConversationOpenListener;
import com.example.joe.cityumobile.Core.Listener.QueryUserListener;
import com.example.joe.cityumobile.Core.Listener.SimpleListener;
import com.example.joe.cityumobile.Core.MyObservable;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.DataAccess.LocalDataAccess;
import com.example.joe.cityumobile.DataModel.BmobModel.BmobApplyMessage;
import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.DataModel.DaoModel.Apply;
import com.example.joe.cityumobile.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

public class ApplyManager implements MyObservable {

    public static final int APPLY_UNHANDLE = 0;
    public static final int APPLY_APPROVAL = 1;
    public static final int APPLY_REJECT = 2;

    /**
     * 单例内部类
     */
    private static class Holder{
        private final static ApplyManager instance = new ApplyManager();
    }

    /**
     * 获取单例
     * @return
     */
    public static ApplyManager getInstance(){
        return Holder.instance;
    }

    private ApplyManager(){
        applyList = new ArrayList<>();
    }

    public List<Apply> applyList;

    public void loadAllApplyFromDB(){
        applyList = LocalDataAccess.getApplyDA().loadAllRecords(new SimpleListener() {
            @Override
            public void done() {
                notifyObservers(EventMessageType.APPLY_MESSAGE_CHANGE);
            }
        });
    }

    public void onApplyReceived(MessageEvent event){
        Apply apply = BmobApplyMessage.convert(event.getMessage());
        LocalDataAccess.getApplyDA().addRecord(apply, false, new SimpleListener() {
            @Override
            public void done() {
                loadAllApplyFromDB();
            }
        });
    }

    public void sendApply(User targetUser, final String postId, final String note){
        ConversationManager.getInstance().startConversation(targetUser, new ConversationOpenListener() {
            @Override
            public void onStarted(BmobIMConversation conversation) {
                BmobApplyMessage applyMessage = new BmobApplyMessage();
                applyMessage.setMsgType("apply");
                applyMessage.setContent(note);
                applyMessage.setIsTransient(true);
                applyMessage.setFromId(BmobIM.getInstance().getCurrentUid());
                Map<String,Object> map = new HashMap<>();
                map.put("nickname",MyUserManager.currentUser.getNickName());
                map.put("avatar",MyUserManager.currentUser.getAvatar());
                map.put("postRef",postId);
                applyMessage.setExtraMap(map);

                conversation.sendMessage(applyMessage, new MessageSendListener() {
                    @Override
                    public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                        if (e == null){
                            Utils.note("申请发送成功");
                        }else{
                            Utils.note("申请发送失败");
                        }
                    }
                });
            }
        });
    }

    public void approveApply(final Apply apply){
        for (Apply application:applyList) {
            if (application.equals(apply)){
                application.setState(APPLY_APPROVAL);
                LocalDataAccess.getApplyDA().updateRecord(apply);

                MyUserManager.findUserById(apply.getUserId(), new QueryUserListener() {
                    @Override
                    public void done(User applier, BmobException e) {
                        Post post = new Post();
                        post.setObjectId(apply.getReferencePostId());
                        if (PostManager.getInstance().findExpressById(apply.getReferencePostId()).getPostType() == 1){
                            OrderManager.getInstance().createOrder(applier,MyUserManager.currentUser,post);
                        }else{
                            OrderManager.getInstance().createOrder(MyUserManager.currentUser,applier,post);
                        }
                    }
                });

                OrderManager.getInstance().loadOrdersFromCloud(new SimpleListener() {
                    @Override
                    public void done() {
                        OrderManager.getInstance().notifyObservers(EventMessageType.ORDER_CHANGE);
                    }
                });

            }else{
                if (application.getReferencePostId().equals(apply.getReferencePostId())){
                    apply.setState(APPLY_REJECT);
                    LocalDataAccess.getApplyDA().updateRecord(apply);
                }
            }
        }
        loadAllApplyFromDB();
    }

    public void rejectApply(Apply apply){
        apply.setState(APPLY_REJECT);
        LocalDataAccess.getApplyDA().updateRecord(apply);
        loadAllApplyFromDB();
    }

    public void findApplierById(String id){

    }

    /**
     * 添加一个观察者
     *
     * @param observer
     */
    @Override
    public void addObserver(MyObserver observer) {
        if (observers.contains(observer)){
            return;
        }
        observers.add(observer);
    }

    /**
     * 移除指定观察者
     *
     * @param observer
     */
    @Override
    public void removeObserver(MyObserver observer) {
        observers.remove(observer);
    }

    /**
     * 通知观察者
     *
     * @param eventMessageCode
     */
    @Override
    public void notifyObservers(Integer... eventMessageCode) {
        for (MyObserver observer:observers) {
            observer.update(eventMessageCode);
        }
    }
}
