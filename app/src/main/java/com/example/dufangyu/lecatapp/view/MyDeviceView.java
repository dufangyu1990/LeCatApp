package com.example.dufangyu.lecatapp.view;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.adapter.DeviceAdapter;
import com.example.dufangyu.lecatapp.bean.DeviceBean;
import com.example.dufangyu.lecatapp.customview.DefineLoadMoreView;
import com.example.dufangyu.lecatapp.helper.EventHelper;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dufangyu on 2017/9/14.
 */

public class MyDeviceView extends ViewImpl implements SwipeRefreshLayout.OnRefreshListener,SwipeMenuRecyclerView.LoadMoreListener,SwipeItemClickListener {
    private TextView titletext,backtext;
    private SwipeRefreshLayout mRefreshLayout;
    private SwipeMenuRecyclerView mRecyclerView;
    private DeviceAdapter deviceAdapter;
    private List<DeviceBean> datalist = new ArrayList<>();
    @Override
    public void initView() {
        titletext = findViewById(R.id.title_text);
        backtext = findViewById(R.id.back_img);
        backtext.setVisibility(View.VISIBLE);
        titletext.setText(mRootView.getContext().getString(R.string.my_device));
        backtext.setText(mRootView.getContext().getString(R.string.myself));
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this); // 刷新监听。

        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(mRootView.getContext(), R.color.divider_color)));
        //点击item监听
        mRecyclerView.setSwipeItemClickListener(this);
        // 自定义的核心就是DefineLoadMoreView类。
        DefineLoadMoreView loadMoreView = new DefineLoadMoreView(mRootView.getContext());
        mRecyclerView.addFooterView(loadMoreView); // 添加为Footer。
        mRecyclerView.setLoadMoreView(loadMoreView); // 设置LoadMoreView更新监听。
        mRecyclerView.setLoadMoreListener(this); // 加载更多的监听。



    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mydevice;
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresent,backtext);
    }

    @Override
    public void onRefresh() {



    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(View itemView, int position) {

    }


    public void setDeviceList()
    {
        datalist.clear();
        int size = DataManager.p_intDeviceCount;
        for(int i =0;i<size;i++)
        {
            DeviceBean bean = new DeviceBean();
            bean.setDeviceId(DataManager.p_strDeviceList[i][1]);
            bean.setNickName(DataManager.p_strDeviceList[i][2]);
            datalist.add(bean);
        }
        showUI();
        mRefreshLayout.setRefreshing(false);
    }


    public void showUI()
    {
        if(deviceAdapter==null){
            deviceAdapter = new DeviceAdapter(datalist);
            mRecyclerView.setAdapter(deviceAdapter);
        }else{
            deviceAdapter.updateList(datalist);
        }
    }


    /**
     *
     * @param isRefreshing 是否出现下拉动画
     */
    public void startRefrsh(final boolean isRefreshing)
    {
        mRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                //只支持出现下拉刷新动画
                mRefreshLayout.setRefreshing(isRefreshing);
            }



        });
    }


}
