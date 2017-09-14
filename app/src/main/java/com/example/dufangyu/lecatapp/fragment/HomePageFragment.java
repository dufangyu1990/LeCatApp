package com.example.dufangyu.lecatapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.dufangyu.lecatapp.present.FragmentPresentImpl;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.view.HomePageView;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageFragment extends FragmentPresentImpl<HomePageView> {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("dfy","HomePageFragment onCreate");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        LogUtil.d("dfy","HomePageFragment onHiddenChanged"+hidden);
        super.onHiddenChanged(hidden);

        if(!hidden)
        {

        }


    }
}
