package com.example.dufangyu.lecatapp.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.LoginActivity;
import com.example.dufangyu.lecatapp.activity.MyApplication;
import com.example.dufangyu.lecatapp.biz.IMyFragment;
import com.example.dufangyu.lecatapp.biz.MyFragmentBiz;
import com.example.dufangyu.lecatapp.listener.JumpToActivityListener;
import com.example.dufangyu.lecatapp.present.FragmentPresentImpl;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.ActivityControl;
import com.example.dufangyu.lecatapp.utils.MyAlertDialog;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.MyView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by dufangyu on 2017/8/31.
 */
@RuntimePermissions
public class MyFragment  extends FragmentPresentImpl<MyView> implements View.OnClickListener{

    private String username;
    private String password;
    private JumpToActivityListener mListener;
    private MyAlertDialog dialog;
    private Context context;
    private IMyFragment myFragmentBiz;


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            username = bundle.getString("username");
            password = bundle.getString("password");
            mView.setValue(R.id.my_name,username);

        }
        myFragmentBiz = new MyFragmentBiz();

    }



    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (JumpToActivityListener)activity;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.userinfo_rel:
                break;
            case R.id.changepwd_rel:
                mListener.jumpToModifyPawActivity(username,password);
                break;
            case R.id.mydevice_rel:
                break;
            case R.id.add_device_rel:
                MyFragmentPermissionsDispatcher.jumpWithCheck(this);
                break;
            case R.id.update_rel:
                mListener.jumpToUpdateActivity();
                break;
            case R.id.exitlogin:
                showExitDialog();
                break;
        }
    }



    @NeedsPermission(Manifest.permission.CAMERA)
    public void jump()
    {
        mListener.jumpToScanCodeActivity();
    }


    public void showExitDialog()
    {
        dialog = new MyAlertDialog(context).builder().setTitle(context.getResources().getString(R.string.exittitle))
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!TcpConnectUtil.p_bLinkCenterON)
                        {
                            MyToast.showTextToast(context,getResources().getString(R.string.badnetwork));
                            return;
                        }
                        myFragmentBiz.exitApp();
                        MyApplication.getInstance().setStringPerference("isFirst", "YES");
                        LoginActivity.actionStart(context,true);
                        ActivityControl.finishAll();

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.show();
    }


//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissions = new ArrayList<String>();
//
//            /***
//             * 读写权限为必须权限，用户如果禁止，则每次进入都会申请
//             */
////            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
////                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
////            }
////            if(checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED){
////                permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
////            }
////
////            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
////                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
////            }
////            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
////                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
////            }
//
//            if(context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                permissions.add(Manifest.permission.CAMERA);
//            }
//            if (permissions.size() > 0) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]),127);
//            }
//        }
//    }
//


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyFragmentPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    //用户选择拒绝的提示
    void showDenied()
    {
        MyToast.showTextToast(context,"拒绝将无法添加设备");
    }


    @OnNeverAskAgain(Manifest.permission.CAMERA)
   //用户选择不再询问后的提示
    void showNotAsk()
    {
        new AlertDialog.Builder(context).setMessage("该功能需要摄像头权限,不开启将无法正常添加设备,只能手动在设置里面赋予权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();


    }
    @OnShowRationale(Manifest.permission.CAMERA)
    //提示用户为何要开启此权限
    void showWhy(final PermissionRequest request)
    {
        new AlertDialog.Builder(context).setMessage("添加设备需要摄像头扫描")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).show();
    }



}
