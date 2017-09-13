package com.example.dufangyu.lecatapp.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.MyToast;

import static com.example.dufangyu.lecatapp.R.id.nickname_ed;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceView extends ViewImpl{

    private TextView titletext,backtext,addDeviceBtn;
    private EditText nickNameEd;
    @Override
    public void initView() {
        titletext = findViewById(R.id.title_text);
        backtext = findViewById(R.id.back_img);
        backtext.setVisibility(View.VISIBLE);
        titletext.setText(mRootView.getContext().getString(R.string.add_device));
        backtext.setText(mRootView.getContext().getString(R.string.myself));
        addDeviceBtn = findViewById(R.id.add_device);
        nickNameEd = findViewById(nickname_ed);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_device;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backtext,addDeviceBtn);
    }

    public boolean checkValid()
    {
        String nickname = nickNameEd.getText().toString().trim();
        if(TextUtils.isEmpty(nickname))
        {
            MyToast.showTextToast(mRootView.getContext(), "版本号不能为空");
            return false;
        }
        return true;
    }
    public String getValueById(int id)
    {
        if(id == nickname_ed)
        {
            return nickNameEd.getText().toString().trim();
        }
        return "";
    }
}
