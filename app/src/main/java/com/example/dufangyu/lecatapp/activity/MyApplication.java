package com.example.dufangyu.lecatapp.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dufangyu.lecatapp.bean.LoginBean;
import com.example.dufangyu.lecatapp.utils.PhoneData;
import com.example.jjhome.network.DeviceUtils;
import com.example.jjhome.network.TestEvent;
import com.jjhome.master.http.MasterRequest;


/**
 * Created by dufangyu on 2017/6/12.
 */

public class MyApplication extends Application{

    private static MyApplication instance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor meditor;

    //-----------------------------------------------
    //以下是视频demo所需
    public LoginBean loginBean;
    private static TestEvent event;
    private static Bundle bundle;
    public static MasterRequest masterRequest;
    //    public static final String APP_ID = "70dd93b26c89e26623c71cedf845e183";//请通过商务查询自己的APP_ID
    public static final String APP_ID = "a76012a3d16d1bb864bb6f47fd00f292";//请通过商务查询自己的APP_ID




    @Override
    public void onCreate() {
        super.onCreate();
        instance= this;
        preferences =instance.getSharedPreferences("settingfile", MODE_PRIVATE);
        meditor = preferences.edit();
        PhoneData.initMobileInfo();


        DeviceUtils.init(this);
        masterRequest = new MasterRequest(this);

    }


    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }


    public static MasterRequest getMasterRequest() {
        return masterRequest;
    }

    public static Context getContext() {
        return instance;
    }

    public static MyApplication getInstance(){
        return instance;
    }


    public String getStringPerference(String key)
    {
        return preferences.getString(key,"");
    }

    public void setStringPerference(String key,String value)
    {
        meditor.putString(key,value).commit();
    }

    public void setlongPreference(String key,long value)
    {
        meditor.putLong(key, value).commit();
    }

    public  long getLongPerference(String key)
    {
        return preferences.getLong(key,0L);
    }
    public void setBooleanPerference(String key,boolean value)
    {
        meditor.putBoolean(key,value).commit();
    }

    public  boolean getBooleanPerference(String key)
    {
        return preferences.getBoolean(key,false);
    }
}
