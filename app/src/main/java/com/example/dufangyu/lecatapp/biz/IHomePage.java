package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface IHomePage extends BaseInterface{

    //获取设备最后数据
    void get4GPushData();

    //重新登录
    void reEnterIn(String loginName, String password);

    void getDeviceList(String loginName);



    //发送巡检指令
    void check4GData();


}
