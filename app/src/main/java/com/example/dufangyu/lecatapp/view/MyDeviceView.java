package com.example.dufangyu.lecatapp.view;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.adapter.DeviceAdapter;
import com.example.dufangyu.lecatapp.bean.DeviceBean;
import com.example.dufangyu.lecatapp.customview.TitleLinearLayout;
import com.example.dufangyu.lecatapp.manager.DataManager;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dufangyu on 2017/9/14.
 */

public class MyDeviceView extends ViewImpl implements SwipeItemClickListener {
    private SwipeRefreshLayout mRefreshLayout;
    private SwipeMenuRecyclerView mRecyclerView;
    private DeviceAdapter deviceAdapter;
    private List<DeviceBean> datalist = new ArrayList<>();
    private int deletePos;
    private TitleLinearLayout linearLayout_title;
    @Override
    public void initView() {



        linearLayout_title = findViewById(R.id.titleLayout);
        linearLayout_title.setBackVisisble(true);
        linearLayout_title.setBackText(mRootView.getContext().getString(R.string.myself));
        linearLayout_title.setTitleText(mRootView.getContext().getString(R.string.my_device));


        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
//        mRefreshLayout.setOnRefreshListener(this); // 刷新监听。
        mRefreshLayout.setEnabled(false);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(mRootView.getContext(), R.color.divider_color)));
        //点击item监听
        mRecyclerView.setSwipeItemClickListener(this);
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        // 自定义的核心就是DefineLoadMoreView类。
//        DefineLoadMoreView loadMoreView = new DefineLoadMoreView(mRootView.getContext());
//        mRecyclerView.addFooterView(loadMoreView); // 添加为Footer。
//        mRecyclerView.setLoadMoreView(loadMoreView); // 设置LoadMoreView更新监听。
//        mRecyclerView.setLoadMoreListener(this); // 加载更多的监听。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mydevice;
    }

    @Override
    public void bindEvent() {
    }





    @Override
    public void onItemClick(View itemView, int position) {

    }
    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = mRootView.getContext().getResources().getDimensionPixelSize(R.dimen.margin70);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加右侧的，如果不添加，则右侧不会出现菜单。

                SwipeMenuItem deleteItem = new SwipeMenuItem(mRootView.getContext())
                        .setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

        }
    };






    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {

                String deviceId = datalist.get(adapterPosition).getDeviceId();
                deletePos = adapterPosition;
                mPresent.presentCallBack(deviceId,"","");
            }
        }
    };





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

    //刷新Adapter
    public void notifyAdapterRefresh()
    {
        datalist.remove(deletePos);
        deviceAdapter.updateList(datalist);
    }



}
