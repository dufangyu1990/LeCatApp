package com.example.dufangyu.lecatapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dufangyu on 2017/6/20.
 */

public class ActivityControl {

    public static ArrayList<Activity> activities = new ArrayList<>();


    public static void addActivity(Activity activity)
    {
        activities.add(activity);
//        LogUtil.d("dfy","activities  size = "+activities.size());
    }



    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
//        LogUtil.d("dfy","activities  size = "+activities.size());
    }


    public static void finishAll()
    {
        for(Activity activity:activities)
        {
            if(!activity.isFinishing())
            {
                activity.finish();
            }
        }
    }

    public static String  getTopActicvityName(Context context)
    {
        String className="";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            className = cpn.getClassName();
        }
        return className;
    }

    public static void clearOtherActivity(String name)
    {
//        LogUtil.d("dfy", "name = " + name);
        for(Activity activity:activities){
//            LogUtil.d("dfy","simpleName = "+activity.getClass().getSimpleName());
            if(activity.getClass().getSimpleName().equals(name))
            {
                continue;
            }else{
                activity.finish();
            }

        }
    }

}
