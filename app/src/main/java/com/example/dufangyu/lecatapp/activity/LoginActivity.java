package com.example.dufangyu.lecatapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.ILogin;
import com.example.dufangyu.lecatapp.biz.LoginBiz;
import com.example.dufangyu.lecatapp.biz.LoginListener;
import com.example.dufangyu.lecatapp.customview.CustomDialog;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.utils.Util;
import com.example.dufangyu.lecatapp.view.LoginView;

import static com.example.dufangyu.lecatapp.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPLINK;


/**
 * Created by dufangyu on 2017/8/30.
 */

public class LoginActivity extends ActivityPresentImpl<LoginView> implements View.OnClickListener,View.OnFocusChangeListener,LoginListener{

    private long exitTime=0;
    private ILogin loginBiz = null;
    private boolean isConnected;
    private Handler myHandler = new Handler();
    private static Activity splashActivity;
    private  String loginName;
    private String password;
    private boolean isFirstEnter;
    private String departCode;
    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);

        isConnected = getIntent().getBooleanExtra("isConnected",false);
        isFirstEnter = getIntent().getBooleanExtra("isFirstEnter",false);
        if(isConnected)
            mView.setNetState(TCPLINK,getResources().getString(R.string.netconnect));
        mView.initPwdCheckBox();
        if(!isFirstEnter)
        {
            mView.ShowOverImg();
            loginBiz = new LoginBiz(this);
            autoLogin();
        }else{
            mView.hideOverImg();
        }


    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.loginlayout:
                loginBiz = new LoginBiz(this);
                login();
                break;
            case R.id.regist_user:
                RegistActivity.actionStart(LoginActivity.this);
                break;
            case R.id.user_name_et:
                getKeyboardHeight();
                break;
            case R.id.pass_word_et:
                getKeyboardHeight();
                break;
        }
    }


    public static void actionStart(Context context,boolean isConnected,boolean isFirstEnter)
    {

        splashActivity = (Activity) context;
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("isConnected",isConnected);
        intent.putExtra("isFirstEnter",isFirstEnter);
        context.startActivity(intent);
        splashActivity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);

    }








    //自动登陆
    private void autoLogin()
    {
        login();
    }


    private void login()
    {
        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }

        if(mView.checkInvalid())
        {
            loginName = mView.getTextValue(R.id.user_name_et);
            password = mView.getTextValue(R.id.pass_word_et);
            mView.startloginAnim();
            loginBiz.login(loginName, password);
        }





    }


    public void pressAgainExit(){
        if((System.currentTimeMillis()-exitTime) > 2000){
            MyToast.showTextToast(getApplicationContext(), getResources().getString(R.string.pressagain));
            exitTime = System.currentTimeMillis();
        } else {
            myHandler.removeCallbacksAndMessages(null);
            finish();
            System.exit(0);
        }
    }

    @Override
    public void doNetConnect() {
        super.doNetConnect();
        mView.setNetState(TCPLINK,getResources().getString(R.string.netconnect));

    }


    @Override
    public void doNetDisConnect() {
        super.doNetDisConnect();
        mView.setNetState(TCPDISLINK,getResources().getString(R.string.strLinkStateLabel));
    }


    private void getKeyboardHeight() {
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int  screenHeight = Util.getScreenHeight(LoginActivity.this);
                //判断窗口可见区域大小
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > screenHeight / 3;
                if (isKeyboardShowing) {
                    changeScrollView();
                    //移除布局变化监听
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
    }

    private void changeScrollView() {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                //将ScrollView滚动到底
                mView.scrollview();
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
        {
            getKeyboardHeight();
        }
    }

    @Override
    public void loginSuccess(String code, String author,String userName,String phoneCall,String address) {
        mView.cancleAnim();
        mView.saveAccountNdPwd();
        departCode = code;
        loginBiz.getDeviceList(loginName);

    }

    @Override
    public void loginFailed() {
        mView.cancleAnim();
        CustomDialog.show(LoginActivity.this, getResources().getString(R.string.loginfail), false, null, R.layout.text_dialog);
        CustomDialog.setAutoDismiss(true, 1500);
    }

    @Override
    public void getDeviceList() {
        MainActivity.actionStart(LoginActivity.this,departCode);
        finish();
    }
}
