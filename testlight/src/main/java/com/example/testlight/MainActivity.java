package com.example.testlight;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Button testBtn;
    private boolean gpiostatus=true;
    public  OutputStream Power_out = null;//by zhu
    public String[] lightsArr = new String[]{"/sys/devices/aptt/driver/blue",
            "/sys/devices/aptt/driver/red","/sys/devices/aptt/driver/green"};

    private int computer;
    private File file;
    private boolean isSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getPersimmions();
        testBtn = (Button)findViewById(R.id.testbtn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testBtn.performClick();
            }
        },3000);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按钮监听事件
                Toast.makeText(MainActivity.this,"测试点击事件",Toast.LENGTH_LONG).show();
                byte[] power_on_data = new byte[]{'1', 0, 0};
                Log.d("dfy","gpiostatus = "+gpiostatus);
                if (gpiostatus) {
                    power_on_data = new byte[]{'1', 0, 0};//拉高电平
                    isSelected = true;
                } else {
                    power_on_data = new byte[]{'0', 0, 0};//拉低
                    isSelected = false;
                }
                gpiostatus = !gpiostatus;
                LedPowerControl(power_on_data);
            }
        });
    }


    ///获取驱动设备节点
    public  void OpenPowerControl() {
        ClosePowerControl();
        if(isSelected)
        {
            computer=(int)(Math.random()*3);
            file= new File(lightsArr[computer]);
        }

//        File file = new File("/sys/devices/aptt/driver/blue");
        ///sys/devices/aptt/driver/green
        ///sys/devices/aptt/driver/blue
        if (file.exists()) {
            try {
                Power_out = new BufferedOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block

                Log.d("dfy","e ="+e.toString());
                e.printStackTrace();
            }
        }
    }
    ///关闭驱动设备节点
    public  void ClosePowerControl() {
        if (Power_out != null) {
            try {
                Power_out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Power_out = null;
        }
    }
    ///控制驱动设备节点高低电平。

    public  void LedPowerControl(byte[] data) {

        OpenPowerControl();
        try {
            if (Power_out != null) {
                Power_out.write(data);
                Power_out.flush();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    testBtn.performClick();
                }
            },5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//    }
//
//
//
//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissions = new ArrayList<String>();
//
//            /***
//             * 读写权限为必须权限，用户如果禁止，则每次进入都会申请
//             */
//
//            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//            if (permissions.size() > 0) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
//            }
//        }
//    }



}
