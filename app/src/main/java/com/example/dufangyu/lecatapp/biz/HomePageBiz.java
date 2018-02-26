package com.example.dufangyu.lecatapp.biz;


import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.LogUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class HomePageBiz extends BaseBiz implements IHomePage {
    private HomePageListener listener;

    private StringBuffer temBuf= new StringBuffer();
    public HomePageBiz(HomePageListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void get4GPushData() {
        temBuf.setLength(0);
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
//        LogUtil.d("dfy","temBuf = "+temBuf.toString());
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




    @Override
    public void check4GData() {
        temBuf.setLength(0);
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
//        LogUtil.d("dfy","temBuf = "+temBuf.toString());
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2160, "0001", "101", temBuf.toString(), "", "", "", "", "", "", "", "","" , "", "", "", "", "", "");
    }

    /**
     * 发送监听指令
     */
    @Override
    public void sendJTOrder(String phoneNumber) {
        temBuf.setLength(0);
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            temBuf.append(DataManager.p_strDeviceList[i][1]).append("&");
        }
//        LogUtil.d("dfy","temBuf = "+temBuf.toString());
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2160, "0003", "101", temBuf.toString(), "", "", "", "", "", "", "", phoneNumber,"" , "", "", "", "", "", "");
    }


    @Override
    protected void handleServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
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
        }else if(intDataType ==2160)
        {
            //收到巡检指令返回
            if(strDataType.equals("1001"))
            {
                LogUtil.d("dfy","4g网关回传数据");
                if(listener!=null)
                    listener.getCheck4GData(strParam1,strParam2,strParam3,strParam4);
            }
        }
    }

    @Override
    public void detachDataCallBackNull() {
        TcpConnectUtil.getTcpInstance().setDataCallBack(null);
    }

}
