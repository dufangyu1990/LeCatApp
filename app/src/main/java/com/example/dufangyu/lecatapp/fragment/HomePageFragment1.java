package com.example.dufangyu.lecatapp.fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.LightControlActivity;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.activity.PlayActivity;
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
import com.example.dufangyu.lecatapp.utils.SharePrefUtil;
import com.example.dufangyu.lecatapp.view.HomePageView1;
import com.example.jjhome.network.Constants;
import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.TestEvent;
import com.example.jjhome.network.ddpush.YeePushService;
import com.example.jjhome.network.ddpush.YeePushUtils;
import com.example.jjhome.network.entity.DeviceListBean;
import com.example.jjhome.network.entity.DeviceListItemBean;
import com.google.gson.Gson;
import com.jjhome.master.http.OnConnListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

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


    private String userId,strPwd,pushServerIp;
    private String titleName;
    private DeviceListBean mListBean = new DeviceListBean();
    private ArrayList<String> mPushIpList = new ArrayList<>();

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            userId= bundle.getString("userId");
            strPwd = bundle.getString("userPwd");
            pushServerIp = bundle.getString("userPushIp");
        }

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        registMyRecivier();
        mLocalBroadcastManager.registerReceiver(mReceiver,filter);
        BroadCastControll.addReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        LogUtil.d("dfy","onResume");
        super.onResume();
        mainBiz = null;
        mainBiz = new HomePageBiz(this);
        int size = DataManager.p_intDeviceCount;
        if(size>0)//有设备的情况下再去获取数据
        {
//            mainBiz.get4GPushData();
            //发送巡检指令
            mainBiz.check4GData();
            isReceviceData = false;
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
        LogUtil.d("dfy","HomeFragment onPause");
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
            isReceviceData = false;
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

                        mainBiz.check4GData();
                        isReceviceData = false;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.removeCallbacks(this);
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
            case R.id.doorlightTv:
                mView.changeImg();
                break;
            case R.id.lightcontrolTv:
                LightControlActivity.actionStart(context,lightStateValue);
                break;
            case R.id.indoor_videoTv:
                titleName = "室内监控";
                videoAction();
                break;
            case R.id.outdoor_videoTv:
//                Intent intent = DeviceListActivity.getIntent(userId, strPwd, pushServerIp, context);
//                startActivity(intent);
                titleName = "室外监控";
                videoAction();
                break;
            case R.id.jiantingTv:
//                LogUtil.d("dfy","phoneStr = "+MyApplication.getInstance().getStringPerference("UserName"));
                mainBiz.sendJTOrder(MyApplication.getInstance().getStringPerference("UserName"));
                break;

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }



    private void videoAction()
    {
        getData();
        Intent startSrv = new Intent(context.getApplicationContext(), YeePushService.class);
        startSrv.putExtra("pushIp", pushServerIp);
        context.getApplicationContext().startService(startSrv);
    }

    /**
     * 从服务器请求设备列表
     */
    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(context.getApplicationContext());
        progressDialog.setMessage("加载中...");
        //TODO 从服务器获取设备列表
        Log.d("success--->>>", "getData");
        MyApplication.getMasterRequest().msDeviceList(userId, strPwd, SharePrefUtil.getString("userToken", ""), new OnConnListener() {
            @Override
            public void onStart() {
//                progressDialog.show();
                Log.d("success--->>>", "onStart()");
            }

            @Override
            public void onSuccess(String s) {
                Log.d("success--->>>", s);
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                try {
                    Gson gson = new Gson();
                    mListBean = gson.fromJson(s, mListBean.getClass());
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (mListBean == null) {
                    onFailure(1, "没有数据");
                    return;
                }
                if (mListBean.errcode != 0) {
                    onFailure(mListBean.errcode, mListBean.errinfo);
                    return;
                }
                List<DeviceListItemBean> device_list = mListBean.device_list;
                mPushIpList.clear();
                if (null != device_list && device_list.size() > 0) {
                    DeviceUtils.deleteAllDevice();
                    for (int i = 0; i < device_list.size(); i++) {
                        DeviceListItemBean deviceListItemBean = device_list.get(i);
                        DeviceUtils.connectDevice(deviceListItemBean.getDevice_id(), deviceListItemBean.getDevice_name(), deviceListItemBean.getDevice_pass(), "0", deviceListItemBean.getShare(), deviceListItemBean.getShared_by(), deviceListItemBean.p2pserver_ip + "-" + deviceListItemBean.p2pserver_port);
                        if (!mPushIpList.contains(deviceListItemBean.getPushserver_ip())) {
                            mPushIpList.add(deviceListItemBean.getPushserver_ip());
                        }
                    }
//                    mListAdapter.addData(mListBean.device_list);
                }

                if (mPushIpList != null && mPushIpList.size() > 0) {
                    YeePushUtils.resetServiceClient(context.getApplicationContext(), mPushIpList);
                    YeePushUtils.sendDeviceList(pushServerIp, context, device_list, userId);//将设备信息绑定到推送服务器
                }

                if(device_list.size() ==  1)
                {
                   Intent intent = PlayActivity.getIntent(context, device_list.get(0),titleName);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(int i, String s) {

                MyToast.showTextToast(context.getApplicationContext(),"参数有误，无法进行实时监控");
                Log.d("success--->>>", "onFailure()" + s);
                if (!TextUtils.isEmpty(s)) {
                    MyToast.showTextToast(context.getApplicationContext(), s);
                }
            }

            @Override
            public void onFinish() {
//                progressDialog.dismiss();
            }
        });
    }


    public void onEventMainThread(TestEvent event) {
        LogUtil.d("HomeonEvent", "DeviceListActivity onEventMainThread:" + event.get_string());
        //获取设备的状态
        if (event.get_string().equals(Constants.ACTION_UPDATE_STATE)) {
            Bundle bundle = event.get_bundle();
            //0:不在线；1：在线 2:密码错误
            LogUtil.d("HomeonEvent", "onEventMainThread bundle.getInt(\"clientState\", 0)" + bundle.getInt("clientState", 0));
            //device_id
//            Log.i("VKState", "DeviceListActivity--> clientState -->> " + bundle.getInt("clientState", 0) + " clientStrId " + bundle.getString("clientStrId"));
            LogUtil.d("HomeonEvent", "onEventMainThread bundle.getString(\"clientStrId\")" + bundle.getString("clientStrId"));
        }

        //在线人数和最大在线人数
        if (event.get_string().equals(Constants.ACTION_SEND_ONLINE_NUMS)) {
            Bundle bundle = event.get_bundle();
            String deviceId = bundle.getString("device_id");
            int onlineNums = bundle.getInt("onlineNums");
            int maxNums = bundle.getInt("maxNums");
            LogUtil.d("HomeonEvent", "device_id" + deviceId + " onlineNums:" + onlineNums + " maxNums:" + maxNums);
        }

        //修改设备密码结果
        if (event.get_string().equals(Constants.MSG_REPSW_RESPONSE)) {
            Bundle bundle = event.get_bundle();
            String result = bundle.getString("result");
            //result为0时设置成功，在这里调用主服务器的修改设备密码接口
            //MyApplication.getMasterRequest().msModifyDevice();
            LogUtil.d("HomeonEvent", "result:" + result);
        }

        if (event.get_string().equals("testyeepushservice")) {
            Bundle bundle = event.get_bundle();
            String cmd = bundle.getString("cmd");
            MyToast.showTextToast(context.getApplicationContext(), cmd);
            LogUtil.d("HomeonEvent", "cmd:" + cmd);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DeviceUtils.deleteAllDevice();
        YeePushUtils.sendLogoutTcp(SharePrefUtil.getString("userPushIp", ""),
                context, mPushIpList, SharePrefUtil.getString("userId", ""));
    }
}
