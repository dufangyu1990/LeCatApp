package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/8/30.
 */

public interface LoginListener {
    void loginSuccess(String code);
    void loginFailed();
}
