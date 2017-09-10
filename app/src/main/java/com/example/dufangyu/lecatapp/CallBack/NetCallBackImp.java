package com.example.dufangyu.lecatapp.CallBack;

import android.os.Handler;

/**
 * Created by dufangyu on 2016/11/10.
 * 监听网络状态回调
 */
public abstract class NetCallBackImp extends NetCallBack{
    private Handler mHandler = new Handler();

    @Override
    public void onHandle(final int stateCode) {
        mHandler.post(new Runnable(){

            @Override
            public void run() {
                runOnUI(stateCode);
            }
        });
    }

    public abstract void runOnUI(int stateCode);
}
