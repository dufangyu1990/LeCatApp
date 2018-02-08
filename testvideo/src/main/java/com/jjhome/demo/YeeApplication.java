package com.jjhome.demo;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.TestEvent;
import com.jjhome.demo.bean.LoginBean;
import com.jjhome.master.http.MasterRequest;


public class YeeApplication extends Application {

    public static YeeApplication instance;
    public LoginBean loginBean;
    private static TestEvent event;
    private static Bundle bundle;
    public static MasterRequest masterRequest;
//    public static final String APP_ID = "70dd93b26c89e26623c71cedf845e183";//请通过商务查询自己的APP_ID
    public static final String APP_ID = "a76012a3d16d1bb864bb6f47fd00f292";//请通过商务查询自己的APP_ID

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DeviceUtils.init(this);
        masterRequest = new MasterRequest(this);
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public static MasterRequest getMasterRequest() {
        return masterRequest;
    }

    // 单例模式中获取唯一的Application 实例
    public static YeeApplication getInstance() {
        if (null == instance) {
            instance = new YeeApplication();
        }
        return instance;
    }

    public static Context getContext() {
        return instance;
    }
}
