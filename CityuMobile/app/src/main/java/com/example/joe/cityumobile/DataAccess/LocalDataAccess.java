package com.example.joe.cityumobile.DataAccess;

import android.content.Context;
import android.util.Log;

import com.example.joe.cityumobile.Core.Listener.SimpleListener;
import com.example.joe.cityumobile.DataModel.DaoModel.Apply;
import com.example.joe.cityumobile.DataModel.DaoModel.ChatObj;
import com.example.joe.cityumobile.DataModel.DaoModel.Conversation;
import com.example.joe.cityumobile.DataModel.DaoModel.Express;
import com.example.joe.cityumobile.greenDaoGenerated.ApplyDao;
import com.example.joe.cityumobile.greenDaoGenerated.ChatObjDao;
import com.example.joe.cityumobile.greenDaoGenerated.ConversationDao;
import com.example.joe.cityumobile.greenDaoGenerated.DaoMaster;
import com.example.joe.cityumobile.greenDaoGenerated.DaoSession;
import com.example.joe.cityumobile.greenDaoGenerated.ExpressDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;


public class LocalDataAccess {

    public static ApplyDA getApplyDA() {
        return applyDA;
    }

    public static ChatDA getChatDA() {
        return chatDA;
    }

    public static ConversationDA getConversationDA() {
        return conversationDA;
    }

    public static ExpressDA getExpressDA() {
        return expressDA;
    }

    /**
     * 单例内部类
     */
    private static class Holder{
        private final static LocalDataAccess instance = new LocalDataAccess();
    }

    /**
     * 获取单例
     * @return
     */
    public static LocalDataAccess getInstance(){
        return Holder.instance;
    }


    private static ApplyDA applyDA;
    private static ChatDA chatDA;
    private static ConversationDA conversationDA;
    private static ExpressDA expressDA;

    private LocalDataAccess(){

    }

    //初始化
    public void initial(Context context,String dbname){

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,dbname,null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();

        applyDA = new ApplyDA(daoSession.getApplyDao());
        chatDA = new ChatDA(daoSession.getChatObjDao());
        conversationDA = new ConversationDA(daoSession.getConversationDao());
        expressDA = new ExpressDA(daoSession.getExpressDao());
    }

    public class ApplyDA {

        private ApplyDao applyDao;

        private ApplyDA(ApplyDao applyDao){
            this.applyDao = applyDao;
        }

        public void addRecord(Apply apply, boolean srcOffline,SimpleListener listener){
            try{
//                if (applyDao.load(apply.getId()) != null){
//                    Log.e("My","重复数据");
//                    return;
//                }
                applyDao.insertOrReplace(apply);
                if (listener != null){
                    listener.done();
                }
            }catch (DaoException e){
                Log.e("My",e.toString());
            }
        }

        public void updateRecord(Apply apply){
            try{
                applyDao.update(apply);
            }catch (DaoException e){

            }
        }

        public void deleteRecord(Apply apply){
            try{
                applyDao.delete(apply);
            }catch (DaoException e){

            }
        }

        public List<Apply> loadAllRecords(SimpleListener listener){
            QueryBuilder<Apply> queryBuilder = applyDao.queryBuilder();
            queryBuilder.where(ApplyDao.Properties.State.eq(0));
            queryBuilder.orderDesc(ApplyDao.Properties.CreateTime);
            List<Apply> records = queryBuilder.list();
            if (records == null){
                records = new ArrayList<>();
            }
            if (listener !=null){
                listener.done();
            }
            return records;
        }

    }

    public class ConversationDA{

        private ConversationDao conversationDao;

        private ConversationDA(ConversationDao conversationDao){
            this.conversationDao = conversationDao;
        }

        //获取全部对话记录
        public List<Conversation> getAllConversation(){
            QueryBuilder<Conversation> queryBuilder = conversationDao.queryBuilder();
            queryBuilder.where(ConversationDao.Properties.Flag.eq(0));
            List<Conversation> result = queryBuilder.list();

            return result;
        }

        //查询是否存在和某人的对话
        public boolean isConversationExist(String targetUid){
            QueryBuilder<Conversation> queryBuilder = conversationDao.queryBuilder();
            queryBuilder.where(ConversationDao.Properties.TargetUid.eq(targetUid));
            Conversation result = queryBuilder.unique();
            if (result != null){
                return true;
            }else{
                return false;
            }
        }

