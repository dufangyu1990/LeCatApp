package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.view.RegistView;

/**
 * Created by dufangyu on 2017/9/11.
 */

public class RegistActivity extends ActivityPresentImpl<RegistView> implements View.OnClickListener{


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,RegistActivity.class);
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
