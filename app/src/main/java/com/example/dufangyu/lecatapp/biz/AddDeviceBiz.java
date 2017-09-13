package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

import static com.example.dufangyu.lecatapp.utils.Constant.DEVICE_TYPE;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceBiz extends BaseBiz implements IAddDevice{


    private AddDeviceListener listener;


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {

        if(intDataType==1108)
        {
            if(strDataType.equals("1002"))
            {
                if(strParam1.equals("1"))
                {
                    listener.AddDeviceSuccess();
                }else{
                    listener.AddDeviceFail();
                }
            }
        }


    }

    @Override
    public void addDevice(String loginName,String deviceId, String nickName, AddDeviceListener listener) {
        this.listener = listener;
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1108, "0002", "1", "", "", "", "", "", "", loginName, DEVICE_TYPE, deviceId, nickName, "", "", "", "", "", "");
    }
}
