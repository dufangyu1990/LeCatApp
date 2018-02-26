package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.LogUtil;

/**
 * Created by dufangyu on 2018/2/4.
 */

public class LightBiz extends BaseBiz implements ILightBiz{
    private LightControlListener listener;
    private StringBuffer temBuf= new StringBuffer();


    public LightBiz(LightControlListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void sendLightCommand(String light_type) {
        LogUtil.d("dfy","发送灯控指令");
        temBuf.setLength(0);
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
//        LogUtil.d("dfy","temBuf = "+temBuf.toString());
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2160, "0002", "101", temBuf.toString(), "", "", "", "", "", "", "", light_type,"" , "", "", "", "", "", "");
    }


    @Override
    public void detachDataCallBackNull() {
        TcpConnectUtil.getTcpInstance().setDataCallBack(null);
    }

    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
        if(intDataType ==2160)
        {
            //收到巡检指令返回
            if(strDataType.equals("1001"))
            {
                if(listener!=null)
                    listener.getDeviceData(strParam1,strParam2,strParam3,strParam4);
            }
        }
    }
}
