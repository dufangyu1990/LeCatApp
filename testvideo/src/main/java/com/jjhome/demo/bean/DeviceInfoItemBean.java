package com.jjhome.demo.bean;

public class DeviceInfoItemBean {
	public int p2pserverPort;
	public String devicePass;
	public String type;
	public String deviceId;
	public String deviceName;
	public String p2pserverIp;

	@Override
	public String toString() {
		return "DeviceInfoBean{" +
				"device_id='" + deviceId + '\'' +
				", p2pserver_port=" + p2pserverPort +
				", device_pass='" + devicePass + '\'' +
				", type='" + type + '\'' +
				", device_name='" + deviceName + '\'' +
				", p2pserver_ip='" + p2pserverIp + '\'' +
				'}';
	}
}
