package com.example.dufangyu.lecatapp.socketUtils;


import com.example.dufangyu.lecatapp.CallBack.DataCallBack;
import com.example.dufangyu.lecatapp.CallBack.NetCallBack;
import com.example.dufangyu.lecatapp.CallBack.RealDataCallBack;
import com.example.dufangyu.lecatapp.utils.Constant;
import com.example.dufangyu.lecatapp.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.dufangyu.lecatapp.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPNONET;


/**
 * Created by dufangyu on 2016/11/1.
 */
public class TcpConnectUtil {
    private String p_strLinkTcpServerIP = Constant.TCPSERVERIP;
    private int p_intLinkTcpServerPort = Constant.TCPSERVERPORT;
    private int p_intStateCode = 0;//状态码
    private NetCallBack mCallBack;
    private  DataCallBack mDataCallBack;
    private RealDataCallBack mRealDatCallBack;
    public static boolean p_bLinkCenterON = false;
    private Socket p_TcpClientSocket = null;
    private OutputStream p_outputstream; // 发送流
    private InputStream p_inputstream; // 接收流
    private boolean p_bConnectSuccessON = false;// 控制是否连接成功过
    private boolean mRunning = true;
    private static TcpConnectUtil tcpInstance;
    // 数据接收
    private byte p_charDataServerBuf[] = new byte[16384];
    private int p_charDataCount = 0;
    private boolean p_bDataReON = false;
    private boolean p_b5CON = false;
    private int p_charCheckRe;
    // 发送数据
    private int p_intSeCount[][] = new int[100][3];// 数据类型,长度，回应
    private byte p_charSeData[][] = new byte[100][16384];
    private int p_intSeSequence = 0;

    public  static String p_strTempList[][] = new String[10000][40];//临时下载表
    public static int p_intTempCount = 0;
    private int p_intTempProCount = 0;

    private boolean threadRunning =false;

    public static TcpConnectUtil getTcpInstance()
    {
        if(tcpInstance ==null)
        {
            tcpInstance = new TcpConnectUtil();
        }
        return tcpInstance;
    }


    public void setNetCallBack(NetCallBack callback)
    {
        mCallBack = callback;
    }

    public void setDataCallBack(DataCallBack callback)
    {
        mDataCallBack = callback;
    }

    public DataCallBack getDataCallBack()
    {
        return mDataCallBack;
    }


    //开启一系列线程
    public  void startThreads()
    {

        if(!threadRunning)
        {
            LogUtil.d("dfy","开启线程");
            threadRunning = true;
            new LinkCenterth().start();// 连接线程
            new LinkCenterthWh().start();// 连接维护，35秒发一次链路数据
            new SendDatath().start();//发送数据线程
            new ReceDatath().start();// 接收线程
        }


    }

    public void setRealDatCallBack(RealDataCallBack mRealDatCallBack) {
        this.mRealDatCallBack = mRealDatCallBack;
    }

