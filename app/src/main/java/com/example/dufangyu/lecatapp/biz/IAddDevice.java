package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/13.
 */

public interface IAddDevice {

    void addDevice(String loginName,String deviceId, String nickName);
    void getMyDeviceAgain(String loginName);
}
