package com.jjhome.demo.bean;

public class GetPushInfoItemBean {

	public String service_port;
	public String pushservice_ip;
	public String pushservic_port;

	@Override
	public String toString() {
		return "GetPushInfoItemBean{" +
				"pushservic_port='" + pushservic_port + '\'' +
				", service_port='" + service_port + '\'' +
				", pushservice_ip='" + pushservice_ip + '\'' +
				'}';
	}
}
