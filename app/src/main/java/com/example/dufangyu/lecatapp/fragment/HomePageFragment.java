package com.example.dufangyu.lecatapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.dufangyu.lecatapp.biz.IHomePage;
import com.example.dufangyu.lecatapp.biz.HomePageBiz;
import com.example.dufangyu.lecatapp.biz.HomePageListener;
import com.example.dufangyu.lecatapp.present.FragmentPresentImpl;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.view.HomePageView;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageFragment extends FragmentPresentImpl<HomePageView> implements HomePageListener {


    private IHomePage mainBiz;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBiz = new HomePageBiz(this);
        LogUtil.d("dfy","HomePageFragment onCreate");
        //获取设备推送数据
        mainBiz.get4GPushData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        LogUtil.d("dfy","HomePageFragment onHiddenChanged"+hidden);
        super.onHiddenChanged(hidden);

        if(!hidden)
        {

        }


    }

    @Override
    public void refreshUI() {

    }
}
