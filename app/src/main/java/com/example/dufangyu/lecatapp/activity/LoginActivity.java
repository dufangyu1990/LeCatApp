package com.example.dufangyu.lecatapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.bean.LoginBean;
import com.example.dufangyu.lecatapp.biz.ILogin;
import com.example.dufangyu.lecatapp.biz.LoginBiz;
import com.example.dufangyu.lecatapp.biz.LoginListener;
import com.example.dufangyu.lecatapp.customview.CustomDialog;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.BroadCastControll;
import com.example.dufangyu.lecatapp.utils.Constant;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.utils.SharePrefUtil;
import com.example.dufangyu.lecatapp.utils.Util;
import com.example.dufangyu.lecatapp.view.LoginView;
import com.google.gson.Gson;
import com.jjhome.master.http.OnConnListener;

import static com.example.dufangyu.lecatapp.utils.Constant.REFRESH;
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
    private String yonghuming;
    private String phoneStr;
    private String addressStr;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;
    private static LoginActivity loginInstance;
    private static boolean isNeedJump = true;//是否需要跳转到主页



    private String mPwd;
    private LoginBean loginBean;


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        loginInstance = this;
        isConnected = getIntent().getBooleanExtra("isConnected",false);
        isFirstEnter = getIntent().getBooleanExtra("isFirstEnter",false);
        if(isConnected)
            mView.setNetState(TCPLINK,getResources().getString(R.string.netconnect));
        else
            mView.setNetState(TCPDISLINK,getResources().getString(R.string.strLinkStateLabel));
        mView.initPwdCheckBox();
        if(!isFirstEnter)
        {
            mView.ShowOverImg();
            loginBiz = new LoginBiz(this);
            autoLogin();
        }else{
            mView.hideOverImg();
        }

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        registMyRecivier();
        mLocalBroadcastManager.registerReceiver(mReceiver,filter);
        BroadCastControll.addReceiver(mReceiver);


    }



    private void registMyRecivier()
    {
        filter = new IntentFilter(Constant.REENTER);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if(action.equals(Constant.REENTER))
                {
                    LogUtil.d("dfy","收到重新登录的请求");
                    isNeedJump = false;
//                    LogUtil.d("dfy","loginBiz = "+loginBiz);
                    loginBiz = null;
                    loginBiz = new LoginBiz(loginInstance);
//                    LogUtil.d("dfy","loginBiz = "+loginBiz);
                    login();
                }
            }
        };
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
            case R.id.forgetPwdTv:
                ForgetPwdActivity.actionStart(LoginActivity.this);
                break;
        }
    }


    public static void actionStart(Context context,boolean isConnected,boolean isFirstEnter)
    {

        isNeedJump = true;
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
            if(loginBiz!=null)
            {
                loginBiz.detachDataCallBackNull();
                loginBiz = null;
            }
            TcpConnectUtil.getTcpInstance().setDataCallBack(null);
            TcpConnectUtil.getTcpInstance().setRealDatCallBack(null);
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
        departCode = code;
        LogUtil.d("dfy","登录成功 userName = "+userName+",phoneCall = "+phoneCall+",address = "+address);
        LogUtil.d("dfy","code = "+code);
        yonghuming = userName;
        phoneStr = phoneCall;
        addressStr = address;
        mView.saveAccountNdPwd(userName,phoneCall,address);



        //同时登陆视频demo的服务器
        loginVideoServer();



    }

    @Override
    public void loginFailed() {
        mView.cancleAnim();
        CustomDialog.show(LoginActivity.this, getResources().getString(R.string.loginfail), false, null, R.layout.text_dialog);
        CustomDialog.setAutoDismiss(true, 1500);
    }

    @Override
    public void getDeviceList() {
        if(isNeedJump)
        {
            MainActivity.actionStart(LoginActivity.this,departCode);
            loginBiz.detachDataCallBackNull();
            loginBiz = null;
            finish();
        }
        else{
            Util.sendLocalBroadcast(this,new Intent(REFRESH));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不在onDestroy中做detachDataCallBackNull操作，防止，下个页面设置了
        //callBack,此时上个页面onDestroy，又重新设为null了

    }



    private void loginVideoServer()
    {
        String name = "7988@qq.com";
        String pwd = "qq123456";
        sendLoginData(name,pwd);

    }

    public void sendLoginData(final String name, final String pwd) {
        mPwd = pwd;
        if (TextUtils.isEmpty(MyApplication.APP_ID)) {
            MyToast.showTextToast(this, "请查询自己的APP_ID");
            return;
        }
        MyApplication.getMasterRequest().msLogin(name, pwd, MyApplication.APP_ID, new OnConnListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(String s) {
                LogUtil.d("dfy", s);
                Gson gson = new Gson();
               loginBean = gson.fromJson(s, LoginBean.class);
                if (loginBean == null) {
                    onFailure(1, "数据有误");
                } else {
                    if (loginBean.errcode == 0) {



                        SharePrefUtil.putString("userId", loginBean.user_id);
                        SharePrefUtil.putString("userPwd", pwd);
                        SharePrefUtil.putString("userToken", loginBean.getUser_token());
                        SharePrefUtil.putString("userPushIp", loginBean.getPushserver_ip());


                        MyApplication.getInstance().setLoginBean(loginBean);
//                        Intent intent = DeviceListActivity.getIntent(loginBean.user_id, mPwd, loginBean.pushserver_ip, LoginActivity.this);
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "errcode "+loginBean.errcode +" errorInfo "
                                +loginBean.errinfo , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (!TextUtils.isEmpty(s)) {
                    MyToast.showTextToast(LoginActivity.this, s);
                }
                LogUtil.d("dfy", ">>>>>>>>>>>" + i);
            }


            public void onFinish() {

                //因为需要登录视频服务器，所以在登陆视频服务器操作结束后再进行
                // 获取设备跳转主页，防止先跳转主页后，视频服务器返回的数据拿不到
                loginBiz.getDeviceList(loginName);
            }
        });
    }
}
