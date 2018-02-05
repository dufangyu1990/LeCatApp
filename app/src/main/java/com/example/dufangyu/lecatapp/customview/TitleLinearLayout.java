package com.example.dufangyu.lecatapp.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;

/**
 * Created by dufangyu on 2017/9/18.
 */

public class TitleLinearLayout extends LinearLayout{
    private TextView backTv;
    private TextView title_textTv;
    public TitleLinearLayout(Context context) {
        super(context);
    }

    public TitleLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mView = LayoutInflater.from(context).inflate(R.layout.title_bar,this);
        title_textTv = (TextView) mView.findViewById(R.id.title_text);
        backTv = (TextView)mView.findViewById(R.id.back_img);
//        backTv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((Activity) getContext()).finish();
//            }
//        });

    }


    //返回键是否可见
    public void setBackVisisble(boolean isVisible)
    {
        if(isVisible)
            backTv.setVisibility(VISIBLE);
        else
            backTv.setVisibility(VISIBLE);
    }

    public void setTitleText(String value)
    {
        title_textTv.setText(value);
    }

    public void setBackText(String value)
    {
        backTv.setText(value);
    }

    public void setTitleSize(float size)
    {
        title_textTv.setTextSize(size);
    }

    public void setBackBg(int color)
    {
        setBackgroundColor(color);
    }

    //返回键是否可见
    public void setTitleVisible(boolean isVisible)
    {
        if(isVisible)
            title_textTv.setVisibility(VISIBLE);
        else
            title_textTv.setVisibility(VISIBLE);
    }

}
