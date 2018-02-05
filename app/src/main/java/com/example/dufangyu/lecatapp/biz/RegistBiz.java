package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class RegistBiz extends BaseBiz implements IRegist{


    private RegistListenr listener;

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

    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
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
}
