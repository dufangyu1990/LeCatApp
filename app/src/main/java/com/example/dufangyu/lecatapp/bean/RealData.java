package com.example.dufangyu.lecatapp.bean;

import java.io.Serializable;

/**
 * Created by dufangyu on 2017/9/15.
 */

public class RealData implements Serializable{

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 报警状态
     */
    private String alarmState;
    /**
     * 最后一次更新时间
     */
    private String updateTime;
    /**
     * 下属设备（4g网关周边设备）
     */
    private String sonDeviceType;
    /**
     * 温度值
     */
    private String temperatureValue;
    /**
     * 湿度值
     */
    private String humidityValueValue;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSonDeviceType() {
        return sonDeviceType;
    }

    public void setSonDeviceType(String sonDeviceType) {
        this.sonDeviceType = sonDeviceType;
    }

    public String getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public String getHumidityValueValue() {
        return humidityValueValue;
    }

    public void setHumidityValueValue(String humidityValueValue) {
        this.humidityValueValue = humidityValueValue;
    }
}
