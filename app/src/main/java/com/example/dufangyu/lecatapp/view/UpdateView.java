package com.example.dufangyu.lecatapp.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.utils.MyToast;

import static com.example.dufangyu.lecatapp.R.id.newVersionCode_editor;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class UpdateView extends ViewImpl{

    private TextView titletext,backtext,updateBtn;
    private EditText versionCode_ed;
    @Override
    public void initView() {
        titletext = findViewById(R.id.title_text);
        backtext = findViewById(R.id.back_img);
        backtext.setVisibility(View.VISIBLE);
        titletext.setText(mRootView.getContext().getString(R.string.update));
        backtext.setText(mRootView.getContext().getString(R.string.myself));
        updateBtn = findViewById(R.id.versionCode_submit_btn);
        versionCode_ed = findViewById(newVersionCode_editor);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backtext,updateBtn);
    }

    public boolean checkValid()
    {
        String versinCode = versionCode_ed.getText().toString().trim();
        if(TextUtils.isEmpty(versinCode))
        {
            MyToast.showTextToast(mRootView.getContext(), "版本号不能为空");
            return false;
        }
        return true;
    }
    public String getValueById(int id)
    {
        if(id == newVersionCode_editor)
        {
            return versionCode_ed.getText().toString().trim();
        }
        return "";
    }
}
