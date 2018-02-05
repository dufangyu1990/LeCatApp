package com.example.dufangyu.lecatapp.view;

import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.customview.TitleLinearLayout;
import com.example.dufangyu.lecatapp.helper.EventHelper;

/**
 * Created by dufangyu on 2017/9/20.
 */

public class UserInfoView extends ViewImpl{
    private TitleLinearLayout linearLayout_title;

    private TextView qrTv;

    private TextView backTv;
    private EditText loginnameEd,nameEd,phoneEd,addressEd;
    @Override
    public void initView() {
        backTv = findViewById(R.id.back_img);
        linearLayout_title = findViewById(R.id.titleLayout);
        linearLayout_title.setBackVisisble(true);
        linearLayout_title.setBackText("");
        linearLayout_title.setTitleSize(18.0f);
        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.userinfo));

        loginnameEd = findViewById(R.id.yonghuming_editor);
        nameEd = findViewById(R.id.name_editor);
        phoneEd = findViewById(R.id.phone_editor);
        addressEd = findViewById(R.id.address_editor);
        qrTv = findViewById(R.id.queren_btn);

      /*  phoneEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String editable = phoneEd.getText().toString();
                String str = Util.stringFilterForText(editable.toString());
                if(!editable.equals(str)){
                    phoneEd.setText(str);
                    //设置新的光标所在位置
                    phoneEd.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void bindEvent() {

        EventHelper.click(mPresent,qrTv,backTv);
    }


    public void initDatas(String loginName,String username,String phoneStr,String addressStr)
    {
        loginnameEd.setText(loginName);
        nameEd.setText(username);
        phoneEd.setText(phoneStr);
        addressEd.setText(addressStr);
    }

    public  void saveUserInfo()
    {
        MyApplication.getInstance().setStringPerference("yonghuming", nameEd.getText().toString().trim());
        MyApplication.getInstance().setStringPerference("phoneStr",phoneEd.getText().toString().trim());
        MyApplication.getInstance().setStringPerference("addressStr",addressEd.getText().toString().trim());
    }

    public String getValue(int id)
    {
        if(id == R.id.name_editor)
        {
            return nameEd.getText().toString().trim();
        }else if(id ==R.id.phone_editor)
        {
            return phoneEd.getText().toString().trim();
        }else if(id ==R.id.address_editor )
        {
            return addressEd.getText().toString().trim();
        }
        return "";
    }
}
