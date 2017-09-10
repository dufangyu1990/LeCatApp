package com.example.dufangyu.lecatapp.activity;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.dufangyu.lecatapp.utils.PhoneData;


/**
 * Created by dufangyu on 2017/6/12.
 */

public class MyApplication extends Application{

    private static MyApplication instance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor meditor;


    @Override
    public void onCreate() {
        super.onCreate();
//        initOkHttpFinal();
        instance= this;
        preferences =instance.getSharedPreferences("settingfile", MODE_PRIVATE);
        meditor = preferences.edit();
        PhoneData.initMobileInfo();
    }


//    private void initOkHttpFinal()
//    {
//        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
//                .setTimeout(Constant.REQ_TIMEOUT);
//        OkHttpFinal.getInstance().init(builder.build());
//    }

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
