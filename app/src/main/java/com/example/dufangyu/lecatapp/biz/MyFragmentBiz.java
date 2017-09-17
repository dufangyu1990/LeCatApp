package com.example.dufangyu.lecatapp.biz;


import android.content.Context;

import com.example.dufangyu.lecatapp.manager.DataManager;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.BroadCastControll;

/**
 * Created by dufangyu on 2017/9/6.
 */

public class MyFragmentBiz  implements IMyFragment{







    @Override
    public void exitApp(Context context) {
        //发送注销登录指令给服务器
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData(1105, "0004", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        DataManager.getManagerInstance().resetData();
        BroadCastControll.removeAllReciver(context);

    }



}
