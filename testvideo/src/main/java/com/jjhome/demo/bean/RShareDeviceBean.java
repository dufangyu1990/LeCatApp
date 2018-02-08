package com.jjhome.demo.bean;

/**
 * Author: LuoQ
 * DATE: 2017/12/6
 */

public class RShareDeviceBean {
    //    {"pushserver_ip":"120.24.15.27","service_port":9966,"push_port":9999,"errcode":0,"user_id":"830"}
    private String pushserver_ip;
    private int service_port;
    private int push_port;
    private int errcode;
    private String user_id;

    public String getPushserver_ip() {
        return pushserver_ip;
    }

    public void setPushserver_ip(String pushserver_ip) {
        this.pushserver_ip = pushserver_ip;
    }

    public int getService_port() {
        return service_port;
    }

    public void setService_port(int service_port) {
        this.service_port = service_port;
    }

    public int getPush_port() {
        return push_port;
    }

    public void setPush_port(int push_port) {
        this.push_port = push_port;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
