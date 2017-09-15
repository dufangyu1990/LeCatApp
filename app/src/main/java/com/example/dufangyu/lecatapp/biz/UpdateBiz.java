package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class UpdateBiz extends BaseBiz implements IUpdate{


    private UpdateListener listener;

    @Override
    public void updateApp(String newVersionCode, UpdateListener listener) {
        this.listener = listener;
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0011", "", "", "", "", "", "", "", "", "", newVersionCode, "", "", "", "", "", "", "");
    }

    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1,
                                      String strAlmComType, String strParam1, String strParam2, String strParam3,String[] strArr) {

        if(intDataType==1105)
        {
            if(strDataType.equals("1011"))
            {
                if(strParam1.equals("1"))
                {
                    listener.updateSuccess();
                }else{
                    listener.updateFail();
                }
            }
        }


    }
}
