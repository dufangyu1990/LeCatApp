package com.example.dufangyu.lecatapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dufangyu on 2017/7/3.
 */

public class BroadCastControll {

    public static LocalBroadcastManager mLocalBroadcastManager;
    //防止跑出java.util.ConcurrentModificationException，所以用CopyOnWriteArrayList
    public static List<BroadcastReceiver> list = new CopyOnWriteArrayList<>();

    public static void addReceiver(BroadcastReceiver receiver)
    {
        list.add(receiver);
    }


    public static void removeAllReciver(Context context)
    {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        for(BroadcastReceiver receiver:list)
        {
            LogUtil.d("dfy","销毁广播");
            list.remove(receiver);
            mLocalBroadcastManager.unregisterReceiver(receiver);
        }
    }

}
