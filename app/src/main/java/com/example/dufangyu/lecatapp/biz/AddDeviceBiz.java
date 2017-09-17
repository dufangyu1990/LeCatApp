package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

import static com.example.dufangyu.lecatapp.utils.Constant.DEVICE_TYPE;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceBiz extends BaseBiz implements IAddDevice{


    private AddDeviceListener listener;

    public AddDeviceBiz(AddDeviceListener listener)
    {
        this.listener = listener;
    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String[] strArr) {

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
        }else if (intDataType == 1106 || intDataType == 1107) {
            if (strDataType.equals("1001")) {
                DataManager.getManagerInstance().saveDeviceListData();
                listener.getDeviceOver();
            }
        }


    }

    @Override
    public void addDevice(String loginName,String deviceId, String nickName) {
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1108, "0002", "1", "", "", "", "", "", "", loginName, DEVICE_TYPE, deviceId, nickName, "", "", "", "", "", "");
    }

    @Override
    public void getMyDeviceAgain(String loginName) {
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1107, "0001", "", "", "", "", "", "", "", "", "", loginName, "", "", "", "", "", "", "");
    }
}
