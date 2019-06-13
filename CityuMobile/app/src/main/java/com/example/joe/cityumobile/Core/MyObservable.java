package com.example.joe.cityumobile.Core;

import java.util.ArrayList;
import java.util.List;

public interface MyObservable {

    List<MyObserver> observers = new ArrayList<>();

    /**
     * 添加一个观察者
     * @param observer
     */
    void addObserver(MyObserver observer);

    /**
     * 移除指定观察者
     * @param observer
     */
    void removeObserver(MyObserver observer);

    /**
     * 通知观察者
     */
    void notifyObservers(Integer... eventMessageCode);

}
