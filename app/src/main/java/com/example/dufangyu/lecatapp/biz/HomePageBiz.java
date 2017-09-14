package com.example.dufangyu.lecatapp.biz;


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
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(2150, "0002", "1", "WG12345678901234&", "", "", "", "", "", "", "", "","" , "", "", "", "", "", "");

    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
        if(intDataType==2150)
        {
            if(strDataType.equals("1002"))
            {
                LogUtil.d("dfy","strParam1="+strParam1+",strParam2="+strParam2+",strParam3="+strParam3);
            }
        }
    }
}
