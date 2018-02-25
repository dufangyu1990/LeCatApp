package com.example.dufangyu.lecatapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.adapter.DeviceListAdapter;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.utils.SharePrefUtil;
import com.example.jjhome.network.Constants;
import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.TestEvent;
import com.example.jjhome.network.ddpush.YeePushService;
import com.example.jjhome.network.ddpush.YeePushUtils;
import com.example.jjhome.network.entity.DeviceListBean;
import com.example.jjhome.network.entity.DeviceListItemBean;
import com.google.gson.Gson;
import com.jjhome.master.http.OnConnListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class DeviceListActivity extends Activity implements View.OnClickListener {

    private ListView mListView;
    private DeviceListAdapter mListAdapter;
    private String mUserID;
    private String mUserPass;
    private String mPushIp;
    private DeviceListBean mListBean = new DeviceListBean();
    private ArrayList<String> mPushIpList = new ArrayList<>();

    public static Intent getIntent(String userId, String userPwd, String pushIp, Context context) {
        Intent intent = new Intent(context, DeviceListActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userPwd", userPwd);
        intent.putExtra("pushIp", pushIp);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_device_list);
        mUserID = getIntent().getStringExtra("userId");
        mUserPass = getIntent().getStringExtra("userPwd");
        mPushIp = getIntent().getStringExtra("pushIp");

        initView();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceListItemBean mDevice = mListAdapter.getItem(position);
                Intent intent = DeviceOptionsActivity.getIntent(DeviceListActivity.this, mDevice);
                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceListItemBean mDevice = mListAdapter.getItem(position);
                deleteLiveDevice(mDevice);
                return true;
            }
        });
        getData();

        Intent startSrv = new Intent(this.getApplicationContext(), YeePushService.class);
        startSrv.putExtra("pushIp", mPushIp);
        this.getApplicationContext().startService(startSrv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //不能每次进入都刷新列表，减轻服务器压力
        if (SharePrefUtil.getBoolen("isRefresh", false)) {
            getData();
            SharePrefUtil.putBoolean("isRefresh", false);
        }
    }

    public void initView() {
        mListView = (ListView) findViewById(R.id.list);
        Button btn_add_device = (Button) findViewById(R.id.btn_add_device);
        btn_add_device.setOnClickListener(this);
        mListAdapter = new DeviceListAdapter(this);
        mListView.setAdapter(mListAdapter);
    }

    /**
     * 从服务器请求设备列表
     */
    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(DeviceListActivity.this);
        progressDialog.setMessage("加载中...");
        //TODO 从服务器获取设备列表
        Log.d("success--->>>", "getData");
        MyApplication.getMasterRequest().msDeviceList(mUserID, mUserPass, SharePrefUtil.getString("userToken", ""), new OnConnListener() {
            @Override
            public void onStart() {
//                progressDialog.show();
                Log.d("success--->>>", "onStart()");
            }

            @Override
            public void onSuccess(String s) {
                Log.d("success--->>>", s);
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                try {
                    Gson gson = new Gson();
                    mListBean = gson.fromJson(s, mListBean.getClass());
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (mListBean == null) {
                    onFailure(1, "没有数据");
                    return;
                }
                if (mListBean.errcode != 0) {
                    onFailure(mListBean.errcode, mListBean.errinfo);
                    return;
                }
                List<DeviceListItemBean> device_list = mListBean.device_list;
                mPushIpList.clear();
                if (null != device_list && device_list.size() > 0) {
                    DeviceUtils.deleteAllDevice();
                    for (int i = 0; i < device_list.size(); i++) {
                        DeviceListItemBean deviceListItemBean = device_list.get(i);
                        DeviceUtils.connectDevice(deviceListItemBean.getDevice_id(), deviceListItemBean.getDevice_name(), deviceListItemBean.getDevice_pass(), "0", deviceListItemBean.getShare(), deviceListItemBean.getShared_by(), deviceListItemBean.p2pserver_ip + "-" + deviceListItemBean.p2pserver_port);
                        if (!mPushIpList.contains(deviceListItemBean.getPushserver_ip())) {
                            mPushIpList.add(deviceListItemBean.getPushserver_ip());
                        }
                    }
                    mListAdapter.addData(mListBean.device_list);
                }

                if (mPushIpList != null && mPushIpList.size() > 0) {
                    YeePushUtils.resetServiceClient(getApplicationContext(), mPushIpList);
                    YeePushUtils.sendDeviceList(mPushIp, DeviceListActivity.this, device_list, mUserID);//将设备信息绑定到推送服务器
                }

            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("success--->>>", "onFailure()" + s);
                if (!TextUtils.isEmpty(s)) {
                    MyToast.showTextToast(DeviceListActivity.this, s);
                }
            }

            @Override
            public void onFinish() {
//                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DeviceUtils.deleteAllDevice();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_device:
//                Intent intent = new Intent(this, AddDeviceStep1.class);
//                startActivity(intent);
                break;
        }
    }

    /**
     * 删除设备
     */
    private void deleteLiveDevice(final DeviceListItemBean deviceInfo) {
        new AlertDialog.Builder(DeviceListActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("注意").setMessage("是否删除设备")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (deviceInfo.getShare()) {
                            removeDevice(deviceInfo);
                        } else {
                            requestSharedDevList(deviceInfo);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private int removeDevice(DeviceListItemBean deviceInfo) {
        if (deviceInfo != null) {
            requestDeleteDevice(deviceInfo, deviceInfo.getShare());
        }
        return 0;
    }

    private void requestDeleteDevice(final DeviceListItemBean deviceInfo, Boolean share) {
        if (share) {
            deleteShared(deviceInfo);
        } else {
            deleteUnshared(deviceInfo);
        }
    }

    private void deleteUnshared(final DeviceListItemBean deviceInfo) {
        MyApplication.getMasterRequest().msDeleteDevice(SharePrefUtil.getString("userId", ""),
                SharePrefUtil.getString("userPwd", ""), deviceInfo.getDevice_id(), SharePrefUtil.getString("userToken", ""), new OnConnListener() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(DeviceListActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                        YeePushUtils.sendUnbindTcp(DeviceListActivity.this, deviceInfo.getPushserver_ip(), deviceInfo.getDevice_id(), false, SharePrefUtil.getString("userId", ""));

                        for (int i = 0; i < mListBean.device_list.size(); i++) {
                            if ((mListBean.device_list.get(i)).getDevice_id().equals(deviceInfo.getDevice_id())) {
                                mListBean.device_list.remove(i);
                                break;
                            }
                        }
                        mListAdapter.addData(mListBean.device_list);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(DeviceListActivity.this, "删除失败", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onStart() {

                    }
                });
    }

    private void deleteShared(final DeviceListItemBean deviceInfo) {
        MyApplication.getMasterRequest().msDeleteSharedDevice(SharePrefUtil.getString("userId", "")
                , SharePrefUtil.getString("userPwd", ""), deviceInfo.getDevice_id(), SharePrefUtil.getString("userToken", ""), new OnConnListener() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(DeviceListActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                        YeePushUtils.sendUnbindTcp(DeviceListActivity.this, deviceInfo.getPushserver_ip(), deviceInfo.getDevice_id(), true, SharePrefUtil.getString("userId", ""));
                        for (int i = 0; i < mListBean.device_list.size(); i++) {
                            if ((mListBean.device_list.get(i)).getDevice_id().equals(deviceInfo.getDevice_id())) {
                                mListBean.device_list.remove(i);
                                break;
                            }
                        }
                        mListAdapter.addData(mListBean.device_list);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(DeviceListActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onStart() {

                    }
                });
    }

    /**
     * 查询某个设备分享的列表
     */
    private void requestSharedDevList(final DeviceListItemBean deviceInfo) {
        MyApplication.getMasterRequest().msGetSharedDevices(SharePrefUtil.getString("userId", ""),
                SharePrefUtil.getString("userPwd", ""), deviceInfo.getDevice_id()
                , SharePrefUtil.getString("userToken", ""), new OnConnListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("shareDevice", "ShareActivity requestSharedDevList onSuccess() response--- " + response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            JSONArray ja = object.getJSONArray("shared_device_list");
                            if (ja.length() > 0) {// 如果已经分享给别人先请求推送服务器把别人删掉
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jb = ja.getJSONObject(i);
                                    String pushserver_ip = jb.getString("pushserver_ip");
                                    String user_id = jb.getString("user_id");
                                    YeePushUtils.sendShareOrUnshareTcp(pushserver_ip, deviceInfo.getDevice_id(), Constants.CmdCode.CMD_104, user_id);
                                }
                            }
                        } catch (JSONException e) {
                            //当没有分享给其他用户时,后台返回的shared_device_list的Json格式是{}
                            //所以object.getJSONArray("shared_device_list");会抛异常,下面再处理一次.
                            try {
                                object = new JSONObject(response);
                                JSONObject jb = object.getJSONObject("shared_device_list");
                                if (jb != null) {
                                    String pushserver_ip = jb.getString("pushserver_ip");
                                    String user_id = jb.getString("user_id");
                                    YeePushUtils.sendShareOrUnshareTcp(pushserver_ip, deviceInfo.getDevice_id(), Constants.CmdCode.CMD_104, user_id);
                                }
                            } catch (JSONException e1) {
                            }
                        }
                        removeDevice(deviceInfo);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.i("shareDevice", "ShareActivity requestSharedDevList onFailure() code--- " + code + " msgmsg--- " + msg);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onStart() {

                    }
                });
    }

    public void onEventMainThread(TestEvent event) {
        Log.d("onEvent", "DeviceListActivity onEventMainThread:" + event.get_string());
        //获取设备的状态
        if (event.get_string().equals(Constants.ACTION_UPDATE_STATE)) {
            Bundle bundle = event.get_bundle();
            //0:不在线；1：在线 2:密码错误
            Log.d("onEvent", "onEventMainThread bundle.getInt(\"clientState\", 0)" + bundle.getInt("clientState", 0));
            //device_id
//            Log.i("VKState", "DeviceListActivity--> clientState -->> " + bundle.getInt("clientState", 0) + " clientStrId " + bundle.getString("clientStrId"));
            Log.d("onEvent", "onEventMainThread bundle.getString(\"clientStrId\")" + bundle.getString("clientStrId"));
        }

        //在线人数和最大在线人数
        if (event.get_string().equals(Constants.ACTION_SEND_ONLINE_NUMS)) {
            Bundle bundle = event.get_bundle();
            String deviceId = bundle.getString("device_id");
            int onlineNums = bundle.getInt("onlineNums");
            int maxNums = bundle.getInt("maxNums");
            Log.d("onEvent", "device_id" + deviceId + " onlineNums:" + onlineNums + " maxNums:" + maxNums);
        }

        //修改设备密码结果
        if (event.get_string().equals(Constants.MSG_REPSW_RESPONSE)) {
            Bundle bundle = event.get_bundle();
            String result = bundle.getString("result");
            //result为0时设置成功，在这里调用主服务器的修改设备密码接口
            //MyApplication.getMasterRequest().msModifyDevice();
            Log.d("onEvent", "result:" + result);
        }

        if (event.get_string().equals("testyeepushservice")) {
            Bundle bundle = event.get_bundle();
            String cmd = bundle.getString("cmd");
            MyToast.showTextToast(this, cmd);
            Log.d("onEvent", "cmd:" + cmd);
        }
    }

    /**
     * 最后按退回键的时间
     */
    private long exitTime;
    private final static long TIME_DIFF = 2 * 1000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//            if ((System.currentTimeMillis() - exitTime) > TIME_DIFF) {
//                MyToast.showTextToast(getApplicationContext(), "再次点击退出App");
//                exitTime = System.currentTimeMillis();
//            } else {
//                //
//                DeviceListActivity.this.finish();
//                /**
//                 * 退出登录时发送信息到推送服务器，以免仍然能收到推送
//                 *
//                 * @param userPushIp 用户登录时获取的 pushIp
//                 * @param context    getApplicationContext();
//                 * @param pushList   设备列表中返回的推送服务器ip（不要重复）
//                 * @param userId     用户Id
//                 */
//
//                YeePushUtils.sendLogoutTcp(SharePrefUtil.getString("userPushIp", ""),
//                        DeviceListActivity.this, mPushIpList, SharePrefUtil.getString("userId", ""));
//            }



            DeviceListActivity.this.finish();
            /**
             * 退出登录时发送信息到推送服务器，以免仍然能收到推送
             *
             * @param userPushIp 用户登录时获取的 pushIp
             * @param context    getApplicationContext();
             * @param pushList   设备列表中返回的推送服务器ip（不要重复）
             * @param userId     用户Id
             */

            YeePushUtils.sendLogoutTcp(SharePrefUtil.getString("userPushIp", ""),
                    DeviceListActivity.this, mPushIpList, SharePrefUtil.getString("userId", ""));

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
