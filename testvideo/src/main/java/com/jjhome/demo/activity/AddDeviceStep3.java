package com.jjhome.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.jjhome.demo.utils.SharePrefUtil;
import com.jjhome.master.http.HttpUtils;
import com.jjhome.master.http.OnConnListener;

import de.greenrobot.event.EventBus;

import static com.jjhome.demo.utils.NetworkUtil.isWifiConnected;

/**
 * Author: LuoQ
 * DATE: 2018/1/9
 */

public class AddDeviceStep3 extends Activity {
    private EditText edit_device_name, edit_wifi_ssid, edit_wifi_psw;
    private ProgressDialog mProgressDialog;
    private boolean mIsSetWifi;//设置wifi之前接收到设备登录状态
    private static final int MSG_SEND_SET_WIFI = 0;
    private static final int MSG_REQUEST_DEVICE_TO_SERVER = 1;
    private static final int MSG_SEND_WEAK_UP_BELL = 2;//唤醒设备
    private String mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_add_step2);
        mDeviceId = getIntent().getStringExtra("deviceId");

        mProgressDialog = new ProgressDialog(this);
        initView();
        //TODO 先删除设备
        DeviceUtils.deleteSingleDevice(mDeviceId);

    }

    private void initView() {
        edit_device_name = (EditText) findViewById(R.id.edit_device_name);
        edit_wifi_ssid = (EditText) findViewById(R.id.edit_wifi_ssid);
        edit_wifi_psw = (EditText) findViewById(R.id.edit_wifi_psw);

        Button commit = (Button) findViewById(R.id.btn_commit_wifi);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 1.连接设备
                mProgressDialog.setMessage("加载中...");
                mProgressDialog.show();
                DeviceUtils.connectDevice(mDeviceId, "", "admin", Constants.TYPE_CAMERA, false, "", "");
                mHandler.sendEmptyMessage(MSG_SEND_WEAK_UP_BELL);
            }
        });
    }

    public void onEventMainThread(TestEvent event) {
        Log.d("onEvent", "AddDeviceStep2 event:" + event.get_string());
        //获取设备的状态
        if (event.get_string().equals(Constants.ACTION_UPDATE_STATE)) {
            Bundle bundle = event.get_bundle();
            if (mDeviceId.equals(bundle.getString("clientStrId")) && bundle.getInt("clientState", 0) == 1) {
                if (!mIsSetWifi) {
                    Log.i("VKSDK", "AddDeviceStep3-->send msg -->> ");
                    // TODO: 2.接收到设备登录成功状态，开始后设置wifi
                    mHandler.sendEmptyMessageDelayed(MSG_SEND_SET_WIFI, 1000);
                } else {
                    //TODO: 3.请求添加到服务器.
                    mHandler.sendEmptyMessageDelayed(MSG_REQUEST_DEVICE_TO_SERVER, 8000);
                }
            } else if (mDeviceId.equals(bundle.getString("clientStrId")) && bundle.getInt("clientState", 0) == 0) {
                /*
                 *   如果为门铃等需要唤醒的设备,但状态为0时,可以使用以下方式唤醒设备.
                 */
                mHandler.sendEmptyMessageDelayed(MSG_SEND_WEAK_UP_BELL, 1000);
            }
        }
        if (event.get_string().equals(Constants.ACTION_BELL_IS_WAKE_UP)) {
            //设备已经登录成功
        }
    }

    private boolean isCommitDevice;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SEND_SET_WIFI:
                    this.removeMessages(MSG_SEND_SET_WIFI);
                    sendWifiSetting();
                    break;
                case MSG_REQUEST_DEVICE_TO_SERVER:
                    this.removeMessages(MSG_REQUEST_DEVICE_TO_SERVER);
                    //判断wifi连接或者移动网络可用
                    if (isWifiConnected(AddDeviceStep3.this) || HttpUtils.netIsAvailable(AddDeviceStep3.this)) {
                        if (!isCommitDevice) {
                            sendDeviceToMaster();
                            isCommitDevice = true;
                        }
                    } else {//判断ap切换wifi成功,不可用重新请求
                        Log.i("VKSDK", "AddDeviceStep3--> WIFI未连接-->> ");
                        mHandler.sendEmptyMessageDelayed(MSG_REQUEST_DEVICE_TO_SERVER, 1000);
                        isCommitDevice = false;
                    }
                    break;
                case MSG_SEND_WEAK_UP_BELL:
                    mHandler.removeMessages(MSG_SEND_WEAK_UP_BELL);
                    DeviceUtils.weakUpBell(mDeviceId);
                    break;
            }
        }
    };

    private void sendWifiSetting() {
        String ssid = edit_wifi_ssid.getText().toString().trim();
        String wifiPassword = edit_wifi_psw.getText().toString().trim();
        for (int i = 0; i < 3; i++) {
            DeviceUtils.setDeviceWifi(mDeviceId, ssid, wifiPassword, mListener);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mIsSetWifi = true;
        DeviceUtils.deleteSingleDevice(mDeviceId);
        DeviceUtils.connectDevice(mDeviceId, "", "admin", Constants.TYPE_CAMERA, false, "", "");
    }

    ISettingListener mListener = new ISettingListener() {
        @Override
        public void onSuccess(final int responseType, final Object response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddDeviceStep3.this, " CMD:" + responseType + " WIFI设置 " + response, Toast.LENGTH_SHORT).show();
                }
            });
            Log.i("TAG", " CMD:" + responseType + " WIFI设置 " + response);
        }

        @Override
        public void onFailure(String msg) {
            Toast.makeText(AddDeviceStep3.this, "msg:" + msg, Toast.LENGTH_SHORT).show();
            Log.i("TAG", "msg:" + msg);
        }

    };

    /**
     * 添加设备到服务器
     */
    private void sendDeviceToMaster() {
        String deviceName = edit_device_name.getText().toString();
        YeeApplication.getMasterRequest().msAddDevice(SharePrefUtil.getString("userId", ""), SharePrefUtil.getString("userPwd", ""),
                mDeviceId, "admin", Constants.TYPE_BELL, deviceName, SharePrefUtil.getString("userToken", ""), mConnListener);
    }

    private int masterCount = 20;
    OnConnListener mConnListener = new OnConnListener() {
        @Override
        public void onSuccess(String s) {
            Log.i("VKSDK", "AddDeviceStep3--> 添加成功-->> masterCount "+masterCount);
            mProgressDialog.dismiss();
            SharePrefUtil.putBoolean("isRefresh", true);
            DeviceUtils.deleteAllDevice();
            Toast.makeText(AddDeviceStep3.this, "添加成功", Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onFailure(int i, String s) {
            if (masterCount == 0) {
                mProgressDialog.dismiss();
                Log.i("VKSDK", "AddDeviceStep3--> 添加失败finish -->> masterCount :"+masterCount);
                Toast.makeText(AddDeviceStep3.this, "添加失败", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.i("VKSDK", "AddDeviceStep3--> 添加失败-->> masterCount :"+masterCount);
                isCommitDevice = false;
                mHandler.sendEmptyMessageDelayed(MSG_REQUEST_DEVICE_TO_SERVER, 1000);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }
}
