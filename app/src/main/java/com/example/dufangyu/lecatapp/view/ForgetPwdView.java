package com.example.dufangyu.lecatapp.view;

import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;

/**
 * Created by dufangyu on 2018/2/1.
 */

public class ForgetPwdView extends ViewImpl{


    private TextView backImg;
    private EditText forget_username_editor,forget_pwd_editor,forget_pwd_againt_editor,forget_code_editor;
    private TextView forgetCodeTv;
    @Override
    public void initView() {
        backImg = findViewById(R.id.back_img);
        forget_username_editor = findViewById(R.id.forget_username_editor);
        forget_pwd_editor = findViewById(R.id.forget_pwd_editor);
        forget_pwd_againt_editor = findViewById(R.id.forget_newpwd_editor);
        forget_code_editor = findViewById(R.id.forget_code_editor);
        forgetCodeTv = findViewById(R.id.forget_verfycodeTv);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backImg);
    }
}
