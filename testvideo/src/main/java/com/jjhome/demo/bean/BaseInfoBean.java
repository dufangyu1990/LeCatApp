package com.jjhome.demo.bean;

public class BaseInfoBean {
	public int errcode;
	public String user_id;
	public String errinfo;

	@Override
	public String toString() {
		return "BaseInfoBean{" +
				"errcode=" + errcode +
				", user_id='" + user_id + '\'' +
				", errinfo='" + errinfo + '\'' +
				'}';
	}
}
