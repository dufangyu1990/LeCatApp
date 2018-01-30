package com.example.dufangyu.lecatapp.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.dufangyu.lecatapp.utils.FontCustom;

/**
 * Created by dufangyu on 2018/1/30.
 * 闹钟模块需要用到的字体----粗体
 */

public class MyTextViewNumberB extends AppCompatTextView {

    public MyTextViewNumberB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    /**
     * 初始化字体
     * @param context
     */
    private void init(Context context) {
        //设置字体样式
        setTypeface(FontCustom.setNumberBFont(context));
    }
}
