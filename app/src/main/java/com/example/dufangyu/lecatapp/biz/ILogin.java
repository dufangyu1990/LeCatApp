package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/8/30.
 */

public interface ILogin {

     void login(String loginName, String password, LoginListener listener);
}
