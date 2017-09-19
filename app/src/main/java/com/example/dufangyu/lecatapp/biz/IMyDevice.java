package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/14.
 */

public interface IMyDevice extends BaseInterface{

    void getMyDevice(String loginName);

    void deleteDevice(String loginName,String deviceId);

}
