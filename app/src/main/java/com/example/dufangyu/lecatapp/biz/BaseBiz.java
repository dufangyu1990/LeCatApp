package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.CallBack.DataCallBackImp;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.LogUtil;

/**
 * Created by dufangyu on 2017/9/13.
 */

public abstract class BaseBiz {

    public BaseBiz()
    {
        LogUtil.d("dfy","enter BaseBiz");
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
    }

    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String strParam4,String[] strArr) {
            handleServerResult(intDataType, strDataType, strSetSN, strSetSN1,strAlmComType,strParam1,strParam2,strParam3,strParam4,strArr);
        }

    };




    protected abstract void  handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String strParam4,String[] strArr);
}
