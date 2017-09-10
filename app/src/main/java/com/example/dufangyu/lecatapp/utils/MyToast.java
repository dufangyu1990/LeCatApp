package com.example.dufangyu.lecatapp.utils;

import android.content.Context;
import android.widget.Toast;


public class MyToast {
    private static Toast toast = null;

    public static void showTextToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
