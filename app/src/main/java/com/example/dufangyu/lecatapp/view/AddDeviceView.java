package com.example.dufangyu.lecatapp.view;

import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.customview.TitleLinearLayout;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.MyToast;

import static com.example.dufangyu.lecatapp.R.id.deviceId_ed;
import static com.example.dufangyu.lecatapp.R.id.nickname_ed;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class AddDeviceView extends ViewImpl{

    private TextView addDeviceBtn;
    private EditText nickNameEd;
    private EditText deviceIdEd;
    private TitleLinearLayout linearLayout_title;

    private TextView backTv;
    @Override
    public void initView() {


        backTv = findViewById(R.id.back_img);
        linearLayout_title = findViewById(R.id.titleLayout);
        linearLayout_title.setBackVisisble(true);
        linearLayout_title.setBackText(mRootView.getContext().getString(R.string.myself));
        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.add_device));

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
        EventHelper.click(mPresent,addDeviceBtn,backTv);
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
