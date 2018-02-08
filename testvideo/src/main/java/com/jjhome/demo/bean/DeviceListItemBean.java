package com.jjhome.demo.bean;

import java.io.Serializable;

public class DeviceListItemBean implements Serializable {
    public String device_name;
    public String device_pass;
    public String device_id;
    public boolean share;
    public String shared_by;
    public int p2pserver_port;
    public String p2pserver_ip;
    public String type;
    public int live_broadcast;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_pass() {
        return device_pass;
    }

    public void setDevice_pass(String device_pass) {
        this.device_pass = device_pass;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public String getShared_by() {
        return shared_by;
    }

    public void setShared_by(String shared_by) {
        this.shared_by = shared_by;
    }

    public int getP2pserver_port() {
        return p2pserver_port;
    }

    public void setP2pserver_port(int p2pserver_port) {
        this.p2pserver_port = p2pserver_port;
    }

    public String getP2pserver_ip() {
        return p2pserver_ip;
    }

    public void setP2pserver_ip(String p2pserver_ip) {
        this.p2pserver_ip = p2pserver_ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLive_broadcast() {
        return live_broadcast;
    }

    public void setLive_broadcast(int live_broadcast) {
        this.live_broadcast = live_broadcast;
    }

    @Override
    public String toString() {
        return "DeviceListItemBean{" +
                "device_id='" + device_id + '\'' +
                ", device_name='" + device_name + '\'' +
                ", device_pass='" + device_pass + '\'' +
                ", share=" + share +
                ", shared_by='" + shared_by + '\'' +
                ", p2pserver_port=" + p2pserver_port +
                ", p2pserver_ip='" + p2pserver_ip + '\'' +
                ", type='" + type + '\'' +
                ", live_broadcast=" + live_broadcast +
                '}';
    }
}
