package com.example.dufangyu.lecatapp.view;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;

import static com.example.dufangyu.lecatapp.R.id.close_light;

/**
 * Created by dufangyu on 2018/2/4.
 */

public class LightControlView extends ViewImpl{

    private TextView back_tv,titleTv;
    private TextView blue_lightTv,red_lightTv,green_lightTv,close_lightTv;
    private ImageView lightStateImg;
    private TextView showColorTv,showLightTv;
    @Override
    public void initView() {
        showColorTv = findViewById(R.id.showcolortv);
        showLightTv = findViewById(R.id.showlighttv);
        lightStateImg = findViewById(R.id.lightStateImg);
        back_tv = findViewById(R.id.back_img);
        titleTv = findViewById(R.id.title_text);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("灯控");
        titleTv.setTextColor(mRootView.getContext().getResources().getColor(R.color.green));

        blue_lightTv = findViewById(R.id.blue_light);
        red_lightTv = findViewById(R.id.red_light);
        green_lightTv = findViewById(R.id.green_light);
        close_lightTv = findViewById(close_light);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_light_control;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,back_tv,
                blue_lightTv,red_lightTv,green_lightTv,close_lightTv);
    }





    public void refreshUI(String lightState)
    {
        if(!TextUtils.isEmpty(lightState))
        {
            setLightStateImg(lightState);
        }

    }




    public void setLightStateImg(String state)
    {
        Drawable drawable = null;
        setBtnEnable();
        if(state.equals("3"))
        {
            drawable = ContextCompat.getDrawable(mRootView.getContext(),R.drawable.open_blue);
            blue_lightTv.setEnabled(false);
            showColorTv.setText("蓝色");
            showLightTv.setText("开");
        }else if(state.equals("1"))
        {
            drawable = ContextCompat.getDrawable(mRootView.getContext(),R.drawable.open_red);
            red_lightTv.setEnabled(false);
            showColorTv.setText("红色");
            showLightTv.setText("开");
        }else if(state.equals("2"))
        {
            drawable = ContextCompat.getDrawable(mRootView.getContext(),R.drawable.open_green);
            green_lightTv.setEnabled(false);
            showColorTv.setText("绿色");
            showLightTv.setText("开");
        }else if(state.equals("0"))
        {
            drawable = ContextCompat.getDrawable(mRootView.getContext(),R.drawable.close_light);
            close_lightTv.setEnabled(false);
            showColorTv.setText("无");
            showLightTv.setText("关");
        }
        lightStateImg.setImageDrawable(drawable);
    }


    private void setBtnEnable()
    {
        blue_lightTv.setEnabled(true);
        red_lightTv.setEnabled(true);
        green_lightTv.setEnabled(true);
        close_lightTv.setEnabled(true);
    }
}
