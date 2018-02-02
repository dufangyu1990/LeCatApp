package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.ForgetPwdBiz;
import com.example.dufangyu.lecatapp.biz.IModifyPwd;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.utils.MyCountDownTimer;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.ForgetPwdView;

/**
 * Created by dufangyu on 2018/2/1.
 */

public class ForgetPwdActivity extends ActivityPresentImpl<ForgetPwdView> implements View.OnClickListener{



    private IModifyPwd forgetPwdBiz;

    private MyCountDownTimer countTimer;


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        forgetPwdBiz = new ForgetPwdBiz();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;
            case R.id.forget_verfycodeTv:
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
            case R.id.resetPwd:
                if(mView.checkPwd())
                {
                    //do something
                }
                break;
        }

    }


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,ForgetPwdActivity.class);
        context.startActivity(intent);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        forgetPwdBiz.detachDataCallBackNull();
        forgetPwdBiz = null;
    }
}
