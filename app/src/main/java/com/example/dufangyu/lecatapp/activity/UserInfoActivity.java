package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.IUserInfo;
import com.example.dufangyu.lecatapp.biz.UserInfoBiz;
import com.example.dufangyu.lecatapp.biz.UserInfoListener;
import com.example.dufangyu.lecatapp.customview.CustomDialog;
import com.example.dufangyu.lecatapp.customview.CustomLoadDialog;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.UserInfoView;

/**
 * Created by dufangyu on 2017/9/20.
 */

public class UserInfoActivity extends ActivityPresentImpl<UserInfoView> implements View.OnClickListener,UserInfoListener{




    private IUserInfo userInfoBiz;
    private String yonghuming;
    private String phoneStr;
    private String addressStr;
    private String loginName;

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        userInfoBiz = new UserInfoBiz(this);

        loginName = getIntent().getStringExtra("login_name");
        yonghuming =  MyApplication.getInstance().getStringPerference("yonghuming");
        phoneStr =  MyApplication.getInstance().getStringPerference("phoneStr");
        addressStr =  MyApplication.getInstance().getStringPerference("addressStr");
        mView.initDatas(loginName,yonghuming,phoneStr,addressStr);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.queren_btn:
                submitToServier();
                break;
        }
    }


    private void submitToServier()
    {

        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }
        CustomLoadDialog.show(UserInfoActivity.this,"",true,null,R.layout.logindialog);
        userInfoBiz.modifyUser(loginName,mView.getValue(R.id.name_editor),mView.getValue(R.id.phone_editor),mView.getValue(R.id.address_editor));
    }


    @Override
    public void modifyUserSuccess() {
        CustomLoadDialog.dismisDialog();
        CustomDialog.show(UserInfoActivity.this, getResources().getString(R.string.modifyusersuccess), true, null, R.layout.text_dialog);
        CustomDialog.setAutoDismiss(true,1500,true);
        mView.saveUserInfo();


    }

    @Override
    public void modifyUserFail() {
        CustomLoadDialog.dismisDialog();
        CustomDialog.show(UserInfoActivity.this, getResources().getString(R.string.modifyuserfail), true, null, R.layout.text_dialog);
        CustomDialog.setAutoDismiss(true,1500);
    }



    public  static void actionStart(Context context, String loginName)
    {
        Intent intent = new Intent(context,UserInfoActivity.class);
        intent.putExtra("login_name",loginName);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfoBiz.detachDataCallBackNull();
        userInfoBiz = null;
    }
}
