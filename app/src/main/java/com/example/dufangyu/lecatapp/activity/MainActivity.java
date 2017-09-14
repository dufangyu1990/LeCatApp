package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.IMain;
import com.example.dufangyu.lecatapp.biz.MainBiz;
import com.example.dufangyu.lecatapp.biz.MainListener;
import com.example.dufangyu.lecatapp.listener.JumpToActivityListener;
import com.example.dufangyu.lecatapp.present.FragmentActivityPresentImpl;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.MainView;

import zxing.CaptureActivity;
public class MainActivity extends FragmentActivityPresentImpl<MainView> implements MainListener,JumpToActivityListener {

    private long exitTime=0;

    private IMain mainBiz;
    private String depCode;//部门code  个人用户登录返回""
    private String strShareUserName;
    private String strSharePassword;
    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        mainBiz = new MainBiz(this);
        FragmentManager manager = getSupportFragmentManager();
        mView.initTabs(manager);
        depCode = getIntent().getStringExtra("depCode");
        strShareUserName = MyApplication.getInstance().getStringPerference("UserName");
        strSharePassword =MyApplication.getInstance().getStringPerference("Password");
        //获取设备推送数据
        mainBiz.get4GPushData();

    }


















    public static void actionStart(Context context,String code)
    {
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("depCode",code);
        context.startActivity(intent);


    }


    @Override
    public void refreshUI() {


    }


    //跳转修改密码界面
    @Override
    public void jumpToModifyPawActivity(String loginName,String pwdStr) {
        ModifyPwdActivity.actionStart(this,loginName,pwdStr);
    }

    //跳转到版本界面
    @Override
    public void jumpToUpdateActivity() {
        UpdateActivity.actionStart(this);
    }


    //扫码界面
    @Override
    public void jumpToScanCodeActivity() {
        CaptureActivity.actionStart(this);

    }

    @Override
    public void jumpToMyDeviceActivity() {
        MyDeviceActivity.actionStart(this,strShareUserName);
    }

    public void pressAgainExit(){
        if((System.currentTimeMillis()-exitTime) > 2000){
            MyToast.showTextToast(getApplicationContext(), getResources().getString(R.string.pressagain));
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
