package com.jjhome.demo.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jjhome.network.Constants;
import com.example.jjhome.network.entity.EventBean;
import com.jjhome.demo.R;
import com.jjhome.demo.utils.ToastUtil;

public class MyPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(Constants.EVENT_BUNDLE);
        EventBean eventBean = (EventBean) bundle.getSerializable(Constants.EVENT_EXTRA);
        if (eventBean != null) {
            Log.d("test",eventBean.getEventTime()+"");
            switch (eventBean.getEventType()) {
                case Constants.EVENT_TYPE_BELL:
                    ToastUtil.showToast(context, "有人按门铃" + eventBean.getDeviceId());
                    notifyShare(context, 1, "hhhh", "hahah");

                    break;
                case Constants.EVENT_TYPE_BATTERY:
                    ToastUtil.showToast(context, eventBean.getDeviceId() + "电量剩余" + eventBean.getBatteryValue() + "%");
                    break;
                case Constants.EVENT_TYPE_MOTION:
                    ToastUtil.showToast(context, eventBean.getDeviceId() + " 移动侦测");
                    break;
                case Constants.EVENT_TYPE_PIR:
                    ToastUtil.showToast(context, eventBean.getDeviceId() + " 人体感应");
                    break;
                case Constants.EVENT_TYPE_SHARE:
                    ToastUtil.showToast(context, "有人分享给你设备" + eventBean.getDeviceId());
                    break;
                case Constants.EVENT_TYPE_UNSHARE:
                    ToastUtil.showToast(context, "有人取消分享了设备" + eventBean.getDeviceId());
                    break;
                case Constants.EVENT_TYPE_REPLACE:
                    ToastUtil.showToast(context, "有人添加了你的设备" + eventBean.getDeviceId());
                    break;
                case Constants.EVENT_TYPE_VOICE:
                    ToastUtil.showToast(context, eventBean.getDeviceId() + " 声音侦测");
                    break;
            }
        }
    }

    /**
     * 显示分享的通知
     *
     * @param id
     * @param title
     * @param content
     */
    public void notifyShare(Context context,int id, String title, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.pb_pause)
                .setTicker(content).setContentTitle(title).setContentText(content).setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL).setSound(Uri.parse("")).setVibrate(null).build();
        manager.notify(id, notification);
    }
}
