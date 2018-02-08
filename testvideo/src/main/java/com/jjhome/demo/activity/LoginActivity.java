package com.jjhome.demo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jjhome.network.ddpush.YeePushUtils;
import com.google.gson.Gson;
import com.jjhome.demo.R;
import com.jjhome.demo.YeeApplication;
import com.jjhome.demo.bean.BaseInfoBean;
import com.jjhome.demo.bean.LoginBean;
import com.jjhome.demo.utils.SharePrefUtil;
import com.jjhome.demo.utils.ToastUtil;
import com.jjhome.master.http.OnConnListener;


public class LoginActivity extends AppCompatActivity {
    private String mPwd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameTv = (TextView) findViewById(R.id.login_name);
                TextView pwdTv = (TextView) findViewById(R.id.login_pwd);
                String name = nameTv.getText().toString();
                String pwd = pwdTv.getText().toString();
                boolean isCheck = checkData(name, pwd);
                if (isCheck) {
                    sendLoginData(name, pwd);
                }
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameTv = (TextView) findViewById(R.id.login_name);
                TextView pwdTv = (TextView) findViewById(R.id.login_pwd);
                String name = nameTv.getText().toString();
                String pwd = pwdTv.getText().toString();
                boolean isCheck = checkData(name, pwd);

                if (isCheck) {
                    sendRegisterData(name, pwd);
                }
            }
        });
        YeePushUtils.initPush();
    }

    public boolean checkData(String name, String pwd) {
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(LoginActivity.this, "请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(LoginActivity.this, "请输入密码");
            return false;
        }
        return true;
    }


    public void sendLoginData(final String name, final String pwd) {
        mPwd = pwd;
        if (TextUtils.isEmpty(YeeApplication.APP_ID)) {
            ToastUtil.showToast(this, "请查询自己的APP_ID");
            return;
        }
        YeeApplication.getMasterRequest().msLogin(name, pwd, YeeApplication.APP_ID, new OnConnListener() {

            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(String s) {
                Log.d("success--->>>", s);
                Gson gson = new Gson();

                LoginBean loginBean = gson.fromJson(s, LoginBean.class);
                if (loginBean == null) {
                    onFailure(1, "数据有误");
                } else {
                    if (loginBean.errcode == 0) {
                        SharePrefUtil.putString("userId", loginBean.user_id);
                        SharePrefUtil.putString("userPwd", pwd);
                        SharePrefUtil.putString("userToken", loginBean.getUser_token());
                        SharePrefUtil.putString("userPushIp", loginBean.getPushserver_ip());

                        YeeApplication.instance.setLoginBean(loginBean);
                        Intent intent = DeviceListActivity.getIntent(loginBean.user_id, mPwd, loginBean.pushserver_ip, LoginActivity.this);
                        startActivity(intent);
                        finish();
                    } else {
                     Toast.makeText(LoginActivity.this, "errcode "+loginBean.errcode +" errorInfo "
                             +loginBean.errinfo , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (!TextUtils.isEmpty(s)) {
                    ToastUtil.showToast(LoginActivity.this, s);
                }
                Log.d("错误----", ">>>>>>>>>>>" + i);
            }


            public void onFinish() {
                progressDialog.dismiss();
            }
        });
    }

    public void sendRegisterData(final String name, final String pwd) {
        mPwd = pwd;
        if (TextUtils.isEmpty(YeeApplication.APP_ID)) {
            ToastUtil.showToast(this, "请查询自己的APP_ID");
            return;
        }
        YeeApplication.getMasterRequest().msRegister(name, pwd, YeeApplication.APP_ID, new OnConnListener() {

            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(String s) {
                Log.d("success-->", s);
                Gson gson = new Gson();
                BaseInfoBean baseInfoBean = gson.fromJson(s, BaseInfoBean.class);
                if (baseInfoBean == null) {
                    onFailure(1, "数据有误");
                } else if (baseInfoBean.errcode != 0) {
                    ToastUtil.showToast(LoginActivity.this, baseInfoBean.errinfo);
                } else {
                    ToastUtil.showToast(LoginActivity.this, "注册成功");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (!TextUtils.isEmpty(s)) {
                    ToastUtil.showToast(LoginActivity.this, s);
                } else {
                    ToastUtil.showToast(LoginActivity.this, "Failure");

                }
            }


            public void onFinish() {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
