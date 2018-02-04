package com.example.dufangyu.lecatapp.CallBack;

/**
 * Created by dufangyu on 2016/11/1.
 *  主要作用是回调到UI线程,处理服务器返回的数据
 */
public abstract class DataCallBack {

    public abstract void onReceiveResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String strParam4,String[] strArr);

}
