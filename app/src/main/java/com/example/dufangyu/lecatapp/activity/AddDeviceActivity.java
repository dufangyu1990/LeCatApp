package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceActivity extends ActivityPresentImpl<AddDeviceView> implements View.OnClickListener{

    private IAddDevice addDeviceBiz;
    private String deviceId;

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        addDeviceBiz = new AddDeviceBiz();
        deviceId = getIntent().getStringExtra("deviceId");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;
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

        if(mView.checkValid())
        {


            CustomLoadDialog.show(AddDeviceActivity.this,"",true,null,R.layout.logindialog);
            addDeviceBiz.addDevice(MyApplication.getInstance().getStringPerference("UserName"),deviceId,mView.getValueById(R.id.nickname_ed), new AddDeviceListener() {
                @Override
                public void AddDeviceSuccess() {
                    CustomLoadDialog.dismisDialog();
                    MyToast.showTextToast(AddDeviceActivity.this,"设备添加成功");
                    finish();
                }

                @Override
                public void AddDeviceFail() {
                    CustomLoadDialog.dismisDialog();
                    MyToast.showTextToast(AddDeviceActivity.this,"此设备已存在");
                }

            });
        }

    }

    public  static void actionStart(Context context,String deviceId)
    {
        Intent intent = new Intent(context,AddDeviceActivity.class);
        intent.putExtra("deviceId",deviceId);
        context.startActivity(intent);
    }

}
