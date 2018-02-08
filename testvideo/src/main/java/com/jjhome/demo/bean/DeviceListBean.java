package com.jjhome.demo.bean;


import java.util.List;

public class DeviceListBean {
	public int errcode;
	public List<DeviceListItemBean> device_list;
	public String errinfo;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < device_list.size(); i++) {
			sb.append(i+":");
			sb.append(device_list.get(i).toString());
			sb.append(" \n");
		}
		return "DeviceListBean{" +
				"device_list=" + sb.toString() +
				", errcode=" + errcode +
				", errinfo='" + errinfo + '\'' +
				'}';
	}
}
