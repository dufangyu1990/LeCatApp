package com.example.dufangyu.lecatapp.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.dufangyu.lecatapp.utils.FontCustom;

/**
 * Created by dufangyu on 2018/1/30.
 * 设置方正准圆字体
 */

public class MyTextView extends AppCompatTextView {

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    /**
     * 初始化字体
     * @param context
     */
    private void init(Context context) {
        //设置字体样式
        setTypeface(FontCustom.setFZFont(context));
    }
}
