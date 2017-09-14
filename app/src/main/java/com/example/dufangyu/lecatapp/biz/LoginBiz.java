package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/8/30.
 */

public class LoginBiz extends BaseBiz implements ILogin{





    public LoginBiz(LoginListener listener)
    {
        this.listener = listener;
    }
    private LoginListener listener;

    @Override
    public void login(String loginName, String password) {

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
                    //大参数2:管理部门码  个人用户管理码为空
                    //大参数3:管理权限 (后续定义).
                    listener.loginSuccess(strParam2,strParam3);
                }
            }

        }else if(intDataType ==1106||intDataType ==1107)
        {
            if(strDataType.equals("1001"))
            {
                DataManager.getManagerInstance().saveDeviceListData();
                if(listener!=null)
                    listener.getDeviceList();
            }
        }
    }
}
