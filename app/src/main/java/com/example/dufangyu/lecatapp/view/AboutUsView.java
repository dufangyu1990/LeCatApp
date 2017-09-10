package com.example.dufangyu.lecatapp.view;

import android.view.View;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.helper.EventHelper;


/**
 * Created by dufangyu on 2017/9/6.
 */

public class AboutUsView extends ViewImpl{
    private TextView titletext,backtext,versiontext;
    @Override
    public void initView() {
        titletext = findViewById(R.id.title_text);
        backtext = findViewById(R.id.back_img);
        backtext.setVisibility(View.VISIBLE);
        titletext.setText(mRootView.getContext().getString(R.string.about));
        backtext.setText(mRootView.getContext().getString(R.string.myself));
        versiontext = findViewById(R.id.versinname);
        versiontext.setText("Version"+ MyApplication.getInstance().getStringPerference("versionName"));

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backtext);
    }
}
