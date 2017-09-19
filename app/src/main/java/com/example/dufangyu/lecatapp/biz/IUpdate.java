package com.example.dufangyu.lecatapp.biz;

/**
 * Created by dufangyu on 2017/9/13.
 */

public interface IUpdate extends BaseInterface{

    void updateApp(String newVersionCode,UpdateListener listener);
}
