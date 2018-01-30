package com.example.dufangyu.lecatapp.biz;

import com.example.dufangyu.lecatapp.bean.RealData;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface HomePageListener {
    void refreshUI(RealData realData);
    //重新登录成功
    void reEnterSuccess(String code,String author,String userName,String phoneCall,String address);
    //重新登录失败
    void reEnterFail();

    void getDeviceList();
    //发送灯控指令成功
    void send_lightControlSuccess();
}
