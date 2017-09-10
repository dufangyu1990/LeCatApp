package com.example.dufangyu.lecatapp.CallBack;

import android.os.Handler;

/**
 * Created by dufangyu on 2016/11/2.
 *  获取数据回调
 */

public abstract class DataCallBackImp extends DataCallBack{
    private Handler mHandler = new Handler();


    @Override
    public void onReceiveResult(final int intDataType, final String strDataType, final String strSetSN, final String strSetSN1, final String strAlmComType,
                                final String strParam1, final String strParam2, final String strParam3)
    {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onReceiveServerResult(intDataType, strDataType, strSetSN, strSetSN1,strAlmComType,strParam1,strParam2,strParam3);
            }
        });
    }

    public abstract void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3);
}
