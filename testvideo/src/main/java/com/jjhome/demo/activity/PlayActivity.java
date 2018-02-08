package com.jjhome.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jjhome.network.Constants;
import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.IGestureInterface;
import com.example.jjhome.network.ImageDrawerManager;
import com.example.jjhome.network.OnSingleTouchListener;
import com.example.jjhome.network.OpenGLDrawer;
import com.example.jjhome.network.TestEvent;
import com.example.jjhome.network.entity.DeviceListItemBean;
import com.example.jjhome.network.entity.ISettingListener;
import com.jjhome.demo.R;
import com.jjhome.demo.utils.ToastUtil;

import de.greenrobot.event.EventBus;

public class PlayActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "PlayActivity";
    private String strCameraID;
    /**
     * 最后按退回键的时间
     */
    private long exitTime;
    private final static long TIME_DIFF = 2 * 1000;

    private DeviceListItemBean mDevice;

    private String mDeviceID;
    private String mDeviceType;

    private Button btn_open_voice;
    private Button btn_open_mac;
    private Button btn_save_video;
    private boolean isOpenVoice = true;// 是否开启了声音
    private boolean isOpenMac = false;// 是否开启了对讲
    private boolean isSaveVideo = false;//是否开启录像
    private String[] btnVoice = {"声音开启", "声音关闭"};
    private String[] btnMac = {"通话开启", "通话关闭"};
    private LinearLayout linearLayout;
    private boolean isShakeHeaderModel = false;

    public static Intent getIntent(Context context, DeviceListItemBean deviceBean) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra("device", deviceBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        mDevice = (DeviceListItemBean) getIntent().getSerializableExtra("device");
        mDeviceID = mDevice.getDevice_id();
        mDeviceType = mDevice.getType();
        initView();
        EventBus.getDefault().register(this);
        linearLayout = (LinearLayout) findViewById(R.id.layOutVideo);
        Log.d(TAG, mDeviceID);

        setScreanOn();

        if (mDeviceType.equals(Constants.TYPE_BELL)) {
            DeviceUtils.startDoorBell(mDeviceID);
        }

        //开始播放 第三个参数：是否摇头机模式
        DeviceUtils.startPlay(mDeviceID, linearLayout, isShakeHeaderModel, this);
        //监听屏幕事件
        OpenGLDrawer imageDrawer = ImageDrawerManager.Instant().m_ImageDrawer[0];
        imageDrawer.setDeviceType(Constants.TYPE_CAMERA);
        if (isShakeHeaderModel) {//摇头机模式屏幕不可放大缩小
            imageDrawer.setIGestureInterface(new IGestureInterface() {
                @Override
                public void onScroll(int orientation) {
                    DeviceUtils.sendScreenOrientation(mDeviceID, orientation, new ISettingListener() {
                        @Override
                        public void onSuccess(int i, Object o) {

                        }

                        @Override
                        public void onFailure(String s) {

                        }
                    });
                }

                @Override
                public void onSingleDown() {
                    Log.d("VideoPlayActivity", "onSingleDown");
                    ToastUtil.showToast(PlayActivity.this, "点击了屏幕");
                }
            });
        } else {
            imageDrawer.setOnSingleTouchListener(new OnSingleTouchListener() {
                @Override
                public void onSingleTouch() {
                    ToastUtil.showToast(PlayActivity.this, "点击了屏幕");
                }
            });
        }
    }

    public void initView() {
        btn_open_voice = (Button) findViewById(R.id.btn_open_voice);
        btn_open_voice.setOnClickListener(this);
        btn_open_mac = (Button) findViewById(R.id.btn_open_mac);
        btn_open_mac.setOnClickListener(this);
        btn_save_video = (Button) findViewById(R.id.save_video);
        btn_save_video.setOnClickListener(this);
        Button screen_shot = (Button) findViewById(R.id.screen_shot);
        screen_shot.setOnClickListener(this);
    }


    /**
     * 屏幕常亮
     */
    private void setScreanOn() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();

        //退出时。请判断视频声音或通话是否打开来确定是否调用下面两个方法
        if (isOpenVoice) {
            DeviceUtils.stopAudio();
        }

        if (isOpenMac) {
            DeviceUtils.stopRecord(mDeviceID);
        }
        if (isSaveVideo) {
            DeviceUtils.stopSaveMp4Video(mDeviceID);
        }

        //结束播放 退出播放时调用
        DeviceUtils.stopPlay(mDeviceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something...
            if ((System.currentTimeMillis() - exitTime) > TIME_DIFF) {
                Toast.makeText(PlayActivity.this, "再次点击退出播放",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                PlayActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEvent(TestEvent event) {
        Log.d("onEvent", "PlayActivity event:" + event.get_string());
        //视频码流大小
        if (event.get_string().equals(Constants.ACTION_SEND_FLOW_RATE)) {
            Bundle bundle = event.get_bundle();
            Log.d("onEvent", "bundle.getInt(\"flowRate\")" + bundle.getInt("flowRate") + "KB/S");
        }
        //在线人数和最大在线人数
        if (event.get_string().equals(Constants.ACTION_SEND_ONLINE_NUMS)) {
            Bundle bundle = event.get_bundle();
            String deviceId = bundle.getString("deviceId");
            int onlineNums = bundle.getInt("onlineNums");
            int maxNums = bundle.getInt("maxNums");
            Log.d("onEvent", "deviceId" + deviceId + " onlineNums:" + onlineNums + " maxNums:" + maxNums);
        }

        //开始播放，可以在这里停止加载条
        if (event.get_string().equals(Constants.ACTION_PLAYING)) {
            Log.d("onEvent", "ACTION_PLAYING");
        }

        if (event.get_string().equals(Constants.ACTION_IMG_SHORT_SUCCESS)) {
            Log.d("onEvent", "截屏成功");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_voice:
                if (isOpenVoice) {
                    DeviceUtils.stopAudio();
                    btn_open_voice.setText(btnVoice[1]);
                    ToastUtil.showToast(this, "声音已经关闭");
                } else {
                    DeviceUtils.startAudio();
                    btn_open_voice.setText(btnVoice[0]);
                    ToastUtil.showToast(this, "声音已经开启");

                }
                isOpenVoice = !isOpenVoice;
                break;
            //Android 6.0以上手机需要录音动态权限 Manifest.permission.RECORD_AUDIO
            case R.id.btn_open_mac:
                if (isOpenMac) {
                    DeviceUtils.stopRecord(mDeviceID);
                    btn_open_mac.setText(btnMac[1]);
                    ToastUtil.showToast(this, "通话已经关闭");

                } else {
                    DeviceUtils.startRecord(mDeviceID);
                    btn_open_mac.setText(btnMac[0]);
                    ToastUtil.showToast(this, "通话已经开启");

                }
                isOpenMac = !isOpenMac;
                break;
            //Android 6.0以上手机需要SD卡存储权限
            // Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            case R.id.screen_shot:
                DeviceUtils.saveImg("YeelensSdk", "test");
                break;
            //Android 6.0以上手机需要SD卡存储权限
            // Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            case R.id.save_video:
                if (isSaveVideo) {
                    DeviceUtils.stopSaveMp4Video(mDeviceID);
                    btn_save_video.setText("录像已经停止");
                } else {
                    DeviceUtils.startSaveMp4Video(mDeviceID, "YeelensSdk", "Test_Video");
                    btn_save_video.setText("录像已经开始");
                }
                isSaveVideo = !isSaveVideo;
                break;
        }
    }
}
