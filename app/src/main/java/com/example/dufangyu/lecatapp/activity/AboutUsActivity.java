package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.view.AboutUsView;


/**
 * Created by dufangyu on 2017/9/6.
 */

public class AboutUsActivity extends ActivityPresentImpl<AboutUsView> implements View.OnClickListener{
    public  static void actionStart(Context context)
    {
        Intent intent = new Intent(context,AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;
        }
    }
}
