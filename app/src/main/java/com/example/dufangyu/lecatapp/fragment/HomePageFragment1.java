package com.example.dufangyu.lecatapp.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.LightControlActivity;
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
import com.example.dufangyu.lecatapp.view.HomePageView1;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageFragment1 extends FragmentPresentImpl<HomePageView1> implements HomePageListener,View.OnClickListener {


    private IHomePage mainBiz = null;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;
    private Context context;
    private Handler mHandler = new Handler();
    private boolean isReceviceData = false;//是否收到巡检指令的回应数据

    private String lightStateValue;

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        registMyRecivier();
        mLocalBroadcastManager.registerReceiver(mReceiver,filter);
        BroadCastControll.addReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("dfy","HomeFragment onResume");
        mainBiz = null;
        mainBiz = new HomePageBiz(this);
        int size = DataManager.p_intDeviceCount;
        if(size>0)//有设备的情况下再去获取数据
        {
//            mainBiz.get4GPushData();
            //发送巡检指令
            mainBiz.check4GData();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if(!isReceviceData)
                        MyToast.showTextToast(context.getApplicationContext(),"当前设备不在线");
                }
            },6*1000);
        }


    }


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }



    @Override
    public void refreshUI(RealData realData) {
//       mView.refreshView(realData);
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
//            mainBiz.get4GPushData();
            mainBiz.check4GData();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    LogUtil.d("dfy","enter here");
                    if(!isReceviceData)
                        MyToast.showTextToast(context.getApplicationContext(),"当前设备不在线");
                }
            },6*1000);
        }
    }

    /**
     *
     * @param param1  灯
     * @param param2  锁
     * @param param3  门
     * @param param4  电
     */
    @Override
    public void getCheck4GData(String param1, String param2, String param3, String param4) {
        isReceviceData = true;
        lightStateValue = param1;
        mView.refreshHomeUI(param2,param3,param4);

    }


    private void registMyRecivier()
    {
        filter = new IntentFilter(Constant.ADD_NEWDEVICE);
        filter.addAction(Constant.DELETE_DEVICE);
        filter.addAction(Constant.REENTER);
        filter.addAction(Constant.REFRESH);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(Constant.ADD_NEWDEVICE)||action.equals(Constant.DELETE_DEVICE)||action.equals(Constant.REFRESH)) {

                    LogUtil.d("dfy","收到广播"+action+",刷新数据");
                    int size = DataManager.p_intDeviceCount;
                    if(size>0)//有设备的情况下再去获取数据
                    {
//                        mainBiz.get4GPushData();
                        mainBiz.check4GData();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.removeCallbacks(this);
                                LogUtil.d("dfy","enter here");
                                if(!isReceviceData)
                                    MyToast.showTextToast(context.getApplicationContext(),"当前设备不在线");
                            }
                        },6*1000);
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
            case R.id.lightcontrolTv:
                int size = DataManager.p_intDeviceCount;
                if(size>0)//有设备的情况下再去获取数据
                {
                    LightControlActivity.actionStart(context,lightStateValue);
                }else{
                    MyToast.showTextToast(context.getApplicationContext(),"当前无在线设备");
                }

                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
