package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.IModifyPwd;
import com.example.dufangyu.lecatapp.biz.ModifyBiz;
import com.example.dufangyu.lecatapp.biz.ModifyPwdListener;
import com.example.dufangyu.lecatapp.customview.CustomDialog;
import com.example.dufangyu.lecatapp.customview.CustomLoadDialog;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.ModifyPwdView;


/**
 * Created by dufangyu on 2017/9/6.
 */

public class ModifyPwdActivity extends ActivityPresentImpl<ModifyPwdView> implements View.OnClickListener{


    private IModifyPwd modifyPwdBiz;
    private String loginName;//登录名
    private String loginPwd;//登录密码
    private String newPassword;//新密码

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        modifyPwdBiz = new ModifyBiz();
        loginName = getIntent().getStringExtra("login_name");
        loginPwd = getIntent().getStringExtra("pwdStr");

    }

    public  static void actionStart(Context context,String loginName,String loginPwd)
    {
        Intent intent = new Intent(context,ModifyPwdActivity.class);
        intent.putExtra("login_name",loginName);
        intent.putExtra("pwdStr",loginPwd);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit_btn:
                modifyPwdAction();
                break;
            case R.id.back_img:
                if(modifyPwdBiz!=null)
                {
                    modifyPwdBiz.detachDataCallBackNull();
                    modifyPwdBiz = null;
                }
                finish();
                break;
        }
    }

    @Override
    public void pressAgainExit() {
        if(modifyPwdBiz!=null)
        {
            modifyPwdBiz.detachDataCallBackNull();
            modifyPwdBiz=  null;
        }
        finish();
    }

    private void modifyPwdAction()
    {
        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }

        if(mView.checkValid(loginPwd))
        {

            newPassword = mView.getNewPwdStr();
            CustomLoadDialog.show(ModifyPwdActivity.this,"",true,null,R.layout.logindialog);

            modifyPwdBiz.modifyPwd(loginName, newPassword, new ModifyPwdListener() {
                @Override
                public void modifySuccess() {
                    CustomLoadDialog.dismisDialog();
                    CustomDialog.show(ModifyPwdActivity.this, getResources().getString(R.string.modifypwdsuccess), true, null, R.layout.text_dialog);
                    CustomDialog.setAutoDismiss(true,1500,true);
                    MyApplication.getInstance().setStringPerference("Password", newPassword);

                }

                @Override
                public void modifyFail() {
                    CustomLoadDialog.dismisDialog();
                    CustomDialog.show(ModifyPwdActivity.this, getResources().getString(R.string.modifypwdfail), true, null, R.layout.text_dialog);
                    CustomDialog.setAutoDismiss(true,1500);
                }
            });
        }


    }





}
