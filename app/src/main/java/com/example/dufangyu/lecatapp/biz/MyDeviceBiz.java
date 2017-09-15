package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/14.
 */

public class MyDeviceBiz extends BaseBiz implements IMyDevice{

    public MyDeviceBiz(MyDeviceListener listener)
    {
        this.listener = listener;
    }
    private MyDeviceListener listener;
    @Override
    public void getMyDevice(String loginName) {

        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1107, "0001", "", "", "", "", "", "", "", "", "", loginName, "", "", "", "", "", "", "");
    }

    @Override
    public void deleteDevice(String loginName,String deviceId) {
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1108, "0002", "0", "", "", "", "", "", "", loginName, "", deviceId, "", "", "", "", "", "", "");
    }

    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1,
                                      String strAlmComType, String strParam1, String strParam2, String strParam3,String[] strArr) {
        if(intDataType ==1106||intDataType ==1107)
        {
            if(strDataType.equals("1001"))
            {

                DataManager.getManagerInstance().saveDeviceListData();

                if(listener!=null)
                {
                    listener.getDevices();
                }
            }
        }else if(intDataType == 1108)
        {
            if(strDataType.equals("1002"))
            {

                if(strParam1.equals("1"))
                {
                    listener.deleteSuccess();
                }else if(strParam1.equals("0"))
                {
                    listener.deleteFail();
                }
            }
        }


    }
}
