package com.example.dufangyu.lecatapp.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.customview.TitleLinearLayout;
import com.example.dufangyu.lecatapp.fragment.HomePageFragment1;
import com.example.dufangyu.lecatapp.fragment.MyFragment;
import com.example.dufangyu.lecatapp.fragment.SetPageFragment;
import com.example.dufangyu.lecatapp.utils.SharePrefUtil;


/**
 * Created by dufangyu on 2017/8/31.
 */

public class MainView extends ViewImpl{

    private RadioGroup menuArry;

    private SetPageFragment setPageFragment;
    private HomePageFragment1 homePageFragment1;
    private MyFragment myFragment;
    private TitleLinearLayout linearLayout_title;
    @Override
    public void initView() {
        menuArry = findViewById(R.id.menu_arr);
        linearLayout_title = findViewById(R.id.titleLayout);


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
                        if (homePageFragment1 == null) {
                            homePageFragment1 = new HomePageFragment1();
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", SharePrefUtil.getString("userId",""));
                            bundle.putString("userPwd", SharePrefUtil.getString("userPwd",""));
                            bundle.putString("userPushIp", SharePrefUtil.getString("userPushIp",""));
                            homePageFragment1.setArguments(bundle);
                            transaction.add(R.id.ll_content, homePageFragment1);
                        } else {
                            transaction.show(homePageFragment1);
                        }
                        linearLayout_title.setVisibility(View.VISIBLE);
                        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.homepage));
                        break;
                    case R.id.tab_settingpage:
                        if (setPageFragment == null) {
                            setPageFragment = new SetPageFragment();
                            transaction.add(R.id.ll_content, setPageFragment);
                        } else {
                            transaction.show(setPageFragment);
                        }
                        linearLayout_title.setVisibility(View.VISIBLE);
                        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.setting));

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
                        linearLayout_title.setVisibility(View.INVISIBLE);
                        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.myself));
                        break;


                }
                transaction.commit();
            }
        });

        ((RadioButton) menuArry.findViewById(R.id.tab_homepage)).setChecked(true);
    }


    private void hideFragments(FragmentTransaction ft)
    {

        if(homePageFragment1!=null)
            ft.hide(homePageFragment1);
        if(setPageFragment !=null)
            ft.hide(setPageFragment);
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



}
