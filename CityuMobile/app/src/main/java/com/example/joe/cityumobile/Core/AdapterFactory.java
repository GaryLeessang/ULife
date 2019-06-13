package com.example.joe.cityumobile.Core;

import android.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Adapter工厂
 */
public class AdapterFactory {

    private static class Holder{
        private final static AdapterFactory instance = new AdapterFactory();
    }

    public static AdapterFactory getInstance(){
        return Holder.instance;
    }

    private static Map<Class<? extends ISingletonAdapter>, ISingletonAdapter> adapters = Collections.synchronizedMap( new HashMap());

    /**
     * 获取指定类型的Adapter
     * @param adapterClass
     * @return
     */
    public static ISingletonAdapter getAdapter(Class<? extends ISingletonAdapter> adapterClass){
        if (adapters.containsKey(adapterClass)){
            return adapters.get(adapterClass);
        }else{
            try{
                ISingletonAdapter adapter = adapterClass.newInstance();
                adapters.put(adapterClass,adapter);
                return adapter;
            }catch (Exception e){
                Log.e("My","Create adapter failed");
            }
            return null;
        }
    }
}
