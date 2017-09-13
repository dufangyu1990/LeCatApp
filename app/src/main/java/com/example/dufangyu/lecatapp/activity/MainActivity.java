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

    private boolean fromlogin;
    private IMain mainBiz;
    private String depCode;//部门code  个人用户登录返回""
    private String strShareUserName;
    private String strSharePassword;
    private static SplashActivity splashActivity;
    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        mainBiz = new MainBiz(this);
        FragmentManager manager = getSupportFragmentManager();
        mView.initTabs(manager);
        fromlogin = getIntent().getBooleanExtra("fromlogin",false);
        depCode = getIntent().getStringExtra("depCode");
        strShareUserName = MyApplication.getInstance().getStringPerference("UserName");
        strSharePassword =MyApplication.getInstance().getStringPerference("Password");
        if(!fromlogin)
        {
            mainBiz.sendLoginCommad(strShareUserName, strSharePassword);
        }
        //获取设备列表
        mainBiz.getDeviceList(strShareUserName);

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


    /**
     *
     * @param context
     * @param fromlogin 是否是从登录界面进来  true  是   false 不是
     * false 的时候需要发送一个登陆登录指令
     */
    public static void actionStart(Context context,boolean fromlogin)
    {
//        Intent intent = new Intent(context,MainActivity.class);
//        intent.putExtra("fromlogin",fromlogin);
//        context.startActivity(intent);


        splashActivity = (SplashActivity) context;
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("fromlogin",fromlogin);
        context.startActivity(intent);
        splashActivity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);

    }

    public static void actionStart(Context context,boolean fromlogin,String code)
    {
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("fromlogin",fromlogin);
        intent.putExtra("depCode",code);
        context.startActivity(intent);




    }


    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailed() {

    }



    @Override
    public void jumpToModifyPawActivity(String loginName,String pwdStr) {
        ModifyPwdActivity.actionStart(this,loginName,pwdStr);
    }

    @Override
    public void jumpToUpdateActivity() {
        UpdateActivity.actionStart(this);
    }


    @Override
    public void jumpToScanCodeActivity() {
        CaptureActivity.actionStart(this);

    }


}
