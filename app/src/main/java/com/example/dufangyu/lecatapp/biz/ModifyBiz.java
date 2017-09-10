package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.CallBack.DataCallBackImp;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/6.
 */

public class ModifyBiz implements IModifyPwd{


    private ModifyPwdListener listener;
    public ModifyBiz()
    {
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
    }

    @Override
    public void modifyPwd(String loginName, String newPwd, ModifyPwdListener listener) {
        this.listener = listener;
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1107, "53", "", "", "", "", "", "", "", "", "", loginName, newPwd, "", "", "", "", "", "");

    }


    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
            if(intDataType==1109)
            {
                if(strDataType.equals("53"))
                {
                    if(strParam1.equals("1"))//修改成功
                    {
                        listener.modifySuccess();
                    }else if(strParam1.equals("0"))//修改失败
                    {
                        listener.modifyFail();
                    }
                }

            }
        }


    };

}
