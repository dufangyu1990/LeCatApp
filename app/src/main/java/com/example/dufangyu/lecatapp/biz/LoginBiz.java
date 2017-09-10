package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.CallBack.DataCallBackImp;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/8/30.
 */

public class LoginBiz implements ILogin{


    private LoginListener listener;
    public LoginBiz()
    {
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
    }

    @Override
    public void login(String loginName, String password, LoginListener listener) {

        this.listener = listener;
//        LogUtil.d("dfy","发送登录指令");
//        LogUtil.d("dfy","loginName = "+loginName);
//        LogUtil.d("dfy","password = "+password);
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0002", "", "", "", "", "", "", "", "", "", loginName,password, "", "", "", "", "", "");
    }

    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
            if(intDataType==1105)
            {
                if(strParam1.equals("0"))//登录失败
                {
                    listener.loginFailed();
                }else if(strParam1.equals("1"))//登录成功
                {
                    listener.loginSuccess(strParam2);

                }
            }
        }


    };

}
