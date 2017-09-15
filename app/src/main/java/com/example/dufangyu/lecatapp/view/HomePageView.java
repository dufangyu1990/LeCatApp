package com.example.dufangyu.lecatapp.view;


import android.view.View;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.customview.ProgressView;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.Util;

import static com.example.dufangyu.lecatapp.utils.Constant.HUMiDITY_BELOW;
import static com.example.dufangyu.lecatapp.utils.Constant.HUMiDITY_MID;
import static com.example.dufangyu.lecatapp.utils.Constant.TEMPERATURE_BELOW;
import static com.example.dufangyu.lecatapp.utils.Constant.TEMPERATURE_HIGH;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageView extends ViewImpl{

    private ProgressView wendu_progressView,shidu_progressView;
    private TextView dateTv,timeTv;
    @Override
    public void initView() {
        wendu_progressView = findViewById(R.id.wenduPro);
        shidu_progressView = findViewById(R.id.shiduPro);
//        wendu_progressView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        shidu_progressView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        wendu_progressView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        shidu_progressView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        wendu_progressView.setMax(60);
        shidu_progressView.setMax(100);
        dateTv = findViewById(R.id.date_textView);
        timeTv = findViewById(R.id.time_textView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void bindEvent() {

    }


    public void refreshView(final RealData realData)
    {

        int temperature = Integer.parseInt(realData.getTemperatureValue());
        int humidity = Integer.parseInt(realData.getHumidityValueValue());

        dateTv.setText(Util.getDateTime(realData.getUpdateTime())[0]);
        timeTv.setText(Util.getDateTime(realData.getUpdateTime())[1]);

        LogUtil.d("dfy","temperature = "+temperature);
        LogUtil.d("dfy","humidity = "+humidity);
        wendu_progressView.setAnimProgress(temperature,1);

        if(temperature<TEMPERATURE_BELOW)
        {
            wendu_progressView.setText("寒冷");
        }else if(temperature<=TEMPERATURE_HIGH)
        {
            wendu_progressView.setText("适宜");
        }else{
            wendu_progressView.setText("炎热");
        }

//        wendu_progressView.setTextValue(temperature+"℃");

        if(humidity<HUMiDITY_BELOW)
        {
            shidu_progressView.setText("干燥");
        }else if(humidity<=HUMiDITY_MID)
        {
            shidu_progressView.setText("适宜");
        }else
        {
            shidu_progressView.setText("湿润");
        }
        shidu_progressView.setAnimProgress(humidity,0);
//        shidu_progressView.setTextValue(humidity+"%");
//        shidu_progressView.setProgress(humidity);

    }


}
