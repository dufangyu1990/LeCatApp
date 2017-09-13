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
    public void sendLoginCommad(String loginName, String password) {

        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0002", "", "", "", "", "", "", "", "", "", loginName,password, "", "", "", "", "", "");
    }



    @Override
    public void getDeviceList(String loginName) {
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1107, "0001", "", "", "", "", "", "", "", "", "", loginName, "", "", "", "", "", "", "");
    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
        if(intDataType==1105)
        {
            if(strDataType.equals("1002"))
            {
                if(strParam1.equals("0"))//登录失败
                {
                    listener.loginFailed();
                }else if(strParam1.equals("1"))//登录成功
                {
                    listener.loginSuccess();
                }
            }

        }else if(intDataType ==1106||intDataType ==1107)
        {
            if(strDataType.equals("1001"))
            {
                DataManager.getManagerInstance().saveDeviceListData();
            }
        }
    }
}
