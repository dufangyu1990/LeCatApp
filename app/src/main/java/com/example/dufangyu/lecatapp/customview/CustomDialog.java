package com.example.dufangyu.lecatapp.customview;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;


/**
 * 显示信息dialog，无动画效果
 */
public class CustomDialog extends Dialog {
	 private Animation animation; 
	 private static ImageView imageView ;
	 private static CustomDialog dialog;
	private static Handler mHandler = new Handler();

	private static Context context;


	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {

		super(context, theme);
		this.context = context;
	}

	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
//		 imageView = (ImageView) findViewById(R.id.spinnerImageView);
//		//此方法是用xml文件方式来实现动画效果
//		 animation = AnimationUtils.loadAnimation(getContext(), R.anim.spinner);
//	        //动画完成后，是否保留动画最后的状态，设为true
//	      animation.setFillAfter(true);
//	      if(animation!=null)
//	      {
//	    	  imageView.startAnimation(animation);
//	      }
		// 获取ImageView上的动画背景(此方法是用一组图片来达到动画效果)
//		AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
//		// 开始动画
//		spinner.start();
	}

	/**
	 * 给Dialog设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(CharSequence message) {
		if (message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);
			TextView txt = (TextView) findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}

	/**
	 * 弹出自定义ProgressDialog
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            提示
	 * @param cancelable
	 *            是否按返回键取消
	 * @param cancelListener
	 *            按下返回键监听
	 * @return
	 */
	public static CustomDialog show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener, int resId) {
		dialog = new CustomDialog(context, R.style.Custom_Progress);
		dialog.setTitle("");
//		dialog.setContentView(R.layout.progress_custom);
		dialog.setContentView(resId);
		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
		// 按返回键是否取消
		dialog.setCancelable(cancelable);
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

		if(!((Activity) context).isFinishing())
		{
			//show dialog
			dialog.show();
		}
		return dialog;
	}
	
	public static void setAutoDismiss(boolean flag,long time)
	{
		if(flag)
		{
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dismisDialog();
				}
			},time);
		}
	}

	public static void setAutoDismiss(boolean flag, long time, final boolean finish)
	{
		if(flag)
		{
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dismisDialog();
					if(finish)
					{
						((Activity) context).finish();
					}
				}
			},time);
		}
	}



	public static boolean isNull()
	{
		return dialog==null? true:false;
	}


	public  static void dismisDialog(){
//		if(imageView!=null)
//			imageView.clearAnimation();
		if(dialog!=null)
		{
			dialog.dismiss();
			dialog= null;
		}
		
	}
	
	
}
