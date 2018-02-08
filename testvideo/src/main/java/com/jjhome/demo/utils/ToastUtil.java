package com.jjhome.demo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jaelyn26 on 2016/1/21.
 */
public class ToastUtil {

    public static void showToast(Context context, String msg){
        Toast toast =Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
