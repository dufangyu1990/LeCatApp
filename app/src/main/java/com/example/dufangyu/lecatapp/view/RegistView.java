package com.example.dufangyu.lecatapp.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.Util;

/**
 * Created by dufangyu on 2017/9/11.
 */

public class RegistView extends ViewImpl{

    private TextView backtv;
    private ScrollView mScrollview;
    private EditText username_editor,pwd_editor,code_editor;
    private TextView submitBtn;
    private TextView logoImg;
    private TextView verfycodeTv;
    @Override
    public void initView() {
        logoImg = findViewById(R.id.regist_logo);
        backtv = findViewById(R.id.back_img);
        backtv.setVisibility(View.VISIBLE);
        mScrollview = findViewById(R.id.regist_scroll);
        username_editor = findViewById(R.id.username_editor);
        pwd_editor = findViewById(R.id.pwd_editor);
        code_editor = findViewById(R.id.code_editor);
        submitBtn = findViewById(R.id.submit_btn);
        verfycodeTv = findViewById(R.id.verfycodeTv);


        username_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = username_editor.getText().toString().trim();
                if(Util.isMobile(value))
                {
                    if(!TextUtils.isEmpty(pwd_editor.getText().toString().trim())
                       &&!TextUtils.isEmpty(code_editor.getText().toString().trim())
                            )
                    {
                        submitBtn.setEnabled(true);
                    }else{
                        submitBtn.setEnabled(false);
                    }
                }else{
                    submitBtn.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pwd_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = pwd_editor.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    if(Util.isMobile(username_editor.getText().toString().trim()))
                    {
                        if(!TextUtils.isEmpty(code_editor.getText().toString().trim()))
                        {
                            submitBtn.setEnabled(true);
                        }else{
                            submitBtn.setEnabled(false);
                        }
                    }else{
                        submitBtn.setEnabled(false);
                    }
                }else{
                    submitBtn.setEnabled(false);
                }





            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = code_editor.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    if(Util.isMobile(username_editor.getText().toString().trim()))
                    {
                        if(!TextUtils.isEmpty(pwd_editor.getText().toString().trim()))
                        {
                            submitBtn.setEnabled(true);
                        }else{
                            submitBtn.setEnabled(false);
                        }
                    }else{
                        submitBtn.setEnabled(false);
                    }
                }else{
                    submitBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,backtv,submitBtn,verfycodeTv);
        EventHelper.focus(mPresent,username_editor,pwd_editor,code_editor);
    }





    public boolean isValidPhone()
    {
        String phoneNumber =username_editor.getText().toString().trim();
        return Util.isMobile(phoneNumber);
    }


    public String getValueById(int id)
    {
        if(id == R.id.username_editor)
        {
            return username_editor.getText().toString().trim();
        }else if(id ==R.id.pwd_editor )
        {
            return pwd_editor.getText().toString().trim();
        }
        return "";
    }


    public void scrollview()
    {
//        mScrollview.fullScroll(View.FOCUS_DOWN);
        mScrollview.scrollTo(0, mScrollview.getHeight() - logoImg.getHeight() );
    }




    public void setCodeValue()
    {
        code_editor.setText(Util.getRandomCode(6));
    }

    public void startCountTime(long millisUntilFinished)
    {
        verfycodeTv.setEnabled(false);
        verfycodeTv.setText(millisUntilFinished/1000+"s");

    }

    public void finishCountTime()
    {
        verfycodeTv.setEnabled(true);
        verfycodeTv.setText("获取验证码");
    }

}
