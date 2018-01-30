package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.LogUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class HomePageBiz extends RealBaseBiz implements IHomePage {
    private HomePageListener listener;

    private StringBuffer temBuf= new StringBuffer();
    public HomePageBiz(HomePageListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void get4GPushData() {
        temBuf.setLength(0);
//        StringBuffer temBuf = new StringBuffer();
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
        LogUtil.d("dfy","temBuf = "+temBuf.toString());
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(2150, "0002", "1", temBuf.toString(), "", "", "", "", "", "", "", "","" , "", "", "", "", "", "");

    }

    @Override
    public void reEnterIn(String loginName, String password) {
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0002", "", "", "", "", "", "", "", "", "", loginName,password, "", "", "", "", "", "");
    }

    @Override
    public void getDeviceList(String loginName) {
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1107, "0001", "", "", "", "", "", "", "", "", "", loginName, "", "", "", "", "", "", "");
    }
    /**
     * 发送灯控指令
     * @param light_type
     * 0关灯
     * 1开灯红
     * 2开灯绿
     * 3开灯蓝
     */
    @Override
    public void sendLightCommand(String light_type) {
        temBuf.setLength(0);
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
        LogUtil.d("dfy","temBuf = "+temBuf.toString());
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2160, "0002", "101", temBuf.toString(), "", "", "", "", "", "", "", light_type,"" , "", "", "", "", "", "");


    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3,String[] strArr) {
//        LogUtil.d("dfy","设备Id = "+strArr[2]+",报警状态 = "+strArr[4]+",更新时间 = "+strArr[8]);
//        LogUtil.d("dfy","温度值 = "+strArr[10]+",湿度值 = "+strArr[11]);
        if(intDataType==2150)
        {
            if(strDataType.equals("1002"))
            {
                RealData realData = new RealData();
                realData.setDeviceId(strArr[2]);
                realData.setAlarmState(strArr[4]);
                realData.setUpdateTime(strArr[8]);
                realData.setSonDeviceType(strArr[9]);
                realData.setTemperatureValue(strArr[10]);
                realData.setHumidityValueValue(strArr[11]);
                listener.refreshUI(realData);
            }
        }else if(intDataType==1105)
        {
            if(strDataType.equals("1002"))
            {
                if(strParam1.equals("0"))//登录失败
                {
                    listener.reEnterFail();
                }else if(strParam1.equals("1"))//登录成功
                {
                    //大参数2:管理部门码  个人用户管理码为空
                    //大参数3:管理权限 (后续定义).
                    //大参数4: 用户姓名
                    //大参数5: 联系电话
                    //大参数6:用户地址
                    listener.reEnterSuccess(strParam2,strParam3,strArr[13],strArr[14],strArr[15]);
                }
            }

        }else if(intDataType ==1106||intDataType ==1107)
        {
            if(strDataType.equals("1001"))
            {
                DataManager.getManagerInstance().saveDeviceListData();
                if(listener!=null)
                    listener.getDeviceList();
            }
        }else if(intDataType ==2106)
        {
            if(strDataType.equals("1002"))
            {
                if(strParam1.equals("1"))//灯控指令提交成功
                {
                    if(listener!=null)
                        listener.send_lightControlSuccess();
                }
            }
        }
    }



}
