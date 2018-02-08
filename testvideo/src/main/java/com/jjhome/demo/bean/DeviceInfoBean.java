package com.jjhome.demo.bean;


public class DeviceInfoBean {
	public int errcode;
	public DeviceInfoItemBean deviceItem;
	public String errinfo;

	@Override
	public String toString() {
		return "DeviceInfoBean{" +
				"errcode=" + errcode +
				", itemBean=" + deviceItem.toString() +
				", errinfo='" + errinfo + '\'' +
				'}';
	}
}
