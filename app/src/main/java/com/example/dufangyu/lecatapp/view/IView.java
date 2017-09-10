package com.example.dufangyu.lecatapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dufangyu.lecatapp.present.IPresent;


/**
 * Created by dufangyu on 2017/6/13.
 */

public interface IView {

    /**
     * 根据getLayoutId生成setContentView需要的布局
     * @param inflater
     * @param container
     * @return
     */
    View createView(LayoutInflater inflater, ViewGroup container);

    /**
     * Activity的onCreate之后调用，初始化view
     */
    void initView();

    /**
     * 返回当时视图的layoutid
     * @return
     */
    int getLayoutId();


    /**
     * 获取view
     * @param id
     * @param <V>
     * @return
     */
    <V extends View> V findViewById(int id);

    /**
     * 绑定presenter
     * @param present
     */
    void bindPresenter(IPresent present);


    /**
     * 绑定事件
     */
    void bindEvent();

}
