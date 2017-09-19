package com.example.dufangyu.lecatapp.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.utils.Util;

/**
 * Created by dufangyu on 2017/9/12.
 */

public class ProgressView extends View{


    /**
     * 弧度
     */
    private int mAngle;
    /** 画笔 */
    private Paint paint;
    /** 自定义控件的属性s */
    private TypedArray attributes;
    /** 圆环最大值 */
    private int max;
    /** 上面文字尺寸 */
    private float textSize;
    /** 下面文字尺寸 */
    private float textSize_bleow;
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

    private int bigradius;
    //淡蓝外边距离灰色外边的距离
    private int distance_out =10;
    //淡蓝内边距离灰色内边的距离
    private int distance_inner=20;

    private Context mContext;
    private String text1="";
    private String textValue="";
    private int starAngle = -90;
    //需要执行动画的参数名
    private static final String PROGRESS_PROPERTY = "progress";
    int disProgress = 0;
    private int tempProgress;
    private int timeDely = 5;

    /** 最好是为在xml里定义的这些属性提供set、get方法 */

    // 2参构造方法在xml里引用
    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        paint = new Paint();

        // 读取/res/values/attr.xml中的自定义配置
        attributes = context.obtainStyledAttributes(attrs, R.styleable.udfstyle);
        // 读取每个自定义属性，（xml里配置的属性值，读不到时的默认值）
        ringBackgroudnColor = attributes.getColor(R.styleable.udfstyle_ringBackgroundColor, Color.RED);
        ringForegroundColor = attributes.getColor(R.styleable.udfstyle_ringForegroundColor, Color.GREEN);
        textColor = attributes.getColor(R.styleable.udfstyle_textColor, getResources().getColor(R.color.lightlanse));
        textSize = attributes.getDimension(R.styleable.udfstyle_textSize, 12);
        textSize_bleow = attributes.getDimension(R.styleable.udfstyle_textSize, 10);
        ringSize = attributes.getDimension(R.styleable.udfstyle_ringSize, 5);
        max = attributes.getInteger(R.styleable.udfstyle_max, 60);
        attributes.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        WindowManager wm  =  (WindowManager)  getContext().getSystemService(Context.WINDOW_SERVICE);

        float sw=  wm.getDefaultDisplay().getWidth();

        float sh =  wm.getDefaultDisplay().getHeight();

        float  opt  = sw /  720;

        int dw = (int)(width * opt);//设置适配宽度

        opt = sh / 1280;

        int dh =  (int)(height * opt);//设置适配高度
//        LogUtil.d("dfy","sw = "+sw+",sh = "+sh);
//        LogUtil.d("dfy","dw = "+dw+",dh = "+dh);
        setMeasuredDimension(dw, dh);


    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST){
            return 300;
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY){
            return specSize;
        }
        return specSize;
    }
    //根据xml的设定获取高度
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST){
            return 300;
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY){
            return specSize;
        }
        return specSize;
    }

    /**
     * 绘制控件 此方法持续调用
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        center = getWidth() / 2;	// 此时可以获取控件的尺寸了
//        radius = (int) ((center - ringSize) / 2);
        radius =  (int)(center*0.7) ;


        //蓝色内边距离圆心的距离
        int dis = (int)(radius-ringSize);

        //灰色内边距离圆心的距离
        int temp = dis-distance_inner;

        int bigPaintWidth  =(int) (distance_out+ringSize+distance_inner);

        bigradius =temp+bigPaintWidth/2;

        calBig(canvas,bigPaintWidth);
        cal(canvas);
        arc(canvas);
        txt(canvas);
        txtValue(canvas);
    }


    //绘制最外层圆环
    private void calBig(Canvas canvas,int bigPaintWidth)
    {
        paint.setStyle(Paint.Style.STROKE);     // 设置空心
        paint.setColor(Color.parseColor("#E8E8E8"));    // 设置圆环轨道颜色
        paint.setStrokeWidth(bigPaintWidth); 	// 设置圆环的宽度
        paint.setAntiAlias(true); 		// 消除锯齿
        paint.setShadowLayer(10f, 0, 0, Color.parseColor("#D3D3D3"));
        // 画出圆形（圆心x、圆心y，半径，画笔），如果paint为空心模式，画出来的就是圆环；实心模式画出的是圆饼
        canvas.drawCircle(center, center, bigradius, paint);
    }

    /**
     * 绘制圆环轨道
     */
    private void cal(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);     // 设置空心
        paint.setColor(ringBackgroudnColor);    // 设置圆环轨道颜色
        paint.setStrokeWidth(ringSize); 	// 设置圆环的宽度
        paint.setAntiAlias(true); 		// 消除锯齿
        int tempradius= (int)(radius-ringSize/2);
