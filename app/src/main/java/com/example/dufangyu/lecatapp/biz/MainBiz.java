package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class MainBiz extends BaseBiz implements IMain{
    private MainListener listener;


    public MainBiz(MainListener listener)
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
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(2150, "0002", "1", temBuf.toString(), "", "", "", "", "", "", "", "","" , "", "", "", "", "", "");

    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
     if(intDataType ==1106||intDataType ==1107)
        {
            if(strDataType.equals("1001"))
            {
                DataManager.getManagerInstance().saveDeviceListData();
                if(listener!=null)
                    listener.refreshUI();
            }
        }
    }
}
