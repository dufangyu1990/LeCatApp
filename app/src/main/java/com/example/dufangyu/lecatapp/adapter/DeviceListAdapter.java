package com.example.dufangyu.lecatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dufangyu.lecatapp.R;
import com.example.jjhome.network.Constants;
import com.example.jjhome.network.entity.DeviceListItemBean;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    private List<DeviceListItemBean> datas = new ArrayList<>();
    private Context context;

    public DeviceListAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<DeviceListItemBean> datas) {
        this.datas = new ArrayList<>(datas);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public DeviceListItemBean getItem(int position) {
        if (datas == null) {
            return null;
        }
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView titleTv;
        TextView type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device_list, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.item_title);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        DeviceListItemBean itemBean =  getItem(position);
        if (itemBean == null) {
            return null;
        }

        if (itemBean.getType().equals(Constants.TYPE_BELL)) {
            holder.type.setText("门铃");
        } else if (itemBean.getType().equals(Constants.TYPE_CAMERA)) {
            holder.type.setText("摄像头");
        }

        holder.titleTv.setText(itemBean.getDevice_id());
        return convertView;
    }
}
