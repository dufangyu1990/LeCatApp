package com.example.dufangyu.lecatapp.manager;


import static com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil.p_intTempCount;
import static com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil.p_strTempList;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class DataManager {
    private static DataManager managerInstance;
    //设备表
    public static String p_strDeviceList[][] = new String[100][4];
    public static int p_intDeviceCount = 0;




    public static DataManager getManagerInstance()
    {
        if(managerInstance ==null)
        {
            managerInstance = new DataManager();
        }
        return managerInstance;
    }





    //保存设备表
    public void saveDeviceListData()
    {

        resetData();
        for(int i=0;i<p_intTempCount;i++)
        {
            p_strDeviceList[p_intDeviceCount][0]=p_strTempList[i][0];
            p_strDeviceList[p_intDeviceCount][1]=p_strTempList[i][1];
            p_strDeviceList[p_intDeviceCount][2]=p_strTempList[i][2];
            p_strDeviceList[p_intDeviceCount][3]=p_strTempList[i][3];
            p_intDeviceCount++;
        }
    }

    //退出登录的时候重置数据
    public void resetData()
    {
        //清空设备表
        if(p_intDeviceCount!=0){
            for(int i=0;i<p_intDeviceCount;i++){
                p_strDeviceList[i][0]="";
            }
        }
        p_intDeviceCount = 0;
    }


}
