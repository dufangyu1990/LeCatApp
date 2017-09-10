package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.CallBack.DataCallBackImp;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class SplashBiz implements ISplash{

    SplashListener listener;

    public SplashBiz()
    {
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
    }

    @Override
    public void checkVersion(String old_versionCode,SplashListener listener) {
        this.listener = listener;
//        LogUtil.d("dfy","old_versionCode = "+old_versionCode);
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0010", "", "", "", "", "", "", "", "", "", old_versionCode, "", "", "", "", "", "", "");

    }

    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
//            LogUtil.d("dfy","strParam1 = "+strParam1);
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

    };
}
