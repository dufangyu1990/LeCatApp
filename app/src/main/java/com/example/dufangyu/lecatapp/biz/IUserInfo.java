package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/20.
 */

public interface IUserInfo extends BaseInterface{
    void modifyUser(String loginName,String yonghuming,String phoneNumber,String addressStr);
}
