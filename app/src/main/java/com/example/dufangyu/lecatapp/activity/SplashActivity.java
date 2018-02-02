package com.example.dufangyu.lecatapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.ISplash;
import com.example.dufangyu.lecatapp.biz.SplashBiz;
import com.example.dufangyu.lecatapp.biz.SplashListener;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.DownloadTool;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.SplashView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.example.dufangyu.lecatapp.utils.Constant.APKURL;

@RuntimePermissions
public class SplashActivity extends ActivityPresentImpl<SplashView> implements View.OnClickListener{

    private ISplash splashBiz = null;
    private boolean isServetConnect =false;

    @Override
    public void beforeViewCreate(Bundle savedInstanceState) {
        super.beforeViewCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                SplashActivityPermissionsDispatcher.downloadApkWithCheck(this);
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
//        Util.SystemMsg(SplashActivity.this, "", "网络连接失败,请检查网络状态");
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

        }else{
            mView.startAnim();
        }
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void downloadApk()
    {
        DownloadTool tool = new DownloadTool(SplashActivity.this);
        tool.execute(APKURL);
    }



    @Override
    public void pressAgainExit() {
        if(splashBiz!=null)
        {
            splashBiz.detachDataCallBackNull();
            splashBiz = null;
        }
        TcpConnectUtil.getTcpInstance().setDataCallBack(null);
        TcpConnectUtil.getTcpInstance().setRealDatCallBack(null);
        finish();
        System.exit(0);
    }



    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //用户选择拒绝的提示
    void showDenied()
    {
        MyToast.showTextToast(getApplicationContext(),"拒绝将无法下载最新APK");
        mView.startAnim();
    }


    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //用户选择不再询问后的提示
    void showNotAsk()
    {
        new AlertDialog.Builder(this).setMessage("该功能需要读写文件权限,不开启将无法下载新版apk,需要手动去设置里面赋予权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
        mView.startAnim();


    }
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //提示用户为何要开启此权限
    void showWhy(final PermissionRequest request)
    {
        new AlertDialog.Builder(this).setMessage("下载新版apk需要读写文件权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashBiz.detachDataCallBackNull();
        splashBiz = null;
    }
}
