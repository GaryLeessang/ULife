package com.example.joe.cityumobile.Manager;

import com.example.joe.cityumobile.Core.EventMessageType;
import com.example.joe.cityumobile.Core.Listener.SimpleListener;
import com.example.joe.cityumobile.Core.MyObservable;
import com.example.joe.cityumobile.Core.MyObserver;
import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.DataModel.BmobModel.ServiceOrder;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class OrderManager implements MyObservable {

    /**
     * 单例内部类
     */
    private static class Holder{
        private final static OrderManager instance = new OrderManager();
    }

    /**
     * 获取单例
     * @return
     */
    public static OrderManager getInstance(){
        return Holder.instance;
    }

    public List<ServiceOrder> orderList;

    private OrderManager(){
        orderList = new ArrayList<>();
    }

    public void createOrder(User serviceProvider, User serviceReceiver, Post refPost){
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setPost(refPost);
        serviceOrder.setServiceProvider(serviceProvider);
        serviceOrder.setServiceReceiver(serviceReceiver);
        serviceOrder.setState("Progressing");

        serviceOrder.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    Utils.note("订单创建成功");
                }else{
                    Utils.note("订单创建失败");
                }
            }
        });
    }

    public void updateOrder(ServiceOrder order, final SimpleListener listener){
        if (order == null){
            return;
        }
        order.setState("Finished");
        order.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (listener != null){
                        listener.done();
                    }
                }else{

                }
            }
        });
    }

    public void loadOrdersFromCloud(final SimpleListener listener){
        BmobQuery<ServiceOrder> query = new BmobQuery<>();

        BmobQuery<ServiceOrder> subQuery_1 = new BmobQuery<>();
        subQuery_1.addWhereEqualTo("serviceProvider",BmobUser.getCurrentUser(User.class));

        BmobQuery<ServiceOrder> subQuery_2 = new BmobQuery<>();
        subQuery_2.addWhereEqualTo("serviceReceiver",BmobUser.getCurrentUser(User.class));

        List<BmobQuery<ServiceOrder>> queries = new ArrayList<>();
        queries.add(subQuery_1);
        queries.add(subQuery_2);

        query.or(queries);
        query.addWhereEqualTo("state","Progressing");
        query.include("Post,serviceProvider,serviceReceiver");
        query.order("-createdAt");
        query.findObjects(new FindListener<ServiceOrder>() {
            @Override
            public void done(List<ServiceOrder> list, BmobException e) {
                orderList = list;
                if (orderList == null){
                    orderList = new ArrayList<>();
                }

                if (listener != null){
                    listener.done();
                }
            }
        });

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
            observer.update(EventMessageType.ORDER_CHANGE);
        }
    }
}
