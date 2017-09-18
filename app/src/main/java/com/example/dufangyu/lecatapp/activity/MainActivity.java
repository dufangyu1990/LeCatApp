package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.listener.JumpToActivityListener;
import com.example.dufangyu.lecatapp.present.FragmentActivityPresentImpl;
import com.example.dufangyu.lecatapp.utils.BroadCastControll;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.MainView;

import zxing.CaptureActivity;
public class MainActivity extends FragmentActivityPresentImpl<MainView> implements JumpToActivityListener {

    private long exitTime=0;

    private String depCode;//部门code  个人用户登录返回""
    private String strShareUserName;
    private String strSharePassword;
    private boolean isNetConnnect =true  ;
    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        FragmentManager manager = getSupportFragmentManager();
        mView.initTabs(manager);
        depCode = getIntent().getStringExtra("depCode");
        strShareUserName = MyApplication.getInstance().getStringPerference("UserName");
        strSharePassword =MyApplication.getInstance().getStringPerference("Password");


    }


    public static void actionStart(Context context,String code)
    {
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("depCode",code);
        context.startActivity(intent);


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

    //再按一次退出
    public void pressAgainExit(){
        if((System.currentTimeMillis()-exitTime) > 2000){
            MyToast.showTextToast(getApplicationContext(), getResources().getString(R.string.pressagain));
            exitTime = System.currentTimeMillis();
        } else {
            BroadCastControll.removeAllReciver(this);
            finish();
            System.exit(0);
        }
    }





}
