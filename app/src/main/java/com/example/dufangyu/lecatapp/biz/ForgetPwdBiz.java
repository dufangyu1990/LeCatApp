package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/6.
 */

public class ForgetPwdBiz extends BaseBiz implements IModifyPwd{


    private ModifyPwdListener listener;


    @Override
    public void modifyPwd(String loginName, String newPwd, ModifyPwdListener listener) {
        this.listener = listener;
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0003", "", "", "", "", "", "", "", "", "", loginName, newPwd, "", "", "", "", "", "");

    }


    @Override
    public void detachDataCallBackNull() {
        TcpConnectUtil.getTcpInstance().setDataCallBack(null);
    }

    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
        if(intDataType==1105)
        {
            if(strDataType.equals("1003"))
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
}
