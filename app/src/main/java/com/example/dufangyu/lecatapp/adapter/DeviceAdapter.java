package com.example.dufangyu.lecatapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.bean.DeviceBean;

import java.util.List;

/**
 * Created by dufangyu on 2017/9/14.
 */

public class DeviceAdapter  extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{
    StringBuffer tempsb = new StringBuffer();
    private List<DeviceBean> datalist;

    public DeviceAdapter(List<DeviceBean> datalist) {
        this.datalist = datalist;
    }

    public void updateList(List<DeviceBean> datalist)
    {
        this.datalist = datalist;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(datalist.get(position).getDeviceId());
        String nickName = datalist.get(position).getNickName();
        if(TextUtils.isEmpty(nickName))
        {
            holder.tvName.setText(nickName);
        }else{
            tempsb.delete(0, tempsb.length());
            tempsb.append("(").append(nickName).append(")");
            holder.tvName.setText(tempsb.toString());
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvName = (TextView) itemView.findViewById(R.id.tv_nickname);
        }


    }
}
