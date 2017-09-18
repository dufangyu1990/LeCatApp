package com.example.dufangyu.lecatapp.present;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dufangyu.lecatapp.helper.GenericHelper;
import com.example.dufangyu.lecatapp.view.IView;


/**
 * Created by dufangyu on 2017/6/13.
 */

public class FragmentPresentImpl<T extends IView> extends Fragment implements IPresent<T> {

    protected  T mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        beforeViewCreate(savedInstanceState);
        try {
            mView=getViewClass().newInstance();
            View view = mView.createView(inflater,container);
            mView.bindPresenter(this);
            mView.bindEvent();

            afterViewCreate(savedInstanceState);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }

    @Override
    public void beforeViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void presentCallBack(String param1, String param2, String params3) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
