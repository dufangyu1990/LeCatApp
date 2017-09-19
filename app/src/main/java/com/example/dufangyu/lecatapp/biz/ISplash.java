package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface ISplash extends BaseInterface{
    void checkVersion(String versionCode, SplashListener listener);
}
