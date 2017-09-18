package com.example.dufangyu.lecatapp.view;

import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.customview.TitleLinearLayout;


/**
 * Created by dufangyu on 2017/9/6.
 */

public class AboutUsView extends ViewImpl{
    private TextView versiontext;
    private TitleLinearLayout linearLayout_title;
    @Override
    public void initView() {



        linearLayout_title = findViewById(R.id.titleLayout);
        linearLayout_title.setBackVisisble(true);
        linearLayout_title.setBackText(mRootView.getContext().getString(R.string.myself));
        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.about));

        versiontext = findViewById(R.id.versinname);
        versiontext.setText("Version"+ MyApplication.getInstance().getStringPerference("versionName"));

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void bindEvent() {
    }
}
