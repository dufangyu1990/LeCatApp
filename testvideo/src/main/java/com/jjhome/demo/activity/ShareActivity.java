package com.jjhome.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jjhome.network.Constants;
import com.example.jjhome.network.ddpush.YeePushUtils;
import com.google.gson.Gson;
import com.jjhome.demo.R;
import com.jjhome.demo.YeeApplication;
import com.jjhome.demo.bean.RShareDeviceBean;
import com.jjhome.demo.bean.SharedDeviceBean;
import com.jjhome.demo.bean.SharedDeviceListBean;
import com.jjhome.demo.utils.SharePrefUtil;
import com.jjhome.master.http.OnConnListener;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    private String mDeviceId;
    private String mShareUserName;
    private List<SharedDeviceListBean> mShared_device_list;
    private SharedDeviceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mDeviceId = getIntent().getStringExtra("deviceId");
        intiView();
    }

    private void intiView() {
        Button btn_share = (Button) findViewById(R.id.btn_share);
        Button btn_share_list = (Button) findViewById(R.id.btn_share_list);
        btn_share.setOnClickListener(this);
        btn_share_list.setOnClickListener(this);

        ListView lv_shareDevList = (ListView) findViewById(R.id.lv_share_dev_list);
        mAdapter = new SharedDeviceAdapter();
        lv_shareDevList.setAdapter(mAdapter);
        mShared_device_list = new ArrayList<>();
        lv_shareDevList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                unShareDevice(((SharedDeviceListBean) mAdapter.getItem(position)).getUser_id());
                mShared_device_list.remove(position);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                initRequestShare();
                break;
            case R.id.btn_share_list:
                requestSharedDevList();
                break;
        }
    }

    private void initRequestShare() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_req_share, null);
        final EditText et_share_account = (EditText) view.findViewById(R.id.et_share_account);
        final EditText et_remark_name = (EditText) view.findViewById(R.id.et_remark_name);
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShareUserName = et_share_account.getText().toString();
                        String remark_name = et_remark_name.getText().toString();
                        requestShare(mShareUserName, remark_name);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void requestShare(String shareUserName, String refName) {
        YeeApplication.getMasterRequest().msShareDevice(SharePrefUtil.getString("userId", ""),
                SharePrefUtil.getString("userPwd", ""), mDeviceId
                , shareUserName, refName, SharePrefUtil.getString("userToken", ""),
                YeeApplication.APP_ID, new OnConnListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("ShareDevice", "ShareActivity onSuccess() response---" + response);
                        try {
                            Gson gson = new Gson();
                            RShareDeviceBean rShareDeviceBean = gson.fromJson(response, RShareDeviceBean.class);
                            String user_id = rShareDeviceBean.getUser_id();
                            if (!TextUtils.isEmpty(user_id)) {
                                //将分享信息推送到推送服务器通知被分享者
                                YeePushUtils.sendShareOrUnshareTcp(rShareDeviceBean.getPushserver_ip(),mDeviceId, Constants.CmdCode.CMD_103,user_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.i("ShareDevice", "ShareActivity onFailure() code---" + code + " msg---" + msg);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onStart() {

                    }
                });
    }

    private void unShareDevice(final String userId) {
        YeeApplication.getMasterRequest().msUnShareDevice(SharePrefUtil.getString("userId", ""),
                SharePrefUtil.getString("userPwd", ""), mDeviceId
                , userId, SharePrefUtil.getString("userToken", ""), new OnConnListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("ShareDevice", "ShareActivity  unShareDevice onSuccess() response--- " + response);
                        try{
                            Gson gson = new Gson();
                            RShareDeviceBean shareDeviceBean = gson.fromJson(response, RShareDeviceBean.class);
                            YeePushUtils.sendShareOrUnshareTcp(shareDeviceBean.getPushserver_ip(),mDeviceId,Constants.CmdCode.CMD_104,userId);
                            YeePushUtils.sendUnbindTcp(ShareActivity.this,shareDeviceBean.getPushserver_ip(),mDeviceId,true,userId);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.i("ShareDevice", "ShareActivity  unShareDevice onFailure() code--- " + code + " msg--- " + msg);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onStart() {

                    }
                });
    }

    private void requestSharedDevList() {
        YeeApplication.getMasterRequest().msGetSharedDevices(SharePrefUtil.getString("userId", ""),
                SharePrefUtil.getString("userPwd", ""), mDeviceId
                , SharePrefUtil.getString("userToken", ""), new OnConnListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("shareDevice", "ShareActivity requestSharedDevList onSuccess() response--- " + response);
                        Gson gson = new Gson();
                        SharedDeviceBean sharedDeviceBean = null;
                        try {
                            sharedDeviceBean = gson.fromJson(response, SharedDeviceBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        if (sharedDeviceBean.errcode == 0) {
                            mShared_device_list = sharedDeviceBean.shared_device_list;
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.i("shareDevice", "ShareActivity requestSharedDevList() errcode---" + sharedDeviceBean.errcode
                                    + " errinfo---" + sharedDeviceBean.errinfo);
                        }
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

    private class SharedDeviceAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mShared_device_list == null ? 0 : mShared_device_list.size();
        }

        @Override
        public Object getItem(int position) {
            return mShared_device_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ShareActivity.this).inflate(R.layout.item_shared_dev, null);
                holder = new ViewHolder();
                holder.refName = (TextView) convertView.findViewById(R.id.tv_refname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.refName.setText(mShared_device_list.get(position).getRef_name());
            return convertView;
        }

        class ViewHolder {
            TextView refName;
        }
    }
}
