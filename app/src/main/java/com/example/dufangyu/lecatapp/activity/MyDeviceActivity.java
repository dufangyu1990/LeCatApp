package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.IMyDevice;
import com.example.dufangyu.lecatapp.biz.MyDeviceBiz;
import com.example.dufangyu.lecatapp.biz.MyDeviceListener;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.MyDeviceView;

/**
 * Created by dufangyu on 2017/9/14.
 */

public class MyDeviceActivity extends ActivityPresentImpl<MyDeviceView> implements View.OnClickListener,MyDeviceListener{


    private IMyDevice myDeviceBiz;
    private String loginName;

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        loginName = getIntent().getStringExtra("loginName");
        myDeviceBiz = new MyDeviceBiz(this);
        mView.startRefrsh(true);
        getDataFromServer();

    }


    private void getDataFromServer()
    {

        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }
        myDeviceBiz.getMyDevice(loginName);
    }



    public static void actionStart(Context context,String loginName)
    {
        Intent intent= new Intent(context, MyDeviceActivity.class);
        intent.putExtra("loginName",loginName);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;
        }
    }


    @Override
    public void getDevices() {
        mView.setDeviceList();
    }

    @Override
    public void deleteSuccess() {
        mView.notifyAdapterRefresh();
        MyToast.showTextToast(MyDeviceActivity.this,"删除设备成功");

    }

    @Override
    public void deleteFail() {
        MyToast.showTextToast(MyDeviceActivity.this,"用户无此设备");
    }


    @Override
    public void presentCallBack(String param1, String param2, String params3) {
        super.presentCallBack(param1, param2, params3);
        String deviceId = param1;
        myDeviceBiz.deleteDevice(loginName,deviceId);
    }
}
