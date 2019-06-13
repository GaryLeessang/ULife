package com.example.joe.cityumobile.Manager;

import android.widget.Filter;
import android.widget.Filterable;

import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.MyObservable;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.Core.Listener.PostRefreshListener;
import com.example.joe.cityumobile.DataAccess.LocalDataAccess;
import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.DataModel.DaoModel.Express;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PostManager implements MyObservable , Filterable {

    /**
     * 单例内部类
     */
    private static class Holder{
        private final static PostManager instance = new PostManager();
    }

    /**
     * 获取单例
     * @return
     */
    public static PostManager getInstance(){
        return Holder.instance;
    }

    /**
     * 原始帖子列表
     */
    private List<Post> expressPostList;

    /**
     * 过滤了之后的帖子列表
     */
    private List<Post> filteredPostList;

    /**
     * 自己发帖的历史记录
     */
    private List<Express> expressHistoryList;

    private PostManager(){
        expressPostList = new ArrayList<>();
        filteredPostList = new ArrayList<>();
        expressHistoryList = new ArrayList<>();
    }

    public List<Post> getFilteredPostList(){
        return filteredPostList;
    }

    /**
     * 获取当前帖子数据列表
     * @return
     */
    public List<Post> getExpressPostList(){
        return expressPostList;
    }

    /**
     * 向服务器请求刷新帖子
     */
    public void refreshPost(final PostRefreshListener listener){
        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereGreaterThan("ExpireDate", new BmobDate(new Date()));
        bmobQuery.include("Publisher");
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    if (list != null){
                        expressPostList = list;
                        filteredPostList = expressPostList;
                    }else{
                        expressPostList = new ArrayList<>();
                    }

                    if (listener != null){
                        listener.done();
                    }
                    onPostRefresh();
                }
            }
        });
    }

    /**
     * 当帖子刷新时触发
     */
    private void onPostRefresh(){
        notifyObservers(EventMessageType.POST_UPDATE);
    }

    /**
     * 从本地数据库加载帖子历史记录
     */
    public void loadPostHistoryFromDB(){
        expressHistoryList = LocalDataAccess.getExpressDA().loadAllExpress();
        if (expressHistoryList == null){
            expressHistoryList = new ArrayList<>();
        }
    }

    /**
     * 从云端同步帖子历史记录到本地
     */
    public void loadPostHistoryFromCloud(final PostRefreshListener listener){
        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("Publisher", BmobUser.getCurrentUser(User.class));
        bmobQuery.include("Publisher");
        bmobQuery.setLimit(50);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    for (int i=0;i<list.size();i++){
                        LocalDataAccess.getExpressDA().addExpress(Post.Convert(list.get(i)));
                    }
                }else{

                }
                loadPostHistoryFromDB();
                notifyObservers(EventMessageType.HISTORY_POST_UPDATE);
                if (listener != null){
                    listener.done();
                }
            }
        });
    }

    /**
     * 获取帖子历史记录
     * @return
     */
    public List<Express> getExpressHistoryList() {
        return expressHistoryList;
    }

    /**
     * 根据ID找帖子
     * @param bmobExpressId
     * @return
     */
    public Express findExpressById(String bmobExpressId){
        for (Express express:expressHistoryList) {
            if (express.getBmobExpressId().equals(bmobExpressId)){
                return express;
            }
        }
        return null;
    }

    /**
     * 设置帖子过滤器
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (!charString.isEmpty()){
                    List<Post> posts = new ArrayList<>();
                    for (Post post:expressPostList){
                        if (post.getContent().toLowerCase().contains(charString.toLowerCase())
                                || post.getPublisher().getNickName().toLowerCase().contains(charString.toLowerCase())){
                            posts.add(post);
                        }
                    }
                    filteredPostList = posts;
                }else{
                    filteredPostList = expressPostList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPostList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredPostList = (List<Post>) results.values;
                notifyObservers(EventMessageType.POST_UPDATE);
            }
        };
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
        if (observers.contains(observer)){
            observers.remove(observer);
        }
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
