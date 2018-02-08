package com.jjhome.demo.bean;


public class GetPushInfoBean {
	//{"errcode":0,"pushserver_info":{"service_port":"9966","pushserver_ip":"112.74.197.198","push_port":"9999"}}
	public int errcode;
	public String errinfo;
	GetPushInfoItemBean item;

	@Override
	public String toString() {
		return "GetPushInfoBean{" +
				"errcode=" + errcode +
				", errinfo='" + errinfo + '\'' +
				", item=" + item.toString() +
				'}';
	}

}
