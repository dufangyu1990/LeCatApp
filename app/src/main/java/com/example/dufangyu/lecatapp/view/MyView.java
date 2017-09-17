package com.example.dufangyu.lecatapp.view;


import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.Constant;


/**
 * Created by dufangyu on 2017/8/31.
 */

public class MyView extends ViewImpl{


    private TextView usernametv;
    private RelativeLayout userinfo_layout,changepwd_layout,mydevice_layout,add_device_layout,update_layout;
    private TextView exitlogin;


    private LinearLayout update_ll_layout;
    @Override
    public void initView() {
        usernametv = findViewById(R.id.my_name);
        userinfo_layout = findViewById(R.id.userinfo_rel);
        changepwd_layout = findViewById(R.id.changepwd_rel);
        mydevice_layout = findViewById(R.id.mydevice_rel);
        add_device_layout = findViewById(R.id.add_device_rel);
        exitlogin = findViewById(R.id.exitlogin);
        update_layout = findViewById(R.id.update_rel);
        update_ll_layout = findViewById(R.id.update_layout);

    }




    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,userinfo_layout,changepwd_layout,mydevice_layout,add_device_layout,update_layout,exitlogin);
    }


    public void setValue(int id,String value)
    {
        if(id==R.id.my_name)
        {
            usernametv.setText(value);
        }

        if(!TextUtils.isEmpty(value))
        {
            if(value.equals(Constant.AD))
            {
                update_ll_layout.setVisibility(View.VISIBLE);
            }else{
                update_ll_layout.setVisibility(View.GONE);
            }
        }

    }




}
