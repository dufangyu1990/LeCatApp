package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2018/2/4.
 */

public interface ILightBiz extends BaseInterface{
    //发送灯控指令
    void sendLightCommand(String light_type);
}
