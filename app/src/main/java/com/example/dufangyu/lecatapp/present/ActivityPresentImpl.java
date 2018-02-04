package com.example.dufangyu.lecatapp.present;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.example.dufangyu.lecatapp.CallBack.NetCallBackImp;
import com.example.dufangyu.lecatapp.helper.GenericHelper;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.ActivityControl;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.Util;
import com.example.dufangyu.lecatapp.view.IView;

import static com.example.dufangyu.lecatapp.utils.Constant.LOGIN_INSTANCE;
import static com.example.dufangyu.lecatapp.utils.Constant.REENTER;
import static com.example.dufangyu.lecatapp.utils.Constant.SPLASH_INSTANCE;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPNONET;


/**
 * Created by dufangyu on 2017/6/13.
 */

public  class ActivityPresentImpl<T extends IView>extends Activity implements IPresent<T> {

    protected T mView;


    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beforeViewCreate(savedInstanceState);
        ActivityControl.addActivity(this);
        try {
            mView = getViewClass().newInstance();
            setContentView(mView.createView(getLayoutInflater(),null));
            mView.bindPresenter(this);
            mView.bindEvent();
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

//            LogUtil.d("dfy","activity name = "+this.getClass().getName());
            TcpConnectUtil.getTcpInstance().setNetCallBack(netCallBackImp);
            TcpConnectUtil.getTcpInstance().startThreads();
            afterViewCreate(savedInstanceState);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }

    @Override
    public void beforeViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void presentCallBack(String param1, String param2, String params3) {

    }



    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mView = null;
        ActivityControl.removeActivity(this);
        LogUtil.d("dfy", getClass().getSimpleName() + "  onDestroy");





    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            pressAgainExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private NetCallBackImp netCallBackImp =new NetCallBackImp() {
        @Override
        public void runOnUI(int stateCode) {
            switch (stateCode)
            {
                case TCPNONET:
                    LogUtil.d("dfy", "无网络");
                    doNoNetWork();
                    break;
                case TCPDISLINK:
                    LogUtil.d("dfy", "与服务器连接断开");
                    doNetDisConnect();
                    break;
                case TCPLINK:
                    LogUtil.d("dfy","与服务器连接成功");
                    doNetConnect();
                    break;

            }



        }
    };


    public void pressAgainExit(){
        finish();
    }

    //app连接不上服务器(断网或者服务器出问题)
    public void doNetDisConnect(){

    }

    //app连接服务器成功
    public void doNetConnect(){

        int size = ActivityControl.activities.size();
        String topName = ActivityControl.getTopActicvityName(getApplicationContext());
//        LogUtil.d("dfy","topName = "+topName);
        String topActivityName = ActivityControl.activities.get(size-1).getClass().getName();
//        LogUtil.d("dfy","topActivityName = "+topActivityName);
        if(!TextUtils.isEmpty(topName))
        {
            if(topName.equals(topActivityName) && !topActivityName.equals(LOGIN_INSTANCE) && !topActivityName.equals(SPLASH_INSTANCE))
            {
                LogUtil.d("dfy","发送重新登录广播");
                Util.sendLocalBroadcast(getApplicationContext(),new Intent(REENTER));
            }
        }


    }

    //app没网
    public void doNoNetWork(){

    }

}
