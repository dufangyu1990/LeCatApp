package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface IHomePage {

    void get4GPushData();

    //重新登录
    void reEnterIn(String loginName, String password);

    void getDeviceList(String loginName);


}
