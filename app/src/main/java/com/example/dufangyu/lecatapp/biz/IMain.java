package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface IMain {

    void sendLoginCommad(String loginName, String password);
    void getDeviceList(String loginName);

}
