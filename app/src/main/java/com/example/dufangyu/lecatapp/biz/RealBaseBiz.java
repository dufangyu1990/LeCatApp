package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.CallBack.RealDataCallBackImp;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;

/**
 * Created by dufangyu on 2017/9/13.
 */

public abstract class RealBaseBiz {

    public RealBaseBiz()
    {
        TcpConnectUtil.getTcpInstance().setRealDatCallBack(serverCallBack);
    }

    private RealDataCallBackImp serverCallBack = new RealDataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String strParam4,String[] strArr) {
            handleServerResult(intDataType, strDataType, strSetSN, strSetSN1,strAlmComType,strParam1,strParam2,strParam3,strParam4,strArr);
        }

    };




    protected abstract void  handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String strParam4,String[] strArr);
}