//        Log.d("dfy","tempradius = "+tempradius);
        // 画出圆形（圆心x、圆心y，半径，画笔），如果paint为空心模式，画出来的就是圆环；实心模式画出的是圆饼
        canvas.drawCircle(center, center, tempradius, paint);
    }

    /**
     * 圆环的进度-轨迹
     */
    private void arc(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringSize); 	// 设置圆环的宽度
        paint.setColor(ringForegroundColor);// 设置进度的颜色
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        int tempradius= (int)(radius-ringSize/2);
        // 定义一个矩形区域（左，上，右，下）。正好重合圆环所在的范围
        RectF oval = new RectF(center - tempradius, center - tempradius, center + tempradius, center + tempradius);
        // 绘制圆弧（绘制范围，顺时针起始角度，结束角度，是否显示指针，画笔）
        canvas.drawArc(oval, starAngle,mAngle, false, paint); // 根据进度画圆弧
    }

    /**
     * 画下面文字
     */
    private void txtValue(Canvas canvas) {
        paint.reset();
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize_bleow);
        paint.setTypeface(Typeface.DEFAULT_BOLD); 			// 设置字体
        float textWidth = paint.measureText(textValue); 	// 字体宽度
        // 绘制文本（文本，绘制区域x起点，y起点，画笔）
        canvas.drawText(textValue, center - textWidth / 2, center + textSize_bleow / 2+ Util.dip2px(mContext,10), paint);
    }


    /**
     * 画上面文字
     */
    private void txt(Canvas canvas) {
        paint.reset();
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); 			// 设置字体
        float textWidth = paint.measureText(text1); 	// 字体宽度
        // 绘制文本（文本，绘制区域x起点，y起点，画笔）
        canvas.drawText(text1, center - textWidth / 2, center + textSize / 2- Util.dip2px(mContext,10), paint);
    }


    /**
     * 画进度百分比
     */
    private void txtvalue(Canvas canvas) {
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize_bleow);
        paint.setTypeface(Typeface.DEFAULT_BOLD); 			// 设置字体
        float percent = (float)progress / (float)max * 100;		// 进度百分比
        float textWidth = paint.measureText((int)percent + "%"); 	// 字体宽度
        // 绘制文本（文本，绘制区域x起点，y起点，画笔）
        canvas.drawText((int)percent + "%", center - textWidth / 2, center + textSize_bleow / 2, paint);
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
        tempProgress = progress;
        mAngle  = 360 * progress / max;

//        start();
        // 刷新控件。刷一次即执行一次onDraw。postInvalidate()能在非UI线程刷新
        invalidate();
    }



//
//    public void dodo(float progressText, int progress) {
//        this.progress = progress;
//
//        AnimatorSet animation = new AnimatorSet();
//
//        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this,PROGRESS_PROPERTY, 0f, progress);
//        progressAnimation.setDuration(700);// 动画执行时间
//
//        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        animation.playTogether(progressAnimation);//动画同时执行,可以做多个动画
//        animation.start();
//    }
    /**
     * 设置带动画的进度
     */
    @TargetApi(19)
    public void setAnimProgress(int progress,final int flag) {
        //flag代表绘制温度还是湿度
        //1 温度
        // 0  湿度
        if (progress < 0) {
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }

        this.progress = progress;
        disProgress = progress-tempProgress;
        start();








//        //设置属性动画
//        ValueAnimator  valueAnimator = new ValueAnimator().ofInt(0, progress);
//        //动画从快到慢
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setDuration(1000);
//        //监听值的变化
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int currentV = (Integer) animation.getAnimatedValue();
//                mAngle = 360 * currentV / max;
//                if(flag ==1)
//                {
//                    textValue = currentV + "℃";
//                }else if(flag ==0)
//                {
//                    textValue = currentV + "%";
//                }
//                invalidate();
//            }
//        });
//        valueAnimator.start();
    }




    //上面的文字
    public void setText(String text)
    {
        this.text1 = text;
    }

    //下面的文字
    public void setTextValue(String textValue)
    {
        this.textValue = textValue;
    }

    public void setMax(int max)
    {
        this.max = max;
    }


    /**
     * 开始。
     */
    public void start() {
        stop();
        post(progressChangeTask);
    }

    /**
     * 停止。
     */
    public void stop() {
        removeCallbacks(progressChangeTask);
    }

    /**
     * 进度更新task。
     */
    private Runnable progressChangeTask = new Runnable() {
        @Override
        public void run() {
//            LogUtil.d("dfy","tempProgress = "+tempProgress);
//            LogUtil.d("dfy","progress = "+progress);
            removeCallbacks(this);
           if(disProgress>0)
           {
               tempProgress +=1;
           }else{
               tempProgress -= 1;
           }
            mAngle = 360 * tempProgress / max;
//            LogUtil.d("dfy","mAngle = "+mAngle);
           if(disProgress>0)
           {
               if(tempProgress<=progress)
               {
//                   LogUtil.d("dfy","progressChangeTask ++");
                   invalidate();
                   postDelayed(progressChangeTask, timeDely);
               }else{
                   tempProgress = progress;
               }
           }else{
               if(tempProgress>=progress)
               {
//                   LogUtil.d("dfy","progressChangeTask --");
                   invalidate();
                   postDelayed(progressChangeTask, timeDely);
               }else{
                   tempProgress = progress;
               }
           }


        }
    };


    public int getTimeDely() {
        return timeDely;
    }

    //动画延迟时间
    public void setTimeDely(int timeDely) {
        this.timeDely = timeDely;
    }
}