        /**
         * 添加一个新的对话到数据库
         * @param newConversation
         * @param srcOffline 消息来源是否是离线消息
         * @return
         */
        public boolean addConversation(Conversation newConversation,boolean srcOffline){
            if (newConversation != null){
                try{
                    newConversation.setFlag(0);
                    conversationDao.insert(newConversation);
                    return true;
                }catch (Exception e){
                    Log.e("My","对话重复,拒绝添加");
                    if (!srcOffline){
                        Log.e("My","非离线消息，开启特权");
                        newConversation.setFlag(0);
                        conversationDao.update(newConversation);
                        return true;
                    }
                    return false;
                }
            }else{
                return false;
            }
        }

        /**
         * 从数据库中清理掉所有被标记删除了的对话
         */
        public void cleanAllMarkedConversation(){
            try{
                QueryBuilder<Conversation> queryBuilder = conversationDao.queryBuilder();
                queryBuilder.where(ConversationDao.Properties.Flag.eq(1));
                List<Conversation> preDeleteList = queryBuilder.list();
                if (preDeleteList == null){
                    return;
                }

                for (int i = 0;i<preDeleteList.size();i++){
                    conversationDao.delete(preDeleteList.get(i));
                }
                Log.e("My","冗余对话清理完成");
            }catch (Exception e){
                Log.e("My","冗余对话清理失败");
            }
        }


        /**
         * 标记一条对话进入待删除状态(已经不显示了)
         * @param conversation
         */
        public boolean markRemoveConversation(Conversation conversation){
            if (conversation != null){
                try{
                    QueryBuilder<Conversation> queryBuilder = conversationDao.queryBuilder();
                    queryBuilder.where(ConversationDao.Properties.TargetUid.eq(conversation.getTargetUid()));
                    Conversation updateConversation = queryBuilder.unique();
                    updateConversation.setFlag(1);

                    conversationDao.update(updateConversation);
                    Log.e("My","对话已标记删除");
                    return true;
                }catch (Exception e){
                    Log.e("My","对话标记删除失败");
                    return false;
                }
            }else{
                return false;
            }
        }


        //更新数据库里的未读消息数量
        public void updateUnreadCount(String targetUid,int updateValue){
            try{
                QueryBuilder<Conversation> queryBuilder = conversationDao.queryBuilder();
                queryBuilder.where(ConversationDao.Properties.TargetUid.eq(targetUid));
                Conversation updateConversation = queryBuilder.unique();
                updateConversation.setUnreadCount(updateValue);

                conversationDao.update(updateConversation);

            }catch (Exception e){

            }
        }

    }

    public class ChatDA{

        private ChatObjDao chatObjDao;

        public ChatDA(ChatObjDao chatObjDao){
            this.chatObjDao = chatObjDao;
        }




        //获取和指定ID最新的聊天信息（目前全部）
        public List<ChatObj> getLatestMsg(String currentUid, String targetUid){

            if (chatObjDao == null){
                return null;
            }

            QueryBuilder<ChatObj> queryBuilder = chatObjDao.queryBuilder();
            queryBuilder.where(
                    queryBuilder.or(
                            queryBuilder.and(
                                    ChatObjDao.Properties.SenderId.eq(currentUid),
                                    ChatObjDao.Properties.ReceiverId.eq(targetUid)
                            ),
                            queryBuilder.and(
                                    ChatObjDao.Properties.SenderId.eq(targetUid),
                                    ChatObjDao.Properties.ReceiverId.eq(currentUid)
                            )
                    )
            ).orderAsc(ChatObjDao.Properties.CreateTime);

            List<ChatObj> result = queryBuilder.list();

            return  result;
        }



        //添加一条新的聊天信息到数据库
        public boolean addChatObj(ChatObj chatObj){
            if (chatObj != null){
                try {
                    chatObjDao.insert(chatObj);
                    return true;
                }catch (Exception e){
                    Log.e("My","数据重复,拒绝添加");
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public class ExpressDA{

        private ExpressDao expressDao;

        public ExpressDA(ExpressDao expressDao){
            this.expressDao = expressDao;
        }

        public boolean addExpress(Express express){
            if (express != null){
                try{
                    expressDao.insertOrReplace(express);
                    return true;
                }catch (DaoException e){
                    return false;
                }
            }else{
                return false;
            }
        }

        public boolean deleteExpress(Express express){
            if (express != null){
                try{
                    expressDao.delete(express);
                    return true;
                }catch (DaoException e){
                    return false;
                }
            }else{
                return false;
            }
        }

        public List<Express> loadAllExpress(){
            QueryBuilder<Express> queryBuilder = expressDao.queryBuilder();
            queryBuilder.orderDesc(ExpressDao.Properties.CreateTime);
            return queryBuilder.list();
        }

    }

}