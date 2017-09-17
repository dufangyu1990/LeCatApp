package com.example.dufangyu.lecatapp.present;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dufangyu.lecatapp.CallBack.NetCallBackImp;
import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.helper.GenericHelper;
import com.example.dufangyu.lecatapp.socketUtils.TcpConnectUtil;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.example.dufangyu.lecatapp.utils.MyToast;
import com.example.dufangyu.lecatapp.view.IView;

import static com.example.dufangyu.lecatapp.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPLINK;
import static com.example.dufangyu.lecatapp.utils.Constant.TCPNONET;


/**
 * Created by dufangyu on 2017/6/13.
 */

public class FragmentPresentImpl<T extends IView> extends Fragment implements IPresent<T> {

    protected  T mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        beforeViewCreate(savedInstanceState);
        try {
            mView=getViewClass().newInstance();
            View view = mView.createView(inflater,container);
            mView.bindPresenter(this);
            mView.bindEvent();
            TcpConnectUtil.getTcpInstance().setNetCallBack(netCallBackImp);
            afterViewCreate(savedInstanceState);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }

    @Override
    public void beforeViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void presentCallBack(String param1, String param2, String params3) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private NetCallBackImp netCallBackImp =new NetCallBackImp() {
        @Override
        public void runOnUI(int stateCode) {
            switch (stateCode)
            {
                case TCPNONET:
                    MyToast.showTextToast(getContext(),getResources().getString(R.string.badnetwork));
                    break;
                case TCPDISLINK:
                    LogUtil.d("dfy", "与服务器连接断开");
                    doNetDisConnect();
                    break;
                case TCPLINK:
                    LogUtil.d("dfy","与服务器连接成功");
                    doNetConnect();
                    break;

            }
        }
    };

    public void doNetDisConnect(){

    }

    public void doNetConnect(){

    }

}
