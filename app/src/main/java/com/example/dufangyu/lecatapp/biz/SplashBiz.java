package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class SplashBiz extends BaseBiz implements ISplash{

    SplashListener listener;



    @Override
    public void checkVersion(String old_versionCode,SplashListener listener) {
        this.listener = listener;
//        LogUtil.d("dfy","old_versionCode = "+old_versionCode);
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0010", "", "", "", "", "", "", "", "", "", old_versionCode, "", "", "", "", "", "", "");

    }

    @Override
    public void detachDataCallBackNull() {
        TcpConnectUtil.getTcpInstance().setDataCallBack(null);
    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
        if(intDataType==1105)
        {
            if(strDataType.equals("1010"))
            {
                if(strParam1.equals("2"))
                {
                    listener.updateNewApp();
                }else if(strParam1.equals("1"))
                {
                    listener.noNeedUpdateApp();
                }
            }
        }
    }
}
