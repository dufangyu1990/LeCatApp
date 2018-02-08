package com.jjhome.demo.bean;

public class UpdateAppBean {
	public int errcode;
	public String ver;
	public String url;

	@Override
	public String toString() {
		return "UpdateAppBean{" +
				"errcode=" + errcode +
				", ver='" + ver + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
