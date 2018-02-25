package com.example.dufangyu.lecatapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dufangyu.lecatapp.R;
import com.example.jjhome.network.entity.DeviceListItemBean;


public class DeviceOptionsActivity extends Activity implements View.OnClickListener {
    DeviceListItemBean mDevice;

    public static Intent getIntent(Context context, DeviceListItemBean deviceBean) {
        Intent intent = new Intent(context, DeviceOptionsActivity.class);
        intent.putExtra("device", deviceBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_options);
        mDevice = (DeviceListItemBean) getIntent().getSerializableExtra("device");
        initView();
    }

    private void initView() {
        Button btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        Button btn_replay = (Button) findViewById(R.id.btn_replay);
        btn_replay.setOnClickListener(this);
        Button btn_device_setting = (Button) findViewById(R.id.btn_device_setting);
        btn_device_setting.setOnClickListener(this);
        Button btn_device_share = (Button) findViewById(R.id.btn_device_share);
        btn_device_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.btn_play:
                intent = PlayActivity.getIntent(DeviceOptionsActivity.this, mDevice);
                startActivity(intent);
                break;
            case R.id.btn_replay:
//                intent = ReplayActivity.getIntent(DeviceOptionsActivity.this, mDevice);
//                startActivity(intent);
                break;
            case R.id.btn_device_setting:
                intent = new Intent(this, DeviceSettingActivity.class);
                intent.putExtra("deviceId", mDevice.getDevice_id());
                startActivity(intent);
                break;
            case R.id.btn_device_share:
//                intent = new Intent(this, ShareActivity.class);
//                intent.putExtra("deviceId", mDevice.getDevice_id());
//                startActivity(intent);
                break;
        }
    }
}
