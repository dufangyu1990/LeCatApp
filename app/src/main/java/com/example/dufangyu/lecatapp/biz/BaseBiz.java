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
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
            handleServerResult(intDataType,  strDataType,  strSetType,  strSetSN,
                    strSetSN1,  strAlmComType,  strHisType,  strPosType,
                    strFadeType,  strRecogType,  strRecogType1,  strParam1,
                    strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
                    strParam7,  strParam8,strArr);
        }


    };




    protected abstract void  handleServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr);
}
