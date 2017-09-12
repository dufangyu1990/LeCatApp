package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int progress;
    private ProgressView mPrsBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrsBar = (ProgressView) findViewById(R.id.udfprsbar);
        progress = 0;

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if(progress <=100){
                            // 持续刷新，不停的onDraw
                            while (progress <= 100) {
                                mPrsBar.setProgress(progress += 5);

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            while (progress >= 0) {
                                mPrsBar.setProgress(progress -= 5);

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
