package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.CallBack.DataCallBackImp;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class MainBiz implements IMain{
    private MainListener listener;


    public MainBiz(MainListener listener)
    {
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
        this.listener = listener;
    }
    @Override
    public void sendLoginCommad(String loginName, String password) {

        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0002", "", "", "", "", "", "", "", "", "", loginName,password, "", "", "", "", "", "");
    }

    @Override
    public void getDepResult(String depCode, String loginName) {

//        LogUtil.d("dfy","depCode = "+depCode);
//        LogUtil.d("dfy","loginName = "+loginName);
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1107, "0", "1", "1", "", "", "", "", "", "", "", depCode, loginName, "", "", "", "", "", "");
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
                    listener.loginSuccess();

                }
            }else if(intDataType ==1106||intDataType ==1107)
            {
                if(strDataType.equals("0"))
                {
                    DataManager.getManagerInstance().saveDepListData();
                }
            }
        }
    };

}
