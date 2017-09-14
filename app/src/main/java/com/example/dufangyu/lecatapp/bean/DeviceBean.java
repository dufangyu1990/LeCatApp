package com.example.dufangyu.lecatapp.bean;

import java.io.Serializable;

/**
 * Created by dufangyu on 2017/9/14.
 */

public class DeviceBean implements Serializable{

    private String deviceId;
    private String nickName;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
