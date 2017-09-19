package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/13.
 */

public interface IRegist extends BaseInterface{

    void registUser(String username,String password,RegistListenr listenr);
}
