package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class RegistBiz extends BaseBiz implements IRegist{


    private RegistListenr listener;
    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN,
                                      String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String strParam4,String[] strArr) {

        if(intDataType==1105)
        {
            if(strDataType.equals("1001"))
            {
                if(strParam1.equals("1"))
                {

                    listener.registSuccess();
                }else if(strParam1.equals("0"))
                {
                    listener.registFail();
                }
            }
        }

    }

    @Override
    public void registUser(String username, String password, RegistListenr listenr) {
        this.listener = listenr;
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0001", "", "", "", "", "", "", "", "", "", username, password, "", "", "", "", "", "");

    }

    @Override
    public void detachDataCallBackNull() {
        TcpConnectUtil.getTcpInstance().setDataCallBack(null);
    }
}