    // 发送数据线程
    class SendDatath extends Thread {
        public void run() {
            int i, j;
            byte[] charSeOneData = new byte[16384];
            while (mRunning) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (p_bLinkCenterON) {
                    try {
                        for (i = 0; i < 100; i++) {
                            if (p_intSeCount[i][2] == 1) {
                                for (j = 0; j < p_intSeCount[i][1]; j++) {
                                    charSeOneData[j] = p_charSeData[i][j];
                                }
                                p_outputstream.write(charSeOneData, 0,p_intSeCount[i][1]);
                                p_intSeCount[i][2] = 0;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 连接线程
    class LinkCenterth extends Thread {

        public void run() {
            if(mRunning)
            {
                while (true) {
                    try {
                        if (!p_bLinkCenterON) {
                            if (p_TcpClientSocket != null) {
                                p_TcpClientSocket.close();
                            }
                            p_TcpClientSocket = new Socket();
                            p_TcpClientSocket.connect(new InetSocketAddress(p_strLinkTcpServerIP, p_intLinkTcpServerPort),Constant.REQ_TIMEOUT);// 设置超时为10秒
                            p_outputstream = p_TcpClientSocket.getOutputStream();
                            p_inputstream = p_TcpClientSocket.getInputStream();
                            p_bLinkCenterON = true;
                            p_bConnectSuccessON = true;// 已经连接上过了
                        }
                    } catch (IOException e) {
                        p_bLinkCenterON = false;
                        //如果没连接成功过，就Toast提示网络异常，
                        //连接成功过，再报异常，就在接收线程中处理，不提示toast，用异常图标表示网络问题
                        if (!p_bConnectSuccessON) {
                            p_bConnectSuccessON = true;
                            p_intStateCode = TCPNONET;
                            if(mCallBack!=null)
                                mCallBack.onHandle(p_intStateCode);
                        }
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }




    class LinkCenterthWh extends Thread {
        public void run() {
            byte charSeOneData[] = new byte[1];
            while (mRunning) {
                try {
                    Thread.sleep(45000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendData(1101, charSeOneData, 0);
            }
        }
    }



    // 接收数据线程
    class ReceDatath extends Thread {
        public void run() {
            int i;
            int Buflen;
            byte[] Buf = new byte[16384];

            while (mRunning) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (p_bLinkCenterON) {
                    try {
                        Buflen = p_inputstream.read(Buf, 0, Buf.length);
                        if (Buflen > 0) {
                            // 接收数据
                            for (i = 0; i < Buflen; i++) {
                                if (p_bDataReON) {
                                    if (Buf[i] == 0x5c) {
                                        p_b5CON = true;
                                    } else {
                                        if (p_b5CON) {
                                            p_charDataServerBuf[p_charDataCount] = (byte) (Buf[i] ^ 0x50);
                                            p_b5CON = false;
                                        } else {
                                            p_charDataServerBuf[p_charDataCount] = Buf[i];
                                        }
                                        p_charCheckRe = p_charCheckRe
                                                ^ p_charDataServerBuf[p_charDataCount];
                                        p_charDataCount++;
                                    }
                                    if (p_charDataCount > 16383) {
                                        p_bDataReON = false;
                                        return;
                                    }
                                    if (Buf[i] == 0x5d) {
                                        int charOwnCheck = 0x00;
                                        charOwnCheck = p_charDataServerBuf[p_charDataCount - 2]
                                                ^ p_charDataServerBuf[p_charDataCount - 2]
                                                ^ p_charDataServerBuf[p_charDataCount - 1];
                                        if (p_charCheckRe == charOwnCheck) {
                                            ReClientData(p_charDataCount - 3);
                                        }
                                        p_bDataReON = false;
                                    }
                                }
                                if (Buf[i] == 0x5b) {
                                    p_charDataCount = 0;
                                    p_bDataReON = true;
                                    p_b5CON = false;
                                    p_charCheckRe = 0x00;
                                    p_charDataServerBuf[p_charDataCount] = Buf[i];
                                    p_charDataCount++;
                                }
                            }
                        }
                        if (Buflen == -1) {
                            // 网络断开
                            p_bLinkCenterON = false;
                            p_intStateCode = TCPDISLINK;
                            if(mCallBack!=null)
                                mCallBack.onHandle(p_intStateCode);
//                            myHandler.post(taskRunnable);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 发送数据组包
    public void SendData(int intDataType,byte[] charSeOneData, int intDataLen) {

        if (p_bLinkCenterON) {
            int i;
            int intSeSequence;
            int intTMDataLen = 0;
            int charCheckRe = 0x00;
            if (p_intSeSequence >= 100) {
                p_intSeSequence = 0;
            }
            intSeSequence=p_intSeSequence;
            p_intSeSequence++;

            p_intSeCount[intSeSequence][0] = intDataType;
            p_charSeData[intSeSequence][intTMDataLen] = 0x5b;
            intTMDataLen++;
            p_charSeData[intSeSequence][intTMDataLen] = (byte) (intDataType >> 8);
            charCheckRe = p_charSeData[intSeSequence][intTMDataLen];
            if (p_charSeData[intSeSequence][intTMDataLen] == 0x5b
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5c
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5d) {
                p_charSeData[intSeSequence][intTMDataLen + 1] = (byte) (p_charSeData[intSeSequence][intTMDataLen] ^ 0x50);
                p_charSeData[intSeSequence][intTMDataLen] = 0x5c;
                intTMDataLen++;
            }
            intTMDataLen++;
            p_charSeData[intSeSequence][intTMDataLen] = (byte) (intDataType & 0x00ff);
            charCheckRe = charCheckRe
                    ^ p_charSeData[intSeSequence][intTMDataLen];
            if (p_charSeData[intSeSequence][intTMDataLen] == 0x5b
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5c
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5d) {
                p_charSeData[intSeSequence][intTMDataLen + 1] = (byte) (p_charSeData[intSeSequence][intTMDataLen] ^ 0x50);
                p_charSeData[intSeSequence][intTMDataLen] = 0x5c;
                intTMDataLen++;
            }
            intTMDataLen++;
            p_charSeData[intSeSequence][intTMDataLen] = (byte) (intSeSequence >> 8);
            charCheckRe = charCheckRe
                    ^ p_charSeData[intSeSequence][intTMDataLen];
            if (p_charSeData[intSeSequence][intTMDataLen] == 0x5b
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5c
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5d) {
                p_charSeData[intSeSequence][intTMDataLen + 1] = (byte) (p_charSeData[intSeSequence][intTMDataLen] ^ 0x50);
                p_charSeData[intSeSequence][intTMDataLen] = 0x5c;
                intTMDataLen++;
            }
            intTMDataLen++;
            p_charSeData[intSeSequence][intTMDataLen] = (byte) (intSeSequence & 0x00ff);
            charCheckRe = charCheckRe
                    ^ p_charSeData[intSeSequence][intTMDataLen];
            if (p_charSeData[intSeSequence][intTMDataLen] == 0x5b
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5c
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5d) {
                p_charSeData[intSeSequence][intTMDataLen + 1] = (byte) (p_charSeData[intSeSequence][intTMDataLen] ^ 0x50);
                p_charSeData[intSeSequence][intTMDataLen] = 0x5c;
                intTMDataLen++;
            }
            intTMDataLen++;
            for (i = 0; i < intDataLen; i++) {
                p_charSeData[intSeSequence][intTMDataLen] = charSeOneData[i];
                charCheckRe = charCheckRe
                        ^ p_charSeData[intSeSequence][intTMDataLen];
                if (p_charSeData[intSeSequence][intTMDataLen] == 0x5b
                        || p_charSeData[intSeSequence][intTMDataLen] == 0x5c
                        || p_charSeData[intSeSequence][intTMDataLen] == 0x5d) {
                    p_charSeData[intSeSequence][intTMDataLen + 1] = (byte) (p_charSeData[intSeSequence][intTMDataLen] ^ 0x50);
                    p_charSeData[intSeSequence][intTMDataLen] = 0x5c;
                    intTMDataLen++;
                }
                intTMDataLen++;
            }
            p_charSeData[intSeSequence][intTMDataLen] = (byte) charCheckRe;
            if (p_charSeData[intSeSequence][intTMDataLen] == 0x5b
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5c
                    || p_charSeData[intSeSequence][intTMDataLen] == 0x5d) {
                p_charSeData[intSeSequence][intTMDataLen + 1] = (byte) (p_charSeData[intSeSequence][intTMDataLen] ^ 0x50);
                p_charSeData[intSeSequence][intTMDataLen] = 0x5c;
                intTMDataLen++;
            }
            intTMDataLen++;
            p_charSeData[intSeSequence][intTMDataLen] = 0x5d;
            intTMDataLen++;
            p_intSeCount[intSeSequence][1] = intTMDataLen;
            if (intDataType == 111 || intDataType == 1101) //链路数据
            {
                p_intSeCount[intSeSequence][2] = 0;
                try {
                    p_outputstream.write(p_charSeData[intSeSequence], 0, p_intSeCount[intSeSequence][1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                p_intSeCount[intSeSequence][2] = 1;//指令数据
            }
        }
    }



    //数据接收
    private void ReClientData(int intDataLen) {
        int intDataType;
        intDataType = p_charDataServerBuf[1];
        intDataType = intDataType << 8 | p_charDataServerBuf[2];
        if (intDataType < 1000) {
            if (intDataType == 101 || intDataType == 111) {
            } else {
                ReCommData(intDataType, intDataLen);
            }
        } else {
            if (intDataType == 1100) {
                // 连接成功
                p_intStateCode = TCPLINK;
//                LogUtil.d("dfy","mCallBack = "+mCallBack);
                if(mCallBack!=null)
                    mCallBack.onHandle(p_intStateCode);
//                myHandler.post(taskRunnable);
            } else {
                if (intDataType == 1114) {
                } else {
                    ReCommData(intDataType, intDataLen);
                }
            }
        }
    }



    private void ReCommData(int intDataType, int intDataLen) {
        String strReData = "";
        try {
            strReData = new String(p_charDataServerBuf, 5, intDataLen - 4,
                    "GB2312");// 转为可见汉字
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strReStem[] = strReData.split("\\^", -1);// -1存空值
        if (strReStem.length == 19) {
            ClintReceBcCommData(intDataType, strReStem[0], strReStem[1], strReStem[2], strReStem[3],
                    strReStem[4], strReStem[5], strReStem[6], strReStem[7], strReStem[8], strReStem[9],
                    strReStem[10], strReStem[11], strReStem[12], strReStem[13], strReStem[14],
                    strReStem[15], strReStem[16], strReStem[17],strReStem);
        }
    }


    //清理临时表
    public void IntiTemp() {
        int i;
        if(p_intTempCount!=0){
            for(i=0;i<p_intTempCount;i++){
                p_strTempList[i][0]="";
            }
        }else{
            for(i=0;i<p_intTempProCount;i++){
                p_strTempList[i][0]="";
            }
        }
        p_intTempCount = 0;
        p_intTempProCount = 0;
    }


    /**
     * 1106接收的是数据条数，当数据条数为0时，后台不会产生1107事件。
     * 当有数据时，1107和1106可能会互相穿插发生，所以两者里面都做了条数判断
     * 一个符合条件时就会触发回调事件，另一个就不会再继续走了
     * @param intDataType
     * @param strDataType
     * @param strSetType
     * @param strSetSN
     * @param strSetSN1
     * @param strAlmComType
     * @param strHisType
     * @param strPosType
     * @param strFadeType
     * @param strRecogType
     * @param strRecogType1
     * @param strParam1
     * @param strParam2
     * @param strParam3
     * @param strParam4
     * @param strParam5
     * @param strParam6
     * @param strParam7
     * @param strParam8
     */

    public void ClintReceBcCommData(int intDataType, String strDataType, String strSetType, String strSetSN,
                                    String strSetSN1, String strAlmComType, String strHisType, String strPosType,
                                    String strFadeType, String strRecogType, String strRecogType1, String strParam1,
                                    String strParam2, String strParam3, String strParam4, String strParam5, String strParam6,
                                    String strParam7, String strParam8,String [] strArr)
    {
        int i,j;

//        LogUtil.d("dfy","intDataType = "+intDataType+",strDataType = "+strDataType);
        if (intDataType == 1106) {
            p_intTempCount = Integer.parseInt(strParam1);
            if (p_intTempCount == 0) {
//                LogUtil.d("dfy","mDataCallBack = "+mDataCallBack);
                if(mDataCallBack!=null)
                    mDataCallBack.onReceiveResult(intDataType,  strDataType,  strSetType,  strSetSN,
                         strSetSN1,  strAlmComType,  strHisType,  strPosType,
                         strFadeType,  strRecogType,  strRecogType1,  strParam1,
                         strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
                         strParam7,  strParam8,strArr);
            } else {
                if (p_intTempProCount >= p_intTempCount)
                {
                    p_intTempProCount=0;
//                    LogUtil.d("dfy","mDataCallBack = "+mDataCallBack);
                    if(mDataCallBack!=null)
                        mDataCallBack.onReceiveResult(intDataType,  strDataType,  strSetType,  strSetSN,
                                strSetSN1,  strAlmComType,  strHisType,  strPosType,
                                strFadeType,  strRecogType,  strRecogType1,  strParam1,
                                strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
                                strParam7,  strParam8,strArr);
                }
            }
        }else if (intDataType == 1107) {
            int intTempCount;
            String strTempStem[];
            String strTempOne[];
            if(strDataType.equals("34"))
            {
                strParam1=strParam1.replaceAll("Y&", "Y*");
                strParam1=strParam1.replaceAll("I&", "I*");
            }
            strTempStem = strParam1.split("\\&", -1);// -1存空值
            for (i = 0; i < strTempStem.length - 1; i++) {
                strTempOne = strTempStem[i].split("\\τ", -1);
                intTempCount = Integer.parseInt(strTempOne[0]);
                if (p_strTempList[intTempCount][0] == null) {
                    p_intTempProCount++;
                    for(j=0;j<strTempOne.length;j++){
                        p_strTempList[intTempCount][j] = strTempOne[j];
                    }
                }
                else{
                    if (p_strTempList[intTempCount][0].equals("")){
                        p_intTempProCount++;
                        for(j=0;j<strTempOne.length;j++){
                            p_strTempList[intTempCount][j] = strTempOne[j];
                        }
                    }
                }
            }
            if (p_intTempProCount >= p_intTempCount && p_intTempCount>0) {
                p_intTempProCount=0;
//                LogUtil.d("dfy","mDataCallBack = "+mDataCallBack);
                if(mDataCallBack!=null)
                    mDataCallBack.onReceiveResult(intDataType,  strDataType,  strSetType,  strSetSN,
                            strSetSN1,  strAlmComType,  strHisType,  strPosType,
                            strFadeType,  strRecogType,  strRecogType1,  strParam1,
                            strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
                            strParam7,  strParam8,strArr);
//                TempJudge(strDataType,strParam2);
            }
        }else{
//            LogUtil.d("dfy","mDataCallBack = "+mDataCallBack);
//            LogUtil.d("dfy","intDataType = "+intDataType);
            if(mDataCallBack!=null)
                mDataCallBack.onReceiveResult(intDataType,  strDataType,  strSetType,  strSetSN,
                        strSetSN1,  strAlmComType,  strHisType,  strPosType,
                        strFadeType,  strRecogType,  strRecogType1,  strParam1,
                        strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
                        strParam7,  strParam8,strArr);



//            if(intDataType ==2160)//实时数据单独处理
//            {
//                if(mRealDatCallBack!=null)
//                {
//                    mRealDatCallBack.onReceiveResult(intDataType,  strDataType,  strSetType,  strSetSN,
//                            strSetSN1,  strAlmComType,  strHisType,  strPosType,
//                            strFadeType,  strRecogType,  strRecogType1,  strParam1,
//                            strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
//                            strParam7,  strParam8,strArr);
//                }
//            }else{
//                //不论什么数据类型，都进入此方法，不用每个都列出来
//                if(mDataCallBack!=null)
//                    mDataCallBack.onReceiveResult(intDataType,  strDataType,  strSetType,  strSetSN,
//                            strSetSN1,  strAlmComType,  strHisType,  strPosType,
//                            strFadeType,  strRecogType,  strRecogType1,  strParam1,
//                            strParam2,  strParam3,  strParam4,  strParam5,  strParam6,
//                            strParam7,  strParam8,strArr);
//            }

        }


//        if(intDataType == 1105)
//        {
////            LogUtil.d("dfy","mDataCallBack = "+mDataCallBack);
//            if(mDataCallBack!=null)
//                mDataCallBack.onReceiveResult(intDataType,strDataType,strSetSN,strSetSN1,strAlmComType,strParam1,strParam2,strParam3);
//        }
//
//        if(intDataType == 1109)
//        {
//            if(mDataCallBack!=null)
//                mDataCallBack.onReceiveResult(intDataType,strDataType,strSetSN,strSetSN1,strAlmComType,strParam1,strParam2,strParam3);
//        }
//
//        if(intDataType == 1150)
//        {
//            if(mDataCallBack!=null)
//                mDataCallBack.onReceiveResult(intDataType,strDataType,strSetSN,strSetSN1,strAlmComType,strParam1,strParam2,strParam3);
//        }
//
//        if(intDataType == 1108)
//        {
//            if(mDataCallBack!=null)
//                mDataCallBack.onReceiveResult(intDataType,strDataType,strSetSN,strSetSN1,strAlmComType,strParam1,strParam2,strParam3);
//        }

    }


    public void ClintSendBcCommData(int intDataType, String strDataType, String strSetType, String strSetSN,
                                    String strSetSN1, String strAlmComType, String strHisType, String strPosType,
                                    String strFadeType, String strRecogType, String strRecogType1, String strParam1,
                                    String strParam2, String strParam3, String strParam4, String strParam5, String strParam6,
                                    String strParam7, String strParam8) {

        int intDataLen;
        byte charSeOneData[] = new byte[16384];
        try {

            String strData = strDataType+"^"+strSetType+"^"+strSetSN+"^"+strSetSN1+"^"+strAlmComType+"^"+strHisType
                    +"^"+strPosType+"^"+strFadeType+"^"+strRecogType+"^"+strRecogType1+"^"+strParam1
                    +"^"+strParam2+"^"+strParam3+"^"+strParam4+"^"+strParam5+"^"+strParam6+"^"+strParam7
                    +"^"+strParam8+"^";
            charSeOneData = strData.getBytes("GB2312");// 转为GB2312编码
            intDataLen = charSeOneData.length;
            SendData(intDataType, charSeOneData, intDataLen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
