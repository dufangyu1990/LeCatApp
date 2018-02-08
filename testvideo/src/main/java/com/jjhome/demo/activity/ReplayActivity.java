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
import com.example.jjhome.network.ImageDrawerManager;
import com.example.jjhome.network.OnSingleTouchListener;
import com.example.jjhome.network.OpenGLDrawer;
import com.example.jjhome.network.TestEvent;
import com.example.jjhome.network.entity.DeviceListItemBean;
import com.example.jjhome.network.entity.FileItem;
import com.example.jjhome.network.entity.ReplayFileList;
import com.jjhome.demo.R;
import com.jjhome.demo.utils.ToastUtil;

import java.util.Calendar;

import de.greenrobot.event.EventBus;


public class ReplayActivity extends Activity {
    public static final String TAG = "ReplayActivity";
    /**
     * 最后按退回键的时间
     */
    private long exitTime;
    private final static long TIME_DIFF = 2 * 1000;

    private DeviceListItemBean mDevice;

    private String mDeviceID;
    private String mDeviceType;

    private LinearLayout linearLayout;
    private int mYear;
    private int mMonth;
    private int mDay;

    public static Intent getIntent(Context context, DeviceListItemBean deviceBean) {
        Intent intent = new Intent(context, ReplayActivity.class);
        intent.putExtra("device", deviceBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_replay);
        mDevice = (DeviceListItemBean) getIntent().getSerializableExtra("device");
        mDeviceID = mDevice.getDevice_id();
        mDeviceType = mDevice.getType();
        initView();
        Calendar m_Calendar = Calendar.getInstance();
        mYear = m_Calendar.get(Calendar.YEAR);
        mMonth = m_Calendar.get(Calendar.MONTH) + 1;
        mDay = m_Calendar.get(Calendar.DAY_OF_MONTH);
        EventBus.getDefault().register(this);
        linearLayout = (LinearLayout) findViewById(R.id.layOutVideo);
        setScreanOn();
        if (mDeviceType.equals(Constants.TYPE_BELL)) {
            //门铃需要调用此方法去唤醒和连接。连接成功后在onEventMainThread（）方法中搜索回看视频列表
            DeviceUtils.startDoorBell(mDeviceID);
        } else {
            // 一般摄像头在线才能进入此页面并搜索回看视频列表
            searchReplayData();
        }

        //开始播放
        DeviceUtils.startReplay(mDeviceID, linearLayout, this);

        //监听屏幕点击事件
        OpenGLDrawer imageDrawer = ImageDrawerManager.Instant().m_ImageDrawer[0];
        imageDrawer.setOnSingleTouchListener(new OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                ToastUtil.showToast(ReplayActivity.this, "点击了屏幕");
            }
        });
    }

    public void initView() {

        Button screen_shot = (Button) findViewById(R.id.screen_shot);
        screen_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtils.saveImg("YeelensSdk", "test");
            }
        });
    }
    /**
     * 屏幕常亮
     */
    private void setScreanOn() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void searchReplayData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //数据不会一次性全部返回，所以需要多搜索几次
                for (int i = 0; i < 5; i++) {
//                    if (ReplayFileList.Instant().GetSearchResult().fileItem.size() == 0) {
                    Log.d(TAG, "fileitemsize:" + ReplayFileList.Instant().GetSearchResult().fileItem.size());
                    //参数是年月日
                    DeviceUtils.searchReplayData(mDeviceID, mYear, mMonth, mDay);
//                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //结束播放 退出播放时调用
        DeviceUtils.stopReplay(mDeviceID);
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
                Toast.makeText(ReplayActivity.this, "再次点击退出播放",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ReplayActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEventMainThread(TestEvent event) {
        Log.d("onEvent", "PlayActivity event:" + event.get_string());
        if (event.get_string().equals(Constants.ACTION_UPDATE_STATE)) {
            Bundle bundle = event.get_bundle();
            if (bundle.getInt("clientState", 0) == 1 && bundle.getString("clientStrId").equals(mDeviceID)) {
                //门铃只有在线状态才能搜索回放视频列表
                searchReplayData();
            }
        }
        //获取历史视频文件成功
        if (event.get_string().equals(Constants.MSG_GET_REPLAY_DATA)) {
            if (ReplayFileList.Instant().GetSearchResult().fileItem.size() > 0) {
                FileItem item = (FileItem) ReplayFileList.Instant().GetSearchResult().fileItem.get(0);
                Log.d("onEvent", "MSG_GET_REPLAY_DATA:" + item.toString());
                DeviceUtils.replayPlay(mDeviceID, item.fileHandler, item.getYear(), item.getMonth(), item.getDay(), item.getHour(), item.getMinute(), item.getSecond());
            }
        }
//        //视频码流大小
//        if (event.get_string().equals(Constants.ACTION_SEND_FLOW_RATE)) {
//            Bundle bundle = event.get_bundle();
//            Log.d("onEvent", "bundle.getInt(\"flowRate\")" + bundle.getInt("flowRate") + "KB/S");
//        }
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
            Toast.makeText(ReplayActivity.this, "截屏成功", Toast.LENGTH_SHORT).show();
        }
        //没有历史视频
        if (event.get_string().equals(Constants.ACTION_NONE_REPLAY_DATA)) {
            Toast.makeText(ReplayActivity.this, "没有视频文件", Toast.LENGTH_SHORT).show();
        }
        //设备没有SDcard
        if (event.get_string().equals(Constants.ACTION_NONE_SD_CARD)) {
            Toast.makeText(ReplayActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
        }
        //历史视频播放进度－－－返回每一帧视频的时间
        if (event.get_string().equals(Constants.ACTION_SEND_REPLAY_SEEK)) {
            Bundle bundle = event.get_bundle();
            int highTime = bundle.getInt("highTime");
            int lowTime = bundle.getInt("lowTime");
            int[] timeArray = new int[7];
            DeviceUtils.hl2TimeArray(highTime, lowTime, timeArray);
            //timeArray中的七位数据分别是 年 月 日 时 分 秒 毫秒
            Log.d(TAG, "year:" + timeArray[0] + " month:" + timeArray[1] + " day:" + timeArray[2] + " hour:" + timeArray[3] + " minute:" + timeArray[4] + " second:" + timeArray[5] + " msecond:" + timeArray[6]);
        }
    }
}
