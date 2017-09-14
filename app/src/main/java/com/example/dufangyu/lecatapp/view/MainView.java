package com.example.dufangyu.lecatapp.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.fragment.HomePageFragment;
import com.example.dufangyu.lecatapp.fragment.MyFragment;


/**
 * Created by dufangyu on 2017/8/31.
 */

public class MainView extends ViewImpl{

    private RadioGroup menuArry;

    private HomePageFragment homePageFragment;
    private MyFragment myFragment;
    private TextView mTitleText;
    @Override
    public void initView() {
        mTitleText = findViewById(R.id.title_text);
        menuArry = findViewById(R.id.menu_arr);
    }




    public void initTabs(final FragmentManager fm)
    {
        menuArry.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                FragmentTransaction transaction = fm.beginTransaction();
                hideFragments(transaction);
                switch (i) {

                    case R.id.tab_homepage:
                        if (homePageFragment == null) {
                            homePageFragment = new HomePageFragment();
                            transaction.add(R.id.ll_content, homePageFragment);
                        } else {
                            transaction.show(homePageFragment);
                        }
                        mTitleText.setText(mRootView.getContext().getString(R.string.homepage));
                        break;
                    case R.id.tab_myself:
                        if (myFragment == null) {
                            myFragment = new MyFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", MyApplication.getInstance().getStringPerference("UserName"));
                            bundle.putString("password", MyApplication.getInstance().getStringPerference("Password"));
                            myFragment.setArguments(bundle);
                            transaction.add(R.id.ll_content, myFragment);
                        } else {
                            transaction.show(myFragment);
                        }
                        mTitleText.setText(mRootView.getContext().getString(R.string.myself));
                        break;

                }
                transaction.commit();
            }
        });

        ((RadioButton) menuArry.findViewById(R.id.tab_homepage)).setChecked(true);
    }


    private void hideFragments(FragmentTransaction ft)
    {

        if(homePageFragment!=null)
            ft.hide(homePageFragment);
        if(myFragment!=null)
            ft.hide(myFragment);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindEvent() {

    }

    public void refreshUI()
    {

    }


}
