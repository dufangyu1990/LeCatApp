package com.example.dufangyu.lecatapp.utils;

/**
 * Created by dufangyu on 2016/7/26.
 */
public abstract class HttpCallBack<T> {
    public abstract void getHttpResult(T t);
    public abstract void getHttpErrorResult(int errorCode, String msg);
}
