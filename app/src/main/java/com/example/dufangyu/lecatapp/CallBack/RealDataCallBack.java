package com.example.dufangyu.lecatapp.CallBack;

/**
 * Created by dufangyu on 2016/11/1.
 *  实时数据主要作用是回调到UI线程,处理服务器返回的数据
 */
public abstract class RealDataCallBack {

    public abstract void onReceiveResult(int intDataType, String strDataType, String strSetType, String strSetSN,
                                         String strSetSN1, String strAlmComType, String strHisType, String strPosType,
                                         String strFadeType, String strRecogType, String strRecogType1, String strParam1,
                                         String strParam2, String strParam3, String strParam4, String strParam5, String strParam6,
                                         String strParam7, String strParam8,String [] strArr);

}
