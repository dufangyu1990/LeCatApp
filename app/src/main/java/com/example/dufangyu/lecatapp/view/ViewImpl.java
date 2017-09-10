package com.example.dufangyu.lecatapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dufangyu.lecatapp.present.IPresent;


/**
 * Created by dufangyu on 2017/6/13.
 */

public abstract class ViewImpl implements IView {

    protected View mRootView;
    protected IPresent mPresent;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container) {

        mRootView = inflater.inflate(getLayoutId(),container,false);
        initView();
        return mRootView;
    }


    @Override
    public <V extends View> V findViewById(int id) {
        return (V) mRootView.findViewById(id);
    }



    @Override
    public void bindPresenter(IPresent present) {
        mPresent = present;
    }
}
