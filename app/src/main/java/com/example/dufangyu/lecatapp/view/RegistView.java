package com.example.dufangyu.lecatapp.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.MyToast;

/**
 * Created by dufangyu on 2017/9/11.
 */

public class RegistView extends ViewImpl{

    private TextView backtv;
    private ScrollView mScrollview;
    private EditText username_editor,pwd_editor,insure_editor;
    private TextView submitBtn;
    private ImageView logoImg;
    @Override
    public void initView() {
        logoImg = findViewById(R.id.regist_logo);
        backtv = findViewById(R.id.back_img);
        backtv.setVisibility(View.VISIBLE);
        mScrollview = findViewById(R.id.regist_scroll);
        username_editor = findViewById(R.id.username_editor);
        pwd_editor = findViewById(R.id.pwd_editor);
        insure_editor = findViewById(R.id.insure_editor);
        submitBtn = findViewById(R.id.submit_btn);





    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,backtv,submitBtn);
        EventHelper.focus(mPresent,username_editor,pwd_editor,insure_editor);
    }


    public boolean checkValid()
    {
        String username = username_editor.getText().toString().trim();
        String password = pwd_editor.getText().toString().trim();
        String again_pwd = insure_editor.getText().toString().trim();
        if(TextUtils.isEmpty(username) )
        {
            MyToast.showTextToast(mRootView.getContext(), "用户名不能为空");
            return false;
        }
        if( TextUtils.isEmpty(password))
        {
            MyToast.showTextToast(mRootView.getContext(), "密码不能为空");
            return false;
        }
        if(TextUtils.isEmpty(again_pwd))
        {
            MyToast.showTextToast(mRootView.getContext(), "确认密码不能为空");
            return false;
        }


        if(!password.equals(again_pwd))
        {
            MyToast.showTextToast(mRootView.getContext(), "两个密码不一致,请重新设置");
            return false;
        }
        return true;
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

}
