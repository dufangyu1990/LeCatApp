package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.view.ForgetPwdView;

/**
 * Created by dufangyu on 2018/2/1.
 */

public class ForgetPwdActivity extends ActivityPresentImpl<ForgetPwdView> implements View.OnClickListener{





    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;
        }

    }


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,ForgetPwdActivity.class);
        context.startActivity(intent);
    }
}
