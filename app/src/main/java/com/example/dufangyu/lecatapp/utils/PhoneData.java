package com.example.dufangyu.lecatapp.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.dufangyu.lecatapp.activity.MyApplication;


public class PhoneData {

    public static void initMobileInfo() {
    	//获取手机型号
        String mobileBrand = android.os.Build.MODEL;
        MyApplication.getInstance().setStringPerference("mobileBrand", mobileBrand.replace(" ", "-"));
        //获取应用版本号
        try {
            PackageManager packageManager = MyApplication.getInstance().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            String version = packInfo.versionCode+"";
//            LogUtil.d("dfy"," local version = "+version);
                   // packInfo.versionName;
            if (version!=null){
                MyApplication.getInstance().setStringPerference("appVersionNo", version);
                MyApplication.getInstance().setStringPerference("versionName",packInfo.versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        }
    }
