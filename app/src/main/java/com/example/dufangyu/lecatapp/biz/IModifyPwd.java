package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/6.
 */

public interface IModifyPwd extends BaseInterface{
    void modifyPwd(String loginName, String newPwd, ModifyPwdListener listener);
}
