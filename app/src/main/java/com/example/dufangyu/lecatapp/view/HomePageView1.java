package com.example.dufangyu.lecatapp.view;


import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.bean.RealData;
import com.example.dufangyu.lecatapp.customview.ProgressView;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.Util;

import static com.example.dufangyu.lecatapp.utils.Constant.HUMiDITY_BELOW;
import static com.example.dufangyu.lecatapp.utils.Constant.HUMiDITY_MID;
import static com.example.dufangyu.lecatapp.utils.Constant.TEMPERATURE_BELOW;
import static com.example.dufangyu.lecatapp.utils.Constant.TEMPERATURE_HIGH;

/**
 * Created by dufangyu on 2017/8/31.
 */

public class HomePageView1 extends ViewImpl{

    private ProgressView wendu_progressView,shidu_progressView;
    private TextView dateTv,timeTv;
    private TextView updateTimeTv;
    private ImageView menkongimg;
    private TextView redLightTv,greenLightTv,blueLightTv,closeLightTv;
    private TextView doorlightTv;
    private TextView lightControlTv;
    private TextView doorStateTv,doorBatteryTv,lockStateTv,lockBatteryTv,catBatteryTv;
    private boolean isLightOn = false;
    private TextView indoor_VideoTv,outdoor_VideoTv;
    private TextView jiantingTv;
    @Override
    public void initView() {

        lightControlTv = findViewById(R.id.lightcontrolTv);
        doorlightTv = findViewById(R.id.doorlightTv);
        doorStateTv = findViewById(R.id.doorStateTv);
        doorBatteryTv = findViewById(R.id.doorBatteryTv);
        lockStateTv = findViewById(R.id.lockStaeTv);
        lockBatteryTv = findViewById(R.id.lockBatteryTv);
        catBatteryTv = findViewById(R.id.catBatteryTv);
        jiantingTv = findViewById(R.id.jiantingTv);
        indoor_VideoTv = findViewById(R.id.indoor_videoTv);
        outdoor_VideoTv = findViewById(R.id.outdoor_videoTv);





//        menkongimg = findViewById(R.id.menkongimg);
//        updateTimeTv = findViewById(R.id.updateTimeTv);
//
//        wendu_progressView = findViewById(R.id.wenduPro);
//        shidu_progressView = findViewById(R.id.shiduPro);
//        wendu_progressView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        shidu_progressView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        wendu_progressView.setMax(60);
//        shidu_progressView.setMax(100);
//        dateTv = findViewById(R.id.date_textView);
//        timeTv = findViewById(R.id.time_textView);
//        redLightTv = findViewById(R.id.red_light);
//        greenLightTv = findViewById(R.id.green_light);
//        blueLightTv = findViewById(R.id.blue_light);
//        closeLightTv = findViewById(R.id.close_light);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homepage1;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,lightControlTv,doorlightTv,
                indoor_VideoTv,outdoor_VideoTv,jiantingTv);
    }


    public void refreshView(final RealData realData)
    {

        int temperature = Integer.parseInt(realData.getTemperatureValue());
        int humidity = Integer.parseInt(realData.getHumidityValueValue());
        updateTimeTv.setVisibility(View.VISIBLE);
        dateTv.setText(Util.getDateTime(realData.getUpdateTime())[0]);
        timeTv.setText(Util.getDateTime(realData.getUpdateTime())[1]);
        if(temperature<TEMPERATURE_BELOW)
        {
            wendu_progressView.setText("寒冷");
        }else if(temperature<=TEMPERATURE_HIGH)
        {
            wendu_progressView.setText("适宜");
        }else{
            wendu_progressView.setText("炎热");
        }
        wendu_progressView.setTimeDely(3);
        wendu_progressView.setTextValue(temperature+"℃");
        wendu_progressView.setAnimProgress(temperature,1);


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
        shidu_progressView.setTextValue(humidity+"%");
        shidu_progressView.setTimeDely(3);

    }


    public void reFreshNoData()
    {
        LogUtil.d("dfy","无数据可刷新");
//        wendu_progressView.setProgress(0);
//        shidu_progressView.setProgress(0);
//        wendu_progressView.setText("");
//        wendu_progressView.setTextValue("");
//        shidu_progressView.setText("");
//        shidu_progressView.setTextValue("");
//        updateTimeTv.setVisibility(View.INVISIBLE);
//        dateTv.setText("");
//        timeTv.setText("");
    }



    public void refreshHomeUI(String lockValue,String doorValue,String batteryValue)
    {

        LogUtil.d("dfy","refreshHomeUI");
        if(doorValue.equals("0"))
        {
            doorStateTv.setText("开");
        }else if(doorValue.equals("1"))
        {
            doorStateTv.setText("关");
        }


        if(lockValue.equals("0"))
        {

            lockStateTv.setText("开");
        }else if(lockValue.equals("1"))
        {
            lockStateTv.setText("关");
        }

        String[] strArr = batteryValue.split(",",-1);
        doorBatteryTv.setText(strArr[0]);
        lockBatteryTv.setText(strArr[1]);
        catBatteryTv.setText(strArr[2]);

    }



    public void changeImg()
    {
        Drawable drawable= null;
        if(!isLightOn)
        {
            drawable = ContextCompat.getDrawable(mRootView.getContext(),R.drawable.light_on);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            doorlightTv.setCompoundDrawables(null,drawable,null,null);
            isLightOn = true;
        }else{

            drawable = ContextCompat.getDrawable(mRootView.getContext(),R.drawable.light);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            doorlightTv.setCompoundDrawables(null,drawable,null,null);
            isLightOn = false;
        }
    }

}
