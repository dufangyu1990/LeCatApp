package com.example.dufangyu.lecatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.biz.IUpdate;
import com.example.dufangyu.lecatapp.biz.UpdateBiz;
import com.example.dufangyu.lecatapp.biz.UpdateListener;
import com.example.dufangyu.lecatapp.customview.CustomLoadDialog;
import com.example.dufangyu.lecatapp.present.ActivityPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.UpdateView;

/**
 * Created by dufangyu on 2017/9/13.
 */

public class UpdateActivity extends ActivityPresentImpl<UpdateView> implements View.OnClickListener{


    private IUpdate updateBiz;

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        updateBiz = new UpdateBiz();
    }

    public  static void actionStart(Context context)
    {
        Intent intent = new Intent(context,UpdateActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.versionCode_submit_btn:

                updateApp();
                break;
        }
    }


    private void updateApp()
    {
        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }

        if(mView.checkValid())
        {


            CustomLoadDialog.show(UpdateActivity.this,"",true,null,R.layout.logindialog);
            updateBiz.updateApp(mView.getValueById(R.id.newVersionCode_editor), new UpdateListener() {
                @Override
                public void updateSuccess() {
                    CustomLoadDialog.dismisDialog();
                    MyToast.showTextToast(getApplicationContext(),"更新成功");
                    finish();
                }

                @Override
                public void updateFail() {
                    CustomLoadDialog.dismisDialog();
                    MyToast.showTextToast(getApplicationContext(),"更新失败");
                }

            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateBiz.detachDataCallBackNull();
        updateBiz= null;
    }
}
