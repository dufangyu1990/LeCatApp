package com.example.dufangyu.lecatapp.view;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.MyToast;

import static com.example.dufangyu.lecatapp.R.id.deviceId_ed;
import static com.example.dufangyu.lecatapp.R.id.nickname_ed;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceView extends ViewImpl{

    private TextView titletext,backtext,addDeviceBtn;
    private EditText nickNameEd;
    private EditText deviceIdEd;

    @Override
    public void initView() {
        titletext = findViewById(R.id.title_text);
        backtext = findViewById(R.id.back_img);
        backtext.setVisibility(View.VISIBLE);
        titletext.setText(mRootView.getContext().getString(R.string.add_device));
        backtext.setText(mRootView.getContext().getString(R.string.myself));
        addDeviceBtn = findViewById(R.id.add_device);
        nickNameEd = findViewById(nickname_ed);
        deviceIdEd = findViewById(deviceId_ed);


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
        String deviceId = deviceIdEd.getText().toString().trim();

        if(deviceId.length()!=16)
        {
            MyToast.showTextToast(mRootView.getContext(), "请输入16有效设备号");
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

    public void setDeviceIdValue(String deviceId)
    {
        deviceIdEd.setText(deviceId);
    }
}
