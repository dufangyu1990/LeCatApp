package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.IRegist;
import com.example.dufangyu.lecatapp.biz.RegistBiz;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyCountDownTimer;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.utils.Util;
import com.example.dufangyu.lecatapp.view.RegistView;

/**
 * Created by dufangyu on 2017/9/11.
 */

public class RegistActivity extends ActivityPresentImpl<RegistView> implements View.OnClickListener,View.OnFocusChangeListener{

    private Handler myHandler = new Handler();
    private MyCountDownTimer countTimer;
    private IRegist registBiz;
    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,RegistActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        registBiz =new RegistBiz();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;
            case R.id.submit_btn:
                registUser();
                break;
            case R.id.verfycodeTv:
                if(!mView.isValidPhone())
                {
                    MyToast.showTextToast(getApplicationContext(),"请输入正确的手机号");
                }else{
                    if(countTimer==null)
                    {
                        initCounterTimer();
                    }
                    countTimer.start();
                    mView.setCodeValue();
                }
                break;
        }
    }


    public void initCounterTimer()
    {
        countTimer = new MyCountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mView.startCountTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                mView.finishCountTime();
            }
        };
    }
    private void registUser()
    {
        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }


//            CustomLoadDialog.show(RegistActivity.this,"",true,null,R.layout.logindialog);
//            registBiz.registUser(mView.getValueById(R.id.username_editor), mView.getValueById(R.id.pwd_editor), new RegistListenr() {
//                @Override
//                public void registSuccess() {
//                    CustomLoadDialog.dismisDialog();
//                    MyToast.showTextToast(getApplicationContext(),"注册成功");
//                    finish();
//                }
//
//                @Override
//                public void registFail() {
//                    CustomLoadDialog.dismisDialog();
//                    MyToast.showTextToast(getApplicationContext(),"用户已存在");
//                }
//            });

    }




    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
        {
            getKeyboardHeight();
        }
    }

    private void getKeyboardHeight() {
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int  screenHeight = Util.getScreenHeight(RegistActivity.this);
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
    protected void onDestroy() {
        super.onDestroy();
        registBiz.detachDataCallBackNull();
        registBiz = null;
    }
}
