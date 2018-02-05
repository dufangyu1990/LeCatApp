package com.example.dufangyu.lecatapp.present;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.example.dufangyu.lecatapp.CallBack.NetCallBackImp;
import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.GenericHelper;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.ActivityControl;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.utils.Util;
import com.example.dufangyu.lecatapp.view.IView;

import static com.example.dufangyu.lecatapp.utils.Constant.MAIN_INSTANCE;
import static com.example.dufangyu.lecatapp.utils.Constant.REENTER;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPNONET;


/**
 * Created by dufangyu on 2017/6/13.
 */

public class FragmentActivityPresentImpl<T extends IView>extends FragmentActivity implements IPresent<T> {

    protected T mView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViewCreate(savedInstanceState);
        ActivityControl.addActivity(this);
        try {
            mView = getViewClass().newInstance();
            mView.bindPresenter(this);
            setContentView(mView.createView(getLayoutInflater(),null));
            mView.bindEvent();
//            LogUtil.d("dfy","activity name = "+this.getClass().getName());
            TcpConnectUtil.getTcpInstance().setNetCallBack(netCallBackImp);
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
    protected void onDestroy() {
        super.onDestroy();
        mView = null;
        ActivityControl.removeActivity(this);
        LogUtil.d("dfy", getClass().getSimpleName() + "  onDestroy");
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
                    MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
                    break;
                case TCPDISLINK:
//                    LogUtil.d("dfy", "与服务器连接断开");
                    doNetDisConnect();
                    break;
                case TCPLINK:
//                    LogUtil.d("dfy","与服务器连接成功");
                    doNetConnect();
                    break;

            }
        }
    };


    public void pressAgainExit(){

    }

    public void doNetDisConnect(){

    }

    public void doNetConnect(){
        int size = ActivityControl.activities.size();
        String topName = ActivityControl.getTopActicvityName(getApplicationContext());
//      LogUtil.d("dfy","topName = "+topName);
        String topActivityName = ActivityControl.activities.get(size-1).getClass().getName();

        if(!TextUtils.isEmpty(topName))
        {
            if(topName.equals(topActivityName) && topActivityName.equals(MAIN_INSTANCE))
            {
                Util.sendLocalBroadcast(getApplicationContext(),new Intent(REENTER));
            }
        }
    }

}
