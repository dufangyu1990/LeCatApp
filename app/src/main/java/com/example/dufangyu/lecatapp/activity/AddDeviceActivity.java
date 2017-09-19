package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.AddDeviceBiz;
import com.example.dufangyu.lecatapp.biz.AddDeviceListener;
import com.example.dufangyu.lecatapp.biz.IAddDevice;
import com.example.dufangyu.lecatapp.customview.CustomLoadDialog;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.AddDeviceView;

import static com.example.dufangyu.lecatapp.utils.Constant.ADD_NEWDEVICE;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceActivity extends ActivityPresentImpl<AddDeviceView> implements View.OnClickListener,AddDeviceListener{
    private LocalBroadcastManager mLocalBroadcastManager;
    private IAddDevice addDeviceBiz;
    private String deviceId;
    private  boolean addsuccess;


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        addDeviceBiz = new AddDeviceBiz(this);
        deviceId = getIntent().getStringExtra("deviceId");
        mView.setDeviceIdValue(deviceId);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_device:
                addDevice();
                break;
        }
    }


    private void addDevice()
    {
        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }

        CustomLoadDialog.show(AddDeviceActivity.this,"",true,null,R.layout.logindialog);
        addDeviceBiz.addDevice(MyApplication.getInstance().getStringPerference("UserName"),deviceId,mView.getValueById(R.id.nickname_ed));

    }

    public  static void actionStart(Context context,String deviceId)
    {
        Intent intent = new Intent(context,AddDeviceActivity.class);
        intent.putExtra("deviceId",deviceId);
        context.startActivity(intent);
    }

    private void sendMyBroacast()
    {
        Intent intent =new Intent(ADD_NEWDEVICE);
        mLocalBroadcastManager.sendBroadcast(intent);
    }




    @Override
    public void AddDeviceSuccess() {
        //添加成功发送获取列表指令
        addDeviceBiz.getMyDeviceAgain(MyApplication.getInstance().getStringPerference("UserName"));
    }

    @Override
    public void AddDeviceFail() {
        CustomLoadDialog.dismisDialog();
        MyToast.showTextToast(getApplicationContext(),"此设备已存在");

    }

    /**
     * 获取到列表了
     */
    @Override
    public void getDeviceOver() {
        addsuccess = true;
        CustomLoadDialog.dismisDialog();
        MyToast.showTextToast(getApplicationContext(),"设备添加成功");
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(addsuccess)
        {
            sendMyBroacast();
        }
        addDeviceBiz.detachDataCallBackNull();
        addDeviceBiz = null;
    }

}
