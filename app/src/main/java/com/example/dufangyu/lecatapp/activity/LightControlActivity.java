package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.ILightBiz;
import com.example.dufangyu.lecatapp.biz.LightBiz;
import com.example.dufangyu.lecatapp.biz.LightControlListener;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.view.LightControlView;

/**
 * Created by dufangyu on 2018/2/4.
 */

public class LightControlActivity extends ActivityPresentImpl<LightControlView> implements View.OnClickListener,LightControlListener{

    private ILightBiz lightBiz;
    private String lightValue;


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        lightBiz = new LightBiz(this);
        lightValue = getIntent().getStringExtra("lightValue");
        mView.refreshUI(lightValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                if(lightBiz!=null)
                {
                    lightBiz.detachDataCallBackNull();
                    lightBiz = null;
                }
                finish();
                break;
            case R.id.blue_light:
                lightBiz.sendLightCommand("3");
                break;
            case R.id.red_light:
                lightBiz.sendLightCommand("1");
                break;
            case R.id.green_light:
                lightBiz.sendLightCommand("2");
                break;
            case R.id.close_light:
                lightBiz.sendLightCommand("0");
                break;
        }

    }



    public static void actionStart(Context context, String lightValue)
    {
        Intent intent = new Intent(context,LightControlActivity.class);
        intent.putExtra("lightValue",lightValue);
        context.startActivity(intent);
    }

    @Override
    public void getDeviceData(String param1, String param2, String param3, String param4) {
        LogUtil.d("dfy","getDeviceData refreshUI");
        mView.refreshUI(param1);
    }

    @Override
    public void pressAgainExit() {
        if(lightBiz!=null)
        {
            lightBiz.detachDataCallBackNull();
            lightBiz = null;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
