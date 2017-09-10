package com.example.dufangyu.lecatapp.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.Util;


/**
 * Created by dufangyu on 2017/7/3.
 */

public class SplashView extends ViewImpl implements Runnable{

    private ImageView startImg;
    private AlertDialog dialog;
    private TextView oktv;
    private TextView cancletv;
    private View view;

    @Override
    public void initView() {
        startImg =findViewById(R.id.startimg);



    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    public void bindEvent() {

        view =((Activity)mPresent).getLayoutInflater().inflate(R.layout.dialog_update, null);
        oktv = (TextView) view.findViewById(R.id.update_ok);
        cancletv = (TextView) view.findViewById(R.id.update_no);
        EventHelper.click(mPresent,oktv,cancletv);
    }

    public void startAnim()
    {
        startImg.postDelayed(this,400);
    }




    @Override
    public void run() {
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);//动画效果
        scaleAnim.setFillAfter(true);//动画持续执行
        scaleAnim.setDuration(2000);//3秒

        scaleAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**结束时调用*/
            @Override
            public void onAnimationEnd(Animation animation) {
                mPresent.presentCallBack("","","");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startImg.startAnimation(scaleAnim);
    }


    public void showUpdateDialog()
    {
        dialog = Util.alterDialog(mRootView.getContext(), view);
    }

    public void disMisDialog()
    {
        dialog.dismiss();
        dialog = null;
    }
}
