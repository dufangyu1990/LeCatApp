package com.jjhome.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jjhome.network.Constants;
import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.TestEvent;
import com.jjhome.demo.R;

import de.greenrobot.event.EventBus;

public class AddDeviceStep1  extends Activity{
    private TextView tv_device_id;
    private static final int MSG_NEARBY_DEVICE = 1;
    String deviceId;
    String deviceIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step1);
        EventBus.getDefault().register(this);
        findViewById(R.id.btn_search_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.在热点模式下搜索设备，得到设备的deviceId
                DeviceUtils.searchDevice();
            }
        });

        tv_device_id = (TextView) findViewById(R.id.tv_device_id);
        tv_device_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDeviceStep1.this, AddDeviceStep2.class);
                intent.putExtra("deviceId", tv_device_id.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEventMainThread(TestEvent event) {
        Log.d("onEvent", "AddDeviceStep1 event:" + event.get_string());
        //获取附近的设备,一共返回五次数据。
        if (event.get_string().equals(Constants.ACTION_SEARCH_NEARBY_DEVICE)) {
            Bundle bundle = event.get_bundle();
            deviceId = bundle.getString("deviceId");
            deviceIp = bundle.getString("deviceIp");
            handler.sendEmptyMessage(MSG_NEARBY_DEVICE);
            Log.d("onEvent", "deviceId:" + deviceId + " deviceIp:" + deviceIp);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("mHandler", "what" + msg.what);
            switch (msg.what) {
                case MSG_NEARBY_DEVICE:
                    tv_device_id.setText(deviceId);
                    tv_device_id.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
