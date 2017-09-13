package com.example.dufangyu.lecatapp.view;


import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;


/**
 * Created by dufangyu on 2017/8/31.
 */

public class MyView extends ViewImpl{


    private TextView usernametv;
    private RelativeLayout sytem_layout,changepwd_layout,yijian_layout,about_layout,update_layout;
    private TextView exitlogin;


    @Override
    public void initView() {
        usernametv = findViewById(R.id.my_name);
        sytem_layout = findViewById(R.id.systemset_rel);
        changepwd_layout = findViewById(R.id.changepwd_rel);
        yijian_layout = findViewById(R.id.returnavs_rel);
        about_layout = findViewById(R.id.about_rel);
        exitlogin = findViewById(R.id.exitlogin);
        update_layout = findViewById(R.id.update_rel);

    }




    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,sytem_layout,changepwd_layout,yijian_layout,about_layout,update_layout,exitlogin);
    }


    public void setValue(int id,String value)
    {
        if(id==R.id.my_name)
        {
            usernametv.setText(value);
        }
    }




}
