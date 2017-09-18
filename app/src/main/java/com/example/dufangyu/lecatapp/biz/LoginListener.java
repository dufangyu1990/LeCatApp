package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/8/30.
 */

public interface LoginListener {
    void loginSuccess(String code,String author,String userName,String phoneCall,String address);
    void loginFailed();
    void getDeviceList();
}
