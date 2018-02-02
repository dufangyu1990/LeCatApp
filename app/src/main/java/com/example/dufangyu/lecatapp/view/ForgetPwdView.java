package com.example.dufangyu.lecatapp.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.utils.Util;

/**
 * Created by dufangyu on 2018/2/1.
 */

public class ForgetPwdView extends ViewImpl{


    private TextView backImg;
    private EditText forget_username_editor,forget_pwd_editor,forget_pwd_againt_editor,forget_code_editor;
    private TextView forgetCodeTv;
    private TextView resetPwd;
    @Override
    public void initView() {
        backImg = findViewById(R.id.back_img);
        resetPwd = findViewById(R.id.resetPwd);
        forget_username_editor = findViewById(R.id.forget_username_editor);
        forget_pwd_editor = findViewById(R.id.forget_pwd_editor);
        forget_pwd_againt_editor = findViewById(R.id.forget_newpwd_editor);
        forget_code_editor = findViewById(R.id.forget_code_editor);
        forgetCodeTv = findViewById(R.id.forget_verfycodeTv);


        forget_username_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = forget_username_editor.getText().toString().trim();
                if(Util.isMobile(value))
                {
                    if(!TextUtils.isEmpty(forget_pwd_editor.getText().toString().trim())
                            &&!TextUtils.isEmpty(forget_code_editor.getText().toString().trim())
                            && !TextUtils.isEmpty(forget_pwd_againt_editor.getText().toString().trim())
                            )
                    {
                        resetPwd.setEnabled(true);
                    }else{
                        resetPwd.setEnabled(false);
                    }
                }else{
                    resetPwd.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        forget_pwd_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = forget_pwd_editor.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    if(Util.isMobile(forget_username_editor.getText().toString().trim()))
                    {
                        if(!TextUtils.isEmpty(forget_code_editor.getText().toString().trim())
                                && !TextUtils.isEmpty(forget_pwd_againt_editor.getText().toString().trim()))
                        {
                            resetPwd.setEnabled(true);
                        }else{
                            resetPwd.setEnabled(false);
                        }
                    }else{
                        resetPwd.setEnabled(false);
                    }
                }else{
                    resetPwd.setEnabled(false);
                }





            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forget_code_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = forget_code_editor.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    if(Util.isMobile(forget_username_editor.getText().toString().trim()))
                    {
                        if(!TextUtils.isEmpty(forget_pwd_editor.getText().toString().trim())
                                && !TextUtils.isEmpty(forget_pwd_againt_editor.getText().toString().trim())
                                )
                        {
                            resetPwd.setEnabled(true);
                        }else{
                            resetPwd.setEnabled(false);
                        }
                    }else{
                        resetPwd.setEnabled(false);
                    }
                }else{
                    resetPwd.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forget_pwd_againt_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = forget_pwd_againt_editor.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    if(Util.isMobile(forget_username_editor.getText().toString().trim()))
                    {
                        if(!TextUtils.isEmpty(forget_pwd_editor.getText().toString().trim())
                                && !TextUtils.isEmpty(forget_code_editor.getText().toString().trim())
                                )
                        {
                            resetPwd.setEnabled(true);
                        }else{
                            resetPwd.setEnabled(false);
                        }
                    }else{
                        resetPwd.setEnabled(false);
                    }
                }else{
                    resetPwd.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }







    @Override
    public int getLayoutId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backImg,resetPwd);
    }





    public boolean checkPwd()
    {
        String pwd =forget_pwd_editor.getText().toString().trim();
        String pwd_again = forget_pwd_againt_editor.getText().toString().trim();
        if(!pwd.equals(pwd_again))
        {
            MyToast.showTextToast(mRootView.getContext(),"两次密码不一致");
            return false;
        }
        return true;
    }




    public boolean isValidPhone()
    {
        String phoneNumber =forget_username_editor.getText().toString().trim();
        return Util.isMobile(phoneNumber);
    }

    public void setCodeValue()
    {
        forget_code_editor.setText(Util.getRandomCode(6));
    }
    public void startCountTime(long millisUntilFinished)
    {
        forgetCodeTv.setEnabled(false);
        forgetCodeTv.setText(millisUntilFinished/1000+"s");

    }

    public void finishCountTime()
    {
        forgetCodeTv.setEnabled(true);
        forgetCodeTv.setText("获取验证码");
    }

}
