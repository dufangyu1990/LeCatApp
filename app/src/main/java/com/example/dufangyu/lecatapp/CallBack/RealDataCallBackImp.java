package com.example.dufangyu.lecatapp.CallBack;

import android.os.Handler;

/**
 * Created by dufangyu on 2016/11/2.
 *  获取数据回调
 */

public abstract class RealDataCallBackImp extends RealDataCallBack{
    private Handler mHandler = new Handler();


    @Override
    public void onReceiveResult(final int intDataType, final String strDataType,final String strSetType,final String strSetSN,
                                final String strSetSN1,final String strAlmComType,final String strHisType,final String strPosType,
                                final String strFadeType,final String strRecogType,
                                final String strRecogType1,final String strParam1,
                                final String strParam2,final String strParam3,
                                final String strParam4,final String strParam5,
                                final String strParam6,final String strParam7,
                                final String strParam8,final String[] strArr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onReceiveServerResult(intDataType,  strDataType,  strSetType,  strSetSN,
                        strSetSN1,  strAlmComType,  strHisType,  strPosType,
                        strFadeType,  strRecogType,  strRecogType1,  strParam1,
                        strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
                        strParam7,  strParam8,strArr);
            }
        });
    }





    public abstract void onReceiveServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr);

}
