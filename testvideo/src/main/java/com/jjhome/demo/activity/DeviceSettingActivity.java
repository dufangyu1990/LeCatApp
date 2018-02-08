package com.jjhome.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.entity.ISettingListener;
import com.example.jjhome.network.entity.MyBodySensity;
import com.example.jjhome.network.entity.MyDeviceInfo;
import com.example.jjhome.network.entity.MyDeviceVoiceSense;
import com.example.jjhome.network.entity.MyDeviceWifi;
import com.example.jjhome.network.entity.MyMoveInfo;
import com.example.jjhome.network.entity.MyRecodeMode;
import com.example.jjhome.network.entity.MyScreenOrientation;
import com.example.jjhome.network.entity.MyTimeZone;
import com.example.jjhome.network.entity.MyVideoDPI;
import com.jjhome.demo.R;


public class DeviceSettingActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "DeviceSettingActivity";
    private String mDeviceId;
    private int mFlip = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mDeviceId = getIntent().getStringExtra("deviceId");
        initView();
    }

    private void initView() {
        Button btn_device_info = (Button) findViewById(R.id.btn_device_info);
        btn_device_info.setOnClickListener(this);
        Button btn_device_wifi = (Button) findViewById(R.id.btn_device_wifi);
        btn_device_wifi.setOnClickListener(this);
        Button btn_device_recode_mode = (Button) findViewById(R.id.btn_device_recode_mode);
        btn_device_recode_mode.setOnClickListener(this);
        Button btn_device_video_dpi = (Button) findViewById(R.id.btn_device_video_dpi);
        btn_device_video_dpi.setOnClickListener(this);
        Button btn_device_moveinfo = (Button) findViewById(R.id.btn_device_moveinfo);
        btn_device_moveinfo.setOnClickListener(this);
        Button btn_body_sensitivity = (Button) findViewById(R.id.btn_body_sensitivity);
        btn_body_sensitivity.setOnClickListener(this);
        Button btn_device_timezone = (Button) findViewById(R.id.btn_device_timezone);
        btn_device_timezone.setOnClickListener(this);

        Button btn_set_device_wifi = (Button) findViewById(R.id.btn_set_device_wifi);
        btn_set_device_wifi.setOnClickListener(this);
        Button btn_set_device_recode_mode = (Button) findViewById(R.id.btn_set_device_recode_mode);
        btn_set_device_recode_mode.setOnClickListener(this);
        Button btn_set_device_video_dpi = (Button) findViewById(R.id.btn_set_device_video_dpi);
        btn_set_device_video_dpi.setOnClickListener(this);
        Button btn_set_device_moveinfo = (Button) findViewById(R.id.btn_set_device_moveinfo);
        btn_set_device_moveinfo.setOnClickListener(this);
        Button btn_set_body_sensitivity = (Button) findViewById(R.id.btn_set_body_sensitivity);
        btn_set_body_sensitivity.setOnClickListener(this);
        Button btn_set_device_timezone = (Button) findViewById(R.id.btn_set_device_timezone);
        btn_set_device_timezone.setOnClickListener(this);
        Button btn_set_device_time = (Button) findViewById(R.id.btn_set_device_time);
        btn_set_device_time.setOnClickListener(this);
        Button btn_set_screen_orientation = (Button) findViewById(R.id.btn_set_screen_orientation);
        btn_set_screen_orientation.setOnClickListener(this);
        Button btn_search_screen_orientation = (Button) findViewById(R.id.btn_search_screen_orientation);
        btn_search_screen_orientation.setOnClickListener(this);
        Button btn_device_format_SD = (Button) findViewById(R.id.btn_device_format_SD);
        btn_device_format_SD.setOnClickListener(this);
        Button btn_modify_pwd = (Button) findViewById(R.id.btn_modify_pwd);
        btn_modify_pwd.setOnClickListener(this);
        Button btn_device_voice_sense = (Button) findViewById(R.id.btn_device_voice_sense);
        btn_device_voice_sense.setOnClickListener(this);
        Button btn_set_device_voice_sense = (Button) findViewById(R.id.btn_set_device_voice_sense);
        btn_set_device_voice_sense.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_device_info:
                searchDeviceInfo(mDeviceId);
                break;
            case R.id.btn_device_wifi:
                searchDeviceWifi(mDeviceId);
                break;
            case R.id.btn_device_recode_mode:
                searchRecodeMode(mDeviceId);
                break;
            case R.id.btn_device_video_dpi:
                searchDeviceDPI(mDeviceId);
                break;
            case R.id.btn_device_moveinfo:
                searchMoveInfo(mDeviceId);
                break;
            case R.id.btn_body_sensitivity:
                searchBodySensitivity(mDeviceId);
                break;
            case R.id.btn_device_timezone:
                searchDeviceTimeZone(mDeviceId);
                break;
            case R.id.btn_set_device_wifi:
                setDeviceWifi(mDeviceId, "MERCURY_D23A", "QWERT123456");
                break;
            case R.id.btn_set_device_recode_mode:
                setDeviceRecodeMode(mDeviceId, 2);
                break;
            case R.id.btn_set_device_video_dpi:
                setDeviceVideoDPI(mDeviceId, (short) 1);
                break;
            case R.id.btn_set_device_moveinfo:
                setDeviceMoveInfo(mDeviceId, 1);
                break;
            case R.id.btn_set_body_sensitivity:
                setBodySensivity(mDeviceId, 1);
                break;
            case R.id.btn_set_device_timezone:
                setTimeZone(mDeviceId);
                break;
            case R.id.btn_set_device_time:
                setTime(mDeviceId);
                break;
            case R.id.btn_set_screen_orientation:
                if (mFlip == 0) {
                    setScreenOrientation(mDeviceId, 1, 1);
                } else if (mFlip == 1) {
                    setScreenOrientation(mDeviceId, 0, 0);
                }
                break;
            case R.id.btn_search_screen_orientation:
                searchScreenOrientation(mDeviceId);
                break;
            case R.id.btn_device_format_SD:
                setDeviceFormatSDcard(mDeviceId);
                break;
            case R.id.btn_modify_pwd:
                modifyDevicePassword(mDeviceId);
                break;
            case R.id.btn_device_voice_sense:
                sendSearchVoiceSensity(mDeviceId);
                break;
            case R.id.btn_set_device_voice_sense:
                sendSetVoiceSensity(mDeviceId, 50);
                break;
        }
    }

    /**
     * 查看设备信息
     *
     * @param deviceId
     */
    private void searchDeviceInfo(String deviceId) {
        DeviceUtils.searchDeviceInfo(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyDeviceInfo deviceInfo = (MyDeviceInfo) o;
                Log.d(TAG, deviceInfo.getResult().totalSpace + " freeSpace " + deviceInfo.getResult().freeSpace);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg: " + s);
            }
        });
    }

    /**
     * 查看设备wifi
     *
     * @param deviceId
     */
    private void searchDeviceWifi(String deviceId) {
        DeviceUtils.searchDeviceWifi(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyDeviceWifi deviceWifi = (MyDeviceWifi) o;
                Log.d(TAG, "ssid: " + deviceWifi.getResult().ssid);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg: " + s);
            }
        });
    }

    /**
     * 查看设备录像模式
     *
     * @param deviceId
     */
    private void searchRecodeMode(String deviceId) {
        DeviceUtils.searchRecodeMode(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyRecodeMode myRecodeMode = (MyRecodeMode) o;
                Log.d(TAG, myRecodeMode.getResult().mode);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg：" + s);
            }
        });
    }

    /**
     * 查看设备视频分辨率
     *
     * @param deviceId
     */
    private void searchDeviceDPI(String deviceId) {
        DeviceUtils.searchVideoDpi(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyVideoDPI myVideoDPI = (MyVideoDPI) o;
                Log.d(TAG, myVideoDPI.getResult().resolution);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg：" + s);
            }
        });
    }

    /**
     * 查看移动侦测
     *
     * @param deviceId
     */
    private void searchMoveInfo(String deviceId) {
        DeviceUtils.searchMoveInfo(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyMoveInfo myMoveInfo = (MyMoveInfo) o;
                Log.d(TAG, myMoveInfo.getResult().sensitivity);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg：" + s);
            }
        });
    }

    /**
     * 查看人体感应灵敏度
     *
     * @param deviceId
     */
    private void searchBodySensitivity(String deviceId) {
        DeviceUtils.searchBodySensity(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyBodySensity myBodySensity = (MyBodySensity) o;
                Log.d(TAG, myBodySensity.getResult().sensitivity);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg：" + s);
            }
        });
    }

    /**
     * 查看设备时区
     *
     * @param deviceId
     */
    private void searchDeviceTimeZone(String deviceId) {
        DeviceUtils.searchTimeZone(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyTimeZone myTimeZone = (MyTimeZone) o;
                Log.d(TAG, myTimeZone.getResult().szTimeZoneString);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "error msg：" + s);
            }
        });
    }

    /**
     * 设置设备密码
     *
     * @param deviceId
     * @param ssid
     * @param wifiPsw
     */
    private void setDeviceWifi(String deviceId, String ssid, String wifiPsw) {
        DeviceUtils.setDeviceWifi(deviceId, ssid, wifiPsw, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "配置wifi：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "配置wifi：" + s);
            }
        });
    }

    /**
     * @param deviceId
     * @param mode     0禁用， 1报警录像， 2全时录像
     */
    private void setDeviceRecodeMode(String deviceId, int mode) {
        DeviceUtils.setRecordMode(deviceId, mode, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "配置录像模式：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "配置录像模式：" + s);
            }
        });
    }

    private void setDeviceFormatSDcard(String deviceId) {
        DeviceUtils.setFormatSDcard(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int responseType, Object response) {
                Log.d(TAG, "格式化SD卡: " + response);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 设置视频分辨率
     *
     * @param deviceId
     * @param resolution 1高清，2标清，3流畅
     */
    private void setDeviceVideoDPI(String deviceId, short resolution) {
        DeviceUtils.setVideoDPI(deviceId, resolution, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "设置视频分辨率：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "设置视频分辨率：" + s);
            }
        });
    }

    /**
     * 设置移动侦测灵敏度
     *
     * @param deviceId
     * @param moveInfo 0禁用，1低，2中，3高
     */
    private void setDeviceMoveInfo(String deviceId, int moveInfo) {
        DeviceUtils.setMoveInfo(deviceId, moveInfo, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "设置移动侦测灵敏度：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "设置移动侦测灵敏度：" + s);
            }
        });
    }

    /**
     * 设置人体感应灵敏度
     *
     * @param deviceId
     * @param sensitivity 0禁用，1低，2中，3高
     */
    private void setBodySensivity(String deviceId, int sensitivity) {
        DeviceUtils.setBodySensity(deviceId, sensitivity, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "设置人体感应灵敏度：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "设置人体感应灵敏度：" + s);
            }
        });
    }

    /**
     * 同步本地时区到设备
     *
     * @param deviceId
     */
    private void setTimeZone(String deviceId) {
        DeviceUtils.setTimeZone(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "设置设备时区：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "设置设备时区：" + s);
            }
        });
    }

    /**
     * 同步本地时间到设备
     *
     * @param deviceId
     */
    private void setTime(String deviceId) {
        DeviceUtils.setTime(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "设置设备时间：" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "设置设备时间：" + s);
            }
        });
    }

    private void setScreenOrientation(String deviceId, int mirror, int flip) {
        DeviceUtils.setDeviceScreenOrentation(deviceId, mirror, flip, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "设置屏幕方向成功：" + o);
            }

            @Override
            public void onFailure(String s) {

            }
        });
    }

    private void searchScreenOrientation(String deviceId) {
        DeviceUtils.searchDeviceScreenOrentation(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyScreenOrientation screenOrientation = (MyScreenOrientation) o;
                mFlip = screenOrientation.getResult().flip;
                Log.d(TAG, "设备屏幕方向 flip：" + screenOrientation.getResult().flip + " mirror:" + screenOrientation.getResult().mirror);
            }

            @Override
            public void onFailure(String s) {

            }
        });
    }

    private void modifyDevicePassword(String deviceId) {
        DeviceUtils.setDevicePsw(deviceId, "oldPsw", "newPsw", new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "" + s);
            }
        });
    }

    /**
     * 获取声音侦测灵敏度
     *
     * @param deviceId
     */
    private void sendSearchVoiceSensity(String deviceId) {
        DeviceUtils.sendSearchVoiceSensibly(deviceId, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                MyDeviceVoiceSense voiceSense = (MyDeviceVoiceSense) o;
                Log.d(TAG, voiceSense.getResult().sensitivity + "dB");
            }

            @Override
            public void onFailure(String s) {

            }
        });
    }

    /**
     * 设置声音侦测灵敏度
     *
     * @param deviceId
     * @param sensity  50 到 120 当为0时声音侦测关闭
     */
    private void sendSetVoiceSensity(String deviceId, int sensity) {

        DeviceUtils.sendVoiceSensity(deviceId, sensity, new ISettingListener() {
            @Override
            public void onSuccess(int i, Object o) {
                Log.d(TAG, "" + o);
            }

            @Override
            public void onFailure(String s) {
                Log.d(TAG, "" + s);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
