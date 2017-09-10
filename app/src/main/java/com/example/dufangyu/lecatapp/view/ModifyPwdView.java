package com.example.dufangyu.lecatapp.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;


/**
 * Created by dufangyu on 2017/9/6.
 */

public class ModifyPwdView extends ViewImpl{

    private TextView titletext,backtext,submit_btn;
    private String old_password,new_password,repeat_password;
    private EditText old_editor,new_editor,insure_editor;

    @Override
    public void initView() {
        titletext = findViewById(R.id.title_text);
        backtext = findViewById(R.id.back_img);
        backtext.setVisibility(View.VISIBLE);
        titletext.setText(mRootView.getContext().getString(R.string.changePas));
        backtext.setText(mRootView.getContext().getString(R.string.myself));
        submit_btn = findViewById(R.id.submit_btn);
        old_editor = findViewById(R.id.old_editor);
        new_editor = findViewById(R.id.new_editor);
        insure_editor = findViewById(R.id.insure_editor);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modifypwd;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backtext,submit_btn);

    }

    public boolean checkValid(String pwdStr)
    {
        old_password = old_editor.getText().toString().trim();
        new_password= new_editor.getText().toString().trim();
        repeat_password = insure_editor.getText().toString().trim();
        LogUtil.d("dfy","old_password = "+old_password);
        LogUtil.d("dfy","pwdStr = "+pwdStr);

        if(TextUtils.isEmpty(old_password) || TextUtils.isEmpty(new_password)||TextUtils.isEmpty(repeat_password))
        {
            MyToast.showTextToast(mRootView.getContext(), "密码不能为空");
            return false;
        }

        if(!old_password.equals(pwdStr))
        {
            MyToast.showTextToast(mRootView.getContext(), "原始密码错误");
            return false;
        }

        if(new_password.equals(old_password))
        {
            MyToast.showTextToast(mRootView.getContext(), "新密码不能和原始密码一样,请重新设置");
            return false;
        }

        if(!new_password.equals(repeat_password))
        {
            MyToast.showTextToast(mRootView.getContext(), "新密码和重复密码不一致,请重新设置");
            return false;
        }
        return true;
    }


    public String getNewPwdStr()
    {
        return new_password;
    }



}
