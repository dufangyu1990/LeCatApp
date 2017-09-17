package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.LogUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class HomePageBiz extends BaseBiz implements IHomePage {
    private HomePageListener listener;


    public HomePageBiz(HomePageListener listener)
    {
        this.listener = listener;
    }


    @Override
    public void get4GPushData() {

        StringBuffer temBuf = new StringBuffer();
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
//        LogUtil.d("dfy","temBuf = "+temBuf.toString());
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(2150, "0002", "1", temBuf.toString(), "", "", "", "", "", "", "", "","" , "", "", "", "", "", "");

    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String[] strArr) {
//        LogUtil.d("dfy","设备Id = "+strArr[2]+",报警状态 = "+strArr[4]+",更新时间 = "+strArr[8]);
        LogUtil.d("dfy","温度值 = "+strArr[10]+",湿度值 = "+strArr[11]);
        if(intDataType==2150)
        {
            if(strDataType.equals("1002"))
            {
                RealData realData = new RealData();
                realData.setDeviceId(strArr[2]);
                realData.setAlarmState(strArr[4]);
                realData.setUpdateTime(strArr[8]);
                realData.setSonDeviceType(strArr[9]);
                realData.setTemperatureValue(strArr[10]);
                realData.setHumidityValueValue(strArr[11]);
                listener.refreshUI(realData);
            }
        }
    }
}
