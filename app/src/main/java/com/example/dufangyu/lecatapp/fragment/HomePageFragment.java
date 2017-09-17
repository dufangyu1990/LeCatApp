package com.example.dufangyu.lecatapp.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.biz.HomePageBiz;
import com.example.dufangyu.lecatapp.biz.HomePageListener;
import com.example.dufangyu.lecatapp.biz.IHomePage;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.present.FragmentPresentImpl;
import com.example.dufangyu.lecatapp.utils.BroadCastControll;
import com.example.dufangyu.lecatapp.utils.Constant;
import com.example.dufangyu.lecatapp.view.HomePageView;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageFragment extends FragmentPresentImpl<HomePageView> implements HomePageListener,View.OnClickListener {


    private IHomePage mainBiz;

    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;
    private Context context;
    private static  HomePageFragment thisInstance;
    private boolean isNetConnnect =true  ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBiz = new HomePageBiz(this);
        thisInstance = this;
        int size = DataManager.p_intDeviceCount;
//        LogUtil.d("dfy","szie = "+size);
        if(size>0)//有设备的情况下再去获取数据
        {
            mainBiz.get4GPushData();
        }
        context = getActivity();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        registMyRecivier();
        mLocalBroadcastManager.registerReceiver(mReceiver,filter);
        BroadCastControll.addReceiver(mReceiver);


    }


    @Override
    public void refreshUI(RealData realData) {
       mView.refreshView(realData);
    }

    private void registMyRecivier()
    {
        filter = new IntentFilter(Constant.ADD_NEWDEVICE);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(Constant.ADD_NEWDEVICE)||action.equals(Constant.DELETE_DEVICE)) {
                    int size = DataManager.p_intDeviceCount;
                    mainBiz = new HomePageBiz(thisInstance);
                    if(size>0)//有设备的情况下再去获取数据
                    {
                        mainBiz.get4GPushData();
                    }else{
                       mView.reFreshNoData();
                    }
                }

            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.menkongimg:
//                int tempvalue  = Util.getRandomValue(20,30);
//                int tempvalue2  = Util.getRandomValue(20,80);
//                RealData realData = new RealData();
//                realData.setTemperatureValue(tempvalue+"");
//                realData.setHumidityValueValue(tempvalue2+"");
//                mView.refreshView(realData);
                break;
        }
    }

    @Override
    public void doNetConnect() {
        super.doNetConnect();
        if(!isNetConnnect)
        {
            int size = DataManager.p_intDeviceCount;
            if(size>0)//有设备的情况下再去获取数据
            {
                mainBiz.get4GPushData();
            }
        }
    }

    @Override
    public void doNetDisConnect() {
        super.doNetDisConnect();
        isNetConnnect = false;
    }
}
