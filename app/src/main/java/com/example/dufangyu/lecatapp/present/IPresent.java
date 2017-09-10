package com.example.dufangyu.lecatapp.present;

import android.os.Bundle;

/**
 * Created by dufangyu on 2017/6/13.
 */

public interface IPresent<T> {

    Class<T> getViewClass();

    void beforeViewCreate(Bundle savedInstanceState);

    void afterViewCreate(Bundle savedInstanceState);



    //view回调activity中方法
    void presentCallBack(String param1, String param2, String params3) ;



}
