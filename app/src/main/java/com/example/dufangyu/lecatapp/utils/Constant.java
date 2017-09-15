package com.example.dufangyu.lecatapp.utils;

import android.os.Environment;


public class Constant {
	 public static final int TOKENTIME=60;
	 public static final String DATAPATHROOT= Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
	 public static final String TXT_OF_SET = "/set.txt";//

	//socket连接的服务器地
	public final static String TCPSERVERIP = "115.28.75.240";//219.236.247.110(测试服务器)//123.57.217.93(主网)//192.168.1.187//115.28.75.240
	public final static  int TCPSERVERPORT = 5258;

	public final static  String APKURL = "http://123.57.217.93:8080/Android/NewGpsTest.apk";

	public static final int REQ_TIMEOUT = 35000;
	public static final int TCPNONET = 100;//app一进来就没网，100
	public static final int TCPDISLINK = 101;//中途突然没网 101
	public static final int TCPLINK = 102;//连接服务器成功 102

	public static final String DEVICE_TYPE="101";//网关设备类型

	public static final int HUMiDITY_BELOW = 30;
	public static final int HUMiDITY_MID = 60;

	public static final int TEMPERATURE_BELOW = 18;
	public static final int TEMPERATURE_HIGH = 28;


}
