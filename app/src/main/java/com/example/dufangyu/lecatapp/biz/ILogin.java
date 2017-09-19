package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/8/30.
 */

public interface ILogin extends BaseInterface{

     void login(String loginName, String password);
     void getDeviceList(String loginName);
}
