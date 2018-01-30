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
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.biz.HomePageBiz;
import com.example.dufangyu.lecatapp.biz.HomePageListener;
import com.example.dufangyu.lecatapp.biz.IHomePage;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.present.FragmentPresentImpl;
import com.example.dufangyu.lecatapp.utils.BroadCastControll;
import com.example.dufangyu.lecatapp.utils.Constant;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.HomePageView;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageFragment extends FragmentPresentImpl<HomePageView> implements HomePageListener,View.OnClickListener {


    private IHomePage mainBiz = null;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBiz = new HomePageBiz(this);
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

    @Override
    public void reEnterSuccess(String code, String author,String userName,String phoneCall,String address) {

        mainBiz.getDeviceList(MyApplication.getInstance().getStringPerference("UserName"));
    }

    @Override
    public void reEnterFail() {
        MyToast.showTextToast(context.getApplicationContext(),"登录失败");
    }

    @Override
    public void getDeviceList() {
        int size = DataManager.p_intDeviceCount;
        if(size>0)//有设备的情况下再去获取数据
        {
            mainBiz.get4GPushData();
        }
    }

    @Override
    public void send_lightControlSuccess() {
        MyToast.showTextToast(context.getApplicationContext(),"指令发送成功");
    }

    private void registMyRecivier()
    {
        filter = new IntentFilter(Constant.ADD_NEWDEVICE);
        filter.addAction(Constant.DELETE_DEVICE);
        filter.addAction(Constant.REENTER);
        filter.addAction(Constant.REFRESH);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(Constant.ADD_NEWDEVICE)||action.equals(Constant.DELETE_DEVICE)||action.equals(Constant.REFRESH)) {


                    LogUtil.d("dfy","收到广播"+action+",刷新数据");
                    int size = DataManager.p_intDeviceCount;
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

                break;
            case R.id.red_light:
                mainBiz.sendLightCommand("1");
                break;
            case R.id.green_light:
                mainBiz.sendLightCommand("2");
                break;
            case R.id.blue_light:
                mainBiz.sendLightCommand("3");
                break;
            case R.id.close_light:
                mainBiz.sendLightCommand("0");
                break;
        }
    }


}
