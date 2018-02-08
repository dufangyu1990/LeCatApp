package com.jjhome.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jjhome.network.Constants;
import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.TestEvent;
import com.example.jjhome.network.entity.ISettingListener;
import com.jjhome.demo.R;
import com.jjhome.demo.YeeApplication;
import com.jjhome.demo.utils.NetworkUtil;
import com.jjhome.demo.utils.SharePrefUtil;
import com.jjhome.demo.utils.WifiAdmin;
import com.jjhome.master.http.OnConnListener;

import java.util.List;

import de.greenrobot.event.EventBus;


public class AddDeviceStep2 extends Activity {
    private EditText edit_device_name, edit_wifi_ssid, edit_wifi_psw;
    private boolean isConnectDevice = false;
    private String deviceId;
    private static final int MSG_SEND_WIFI_SUCCESS = 0;
    private static final int MSG_SEND_TO_SERVER = 1;
    private ProgressDialog progressDialog;
    private WifiManager mWifiManager;
    private WifiAdmin mWifiAdmin;
    private List<ScanResult> scanList;// wifi列表
    private String mSsid;
    private int mCapabilities;
    private String mDeviceWifiPsw;
    private boolean isReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_add_step2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");

        deviceId = getIntent().getStringExtra("deviceId");

        DeviceUtils.deleteSingleDevice(deviceId);
        DeviceUtils.connectDevice(deviceId, "", "admin", "0", false, "", "");

        initView();

    }

    private void initView() {
        edit_device_name = (EditText) findViewById(R.id.edit_device_name);
        edit_wifi_ssid = (EditText) findViewById(R.id.edit_wifi_ssid);
        edit_wifi_psw = (EditText) findViewById(R.id.edit_wifi_psw);

        Button commit = (Button) findViewById(R.id.btn_commit_wifi);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitWifi();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 发送wifi信息到设备
     */
    private void commitWifi() {
        String deviceName = edit_device_name.getText().toString();
        String deviceWifiName = edit_wifi_ssid.getText().toString();
        mDeviceWifiPsw = edit_wifi_psw.getText().toString();
        if (TextUtils.isEmpty(deviceName) || TextUtils.isEmpty(deviceWifiName) || TextUtils.isEmpty(mDeviceWifiPsw)) {
            Toast.makeText(this, "请输入所有信息", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isConnectDevice) {
            Toast.makeText(this, "设备未连接", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < 3; i++) {
            DeviceUtils.setDeviceWifi(deviceId, deviceWifiName, mDeviceWifiPsw, settingListener);//备注：修改设备wifi也可以调用此方法。
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(this, " setDeviceWifi ", Toast.LENGTH_LONG).show();
//        handler.sendEmptyMessageDelayed(MSG_SEND_TO_SERVER, 5000);
        progressDialog.show();
    }

    /**
     * 添加设备到服务器
     */
    private void sendDeviceToMaster() {
        Log.i("VKHttp", "AddDeviceStep2--> 请求服务器-->> ");
        String deviceName = edit_device_name.getText().toString();
        YeeApplication.getMasterRequest().msAddDevice(SharePrefUtil.getString("userId", ""), SharePrefUtil.getString("userPwd", ""), deviceId, "admin", Constants.TYPE_BELL, deviceName, SharePrefUtil.getString("userToken", ""), listener);
    }

    private int masterCount = 5;
    OnConnListener listener = new OnConnListener() {
        @Override
        public void onSuccess(String s) {
            progressDialog.dismiss();
            Log.d("request", s);
            SharePrefUtil.putBoolean("isRefresh", true);
            DeviceUtils.deleteAllDevice();
            Toast.makeText(AddDeviceStep2.this, "添加成功", Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onFailure(int i, String s) {
            if (masterCount == 0) {
                progressDialog.dismiss();
                Log.i("VKHttp", "AddDeviceStep2--> 添加失败-->> masterCount :"+masterCount);
                Toast.makeText(AddDeviceStep2.this, "添加失败", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.i("VKHttp", "AddDeviceStep2--> 添加失败-->>再次请求 masterCount :"+masterCount);
                isCommitDevice=false;
                handler.sendEmptyMessageDelayed(MSG_SEND_WIFI_SUCCESS, 1000);
                masterCount--;
            }
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onStart() {

        }
    };
    private boolean isCommitDevice = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SEND_WIFI_SUCCESS:
                    if(isWifiConnected(AddDeviceStep2.this)|| NetworkUtil.isConnectionAvailable(AddDeviceStep2.this)) {
                        if (!isCommitDevice) {
                            sendDeviceToMaster();
                            isCommitDevice = true;
                        }
                    } else {//判断ap切换wifi成功
                        Log.i("VKHttp", "AddDeviceStep2--> wifi连接不成功-->> ");
                        handler.sendEmptyMessageDelayed(MSG_SEND_WIFI_SUCCESS, 1000);
                        isCommitDevice=false;
                    }
                    break;
            }
        }
    };
        public  boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String extraInfo = wifiNetworkInfo.getExtraInfo();
        if (extraInfo != null && wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    ISettingListener settingListener = new ISettingListener() {
        @Override
        public void onSuccess(int i, final Object o) {
            //这里需要加个倒计时的功能，延时20秒是为了等待手机连上其它网络，如果手机不能自动切换wifi，请到设置界面设置。（安卓手机可以调用方法设置手机wifi）。
            handler.sendEmptyMessageDelayed(MSG_SEND_WIFI_SUCCESS, 20000);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddDeviceStep2.this, " CMD : 2005 " + (String) o, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onFailure(String s) {

        }
    };

    public void onEventMainThread(TestEvent event) {
        Log.d("onEvent", "AddDeviceStep2 event:" + event.get_string());
        //获取设备的状态
        if (event.get_string().equals(Constants.ACTION_UPDATE_STATE)) {
            Bundle bundle = event.get_bundle();
            if (deviceId.equals(bundle.getString("clientStrId")) && bundle.getInt("clientState", 0) == 1) {
                isConnectDevice = true;
                Log.d("onEvent", " clientStrId:" + bundle.getString("clientStrId") + "  state:" + bundle.getInt("clientState", 0));
            }
        }
    }
}
