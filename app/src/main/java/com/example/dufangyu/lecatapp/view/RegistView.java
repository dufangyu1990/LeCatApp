package com.example.dufangyu.lecatapp.view;

import android.view.View;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;

/**
 * Created by dufangyu on 2017/9/11.
 */

public class RegistView extends ViewImpl{

    private TextView backtv;
    @Override
    public void initView() {
        backtv = findViewById(R.id.back_img);
        backtv.setVisibility(View.VISIBLE);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,backtv);
    }
}
