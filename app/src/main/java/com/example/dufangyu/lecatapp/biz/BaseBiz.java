package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.CallBack.DataCallBackImp;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/13.
 */

public abstract class BaseBiz {

    public BaseBiz()
    {
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
    }

    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {
//            LogUtil.d("dfy","strParam1 = "+strParam1);
            handleServerResult(intDataType, strDataType, strSetSN, strSetSN1,strAlmComType,strParam1,strParam2,strParam3);
        }

    };


    protected abstract void  handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3);
}
