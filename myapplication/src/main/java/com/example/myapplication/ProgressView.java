package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dufangyu on 2017/9/15.
 */

public class ProgressView extends View{


    /** 画笔 */
    private Paint paint;
    /** 自定义控件的属性s */
    private TypedArray attributes;
    /** 圆环最大值 */
    private int max;
    /** 文字尺寸 */
    private float textSize;
    /** 文字颜色 */
    private int textColor;
    /** 圆环前景色-轨迹颜色 */
    private int ringForegroundColor;
    /** 圆环背景色-轨道颜色 */
    private int ringBackgroudnColor;
    /** 圆环轨迹宽度 */
    private float ringSize;
    /** 圆环当前进度 */
    private int progress;
    /** 圆心位置 */
    private int center;
    /** 圆半径 */
    private int radius;

    /** 最好是为在xml里定义的这些属性提供set、get方法 */

    // 2参构造方法在xml里引用
    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        // 读取/res/values/attr.xml中的自定义配置
        attributes = context.obtainStyledAttributes(attrs, R.styleable.udfstyle);
        // 读取每个自定义属性，（xml里配置的属性值，读不到时的默认值）
        ringBackgroudnColor = attributes.getColor(R.styleable.udfstyle_ringBackgroundColor, Color.RED);
        ringForegroundColor = attributes.getColor(R.styleable.udfstyle_ringForegroundColor, Color.GREEN);
        textColor = attributes.getColor(R.styleable.udfstyle_textColor, Color.BLACK);
        textSize = attributes.getDimension(R.styleable.udfstyle_textSize, 15);
        ringSize = attributes.getDimension(R.styleable.udfstyle_ringSize, 5);
        max = attributes.getInteger(R.styleable.udfstyle_max, 100);
        attributes.recycle();
    }


    /**
     * 绘制控件 此方法持续调用
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        center = getWidth() / 2;	// 此时可以获取控件的尺寸了
        radius = (int) ((center - ringSize) / 2);
        //Log.e("ard", "控件中心：" + center + "px，半径：" + radius + "px");

        cal(canvas);
        arc(canvas);
        txt(canvas);
    }

    /**
     * 绘制圆环轨道
     */
    private void cal(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);     // 设置空心
        paint.setColor(ringBackgroudnColor);    // 设置圆环轨道颜色
        paint.setStrokeWidth(ringSize); 	// 设置圆环的宽度
        paint.setAntiAlias(true); 		// 消除锯齿

        // 画出圆形（圆心x、圆心y，半径，画笔），如果paint为空心模式，画出来的就是圆环；实心模式画出的是圆饼
        // xml里配的宽高400dp
        canvas.drawCircle(center, center, radius, paint);
    }

    /**
     * 圆环的进度-轨迹
     */
    private void arc(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringSize); 	// 设置圆环的宽度
        paint.setColor(ringForegroundColor);    // 设置进度的颜色
        // 定义一个矩形区域（左，上，右，下）。正好重合圆环所在的范围
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        // 绘制圆弧（绘制范围，顺时针起始角度，结束角度，是否显示指针，画笔）
        canvas.drawArc(oval, -90, 360 * progress / max, false, paint); // 根据进度画圆弧
    }

    /**
     * 画进度百分比
     */
    private void txt(Canvas canvas) {
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); 			// 设置字体
        float percent = (float)progress / (float)max * 100;		// 进度百分比
        float textWidth = paint.measureText((int)percent + "%"); 	// 字体宽度
        // 绘制文本（文本，绘制区域x起点，y起点，画笔）
        canvas.drawText((int)percent + "%", center - textWidth / 2, center + textSize / 2, paint);
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }

        if (progress > max) {
            progress = max;
        }

        this.progress = progress;

        // 刷新控件。刷一次即执行一次onDraw。postInvalidate()能在非UI线程刷新
        postInvalidate();
    }
}
