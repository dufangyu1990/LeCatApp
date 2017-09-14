package com.example.dufangyu.lecatapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.ISplash;
import com.example.dufangyu.lecatapp.biz.SplashBiz;
import com.example.dufangyu.lecatapp.biz.SplashListener;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.utils.DownloadTool;
import com.example.dufangyu.lecatapp.utils.Util;
import com.example.dufangyu.lecatapp.view.SplashView;

import static com.example.dufangyu.lecatapp.utils.Constant.APKURL;


public class SplashActivity extends ActivityPresentImpl<SplashView> implements View.OnClickListener{

    private ISplash splashBiz;
    private boolean isServetConnect =false;

    @Override
    public void beforeViewCreate(Bundle savedInstanceState) {
        super.beforeViewCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
    }

    @Override
    public void presentCallBack(String param1, String param2, String params3) {
        super.presentCallBack(param1,param2,params3);

        String  isFirst = MyApplication.getInstance().getStringPerference("isFirst");
        if("NO".equals(isFirst))
        {
//            MainActivity.actionStart(SplashActivity.this,false);
            LoginActivity.actionStart(SplashActivity.this,isServetConnect,false);
        }else{
            LoginActivity.actionStart(SplashActivity.this,isServetConnect,true);
        }
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.update_ok:
                DownloadTool tool = new DownloadTool(this);
                tool.execute(APKURL);
                mView.disMisDialog();
                break;
            case R.id.update_no:
                mView.disMisDialog();
                mView.startAnim();
                break;
        }
    }


    @Override
    public void doNoNetWork() {
//        LogUtil.d("dfy","doNoNetWork.....");
        super.doNoNetWork();
        isServetConnect = false;
        Util.SystemMsg(SplashActivity.this, "", "网络连接失败,请检查网络状态");
        executeAnim();
    }

    @Override
    public void doNetConnect() {
//        LogUtil.d("dfy","doNetConnect.....");
        super.doNetConnect();
        isServetConnect = true;
        executeAnim();




    }

    @Override
    public void doNetDisConnect() {
//        LogUtil.d("dfy","doNetDisConnect.....");
        super.doNetDisConnect();
        isServetConnect =false;
        executeAnim();
    }

    private void executeAnim()
    {
        if(isServetConnect)
        {
            splashBiz = new SplashBiz();
            splashBiz.checkVersion(MyApplication.getInstance().getStringPerference("appVersionNo"), new SplashListener() {
                @Override
                public void updateNewApp() {
                    mView.showUpdateDialog();
                }
                @Override
                public void noNeedUpdateApp() {
                    mView.startAnim();
                }
            });

        }
    }


    @Override
    public void pressAgainExit() {
        finish();
        System.exit(0);
    }
}
