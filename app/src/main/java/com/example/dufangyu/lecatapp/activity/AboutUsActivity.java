package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;

import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.view.AboutUsView;


/**
 * Created by dufangyu on 2017/9/6.
 */

public class AboutUsActivity extends ActivityPresentImpl<AboutUsView> {
    public  static void actionStart(Context context)
    {
        Intent intent = new Intent(context,AboutUsActivity.class);
        context.startActivity(intent);
    }

}
