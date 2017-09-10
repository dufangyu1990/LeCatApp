package com.example.dufangyu.lecatapp.manager;


import static com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil.p_intTempCount;
import static com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil.p_strTempList;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class DataManager {
    private static DataManager managerInstance;
    // 部门表
    public static String p_strDepList[][] = new String[6000][3];
    public static int p_intDepCount = 0;

    public static DataManager getManagerInstance()
    {
        if(managerInstance ==null)
        {
            managerInstance = new DataManager();
        }
        return managerInstance;
    }


    //部门一级表
    public void saveDepListData()
    {
        for(int i=0;i<p_intTempCount;i++)
        {
            p_strDepList[p_intDepCount][0]=p_strTempList[i][0];
            p_strDepList[p_intDepCount][1]=p_strTempList[i][1];
            p_strDepList[p_intDepCount][2]=p_strTempList[i][2];
            p_intDepCount++;
        }
    }

    //退出登录的时候重置数据
    public void resetData()
    {
        //清空部门表
        if(p_intDepCount!=0){
            for(int i=0;i<p_intDepCount;i++){
                p_strDepList[i][0]="";
            }
        }
        p_intDepCount = 0;
    }


}
