package com.example.dufangyu.lecatapp.view;


import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.customview.ProgressView;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageView extends ViewImpl{

    private ProgressView wendu_progressView,shidu_progressView;
    @Override
    public void initView() {
        wendu_progressView = findViewById(R.id.wenduPro);
        shidu_progressView = findViewById(R.id.shiduPro);
        shidu_progressView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        wendu_progressView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        wendu_progressView.setProgress(30);
        wendu_progressView.setMax(60);
        wendu_progressView.setText("酷热");
        wendu_progressView.setTextValue("30℃");
        shidu_progressView.setProgress(30);
        shidu_progressView.setMax(100);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void bindEvent() {

    }
}
