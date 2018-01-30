package com.example.dufangyu.lecatapp.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by dufangyu on 2018/1/30.
 */

public class FontCustom {
    // fongUrl是自定义字体分类的名称
    private static String fongUrl = "FZY3JW.TTF";//方正准圆
    private static String fongUrlNumber = "Deng.TTF";//标准
    private static String fongUrlNumberBlod = "Dengb.TTF";//粗体
    private static String fongUrlNUmberLight = "Dengl.TTF";//细
    //Typeface是字体，这里我们创建一个对象
    private static Typeface tf;

    /**
     * 设置字体
     */
    public static Typeface setFZFont(Context context)
    {
        if (tf == null)
        {
            //给它设置你传入的自定义字体文件，再返回回来
            tf = Typeface.createFromAsset(context.getAssets(),fongUrl);
        }
        return tf;
    }

    /**
     * 设置闹钟数字字体
     */
    public static Typeface setNumberFont(Context context)
    {
        if (tf == null)
        {
            //给它设置你传入的自定义字体文件，再返回回来
            tf = Typeface.createFromAsset(context.getAssets(),fongUrlNumber);
        }
        return tf;
    }


    /**
     * 设置闹钟数字粗体字体
     */
    public static Typeface setNumberBFont(Context context)
    {
        if (tf == null)
        {
            //给它设置你传入的自定义字体文件，再返回回来
            tf = Typeface.createFromAsset(context.getAssets(),fongUrlNumberBlod);
        }
        return tf;
    }


    /**
     * 设置闹钟数字细字体
     */
    public static Typeface setNumberLFont(Context context)
    {
        if (tf == null)
        {
            //给它设置你传入的自定义字体文件，再返回回来
            tf = Typeface.createFromAsset(context.getAssets(),fongUrlNUmberLight);
        }
        return tf;
    }



}
