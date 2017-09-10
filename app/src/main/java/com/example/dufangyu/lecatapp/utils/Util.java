package com.example.dufangyu.lecatapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by dufangyu on 2016/7/19.
 */
public class Util {


    /**
     * 获取车辆状态
     * @param context
     * @return
     */
//    public static String getCarState(Context context, int position)
//    {
//        String strvalue="";
//        String[] strArr = context.getResources().getStringArray(R.array.statecontent);
//        strvalue = strArr[position];
//        return strvalue;
//
//    }


    /**
     * 获取当前日期
     * @param date
     * @return
     */
    public static String getToady(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }


    public static String getToady2(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getToady3(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取以当前日期为结束日期一个月内的时间：
     * @param dateNow
     * @return
     */
    public static String getBeforeMonth(Date dateNow)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateBefore = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(dateNow);

        cal.add(Calendar.MONTH, -1);

        dateBefore = cal.getTime();

        return sdf.format(dateBefore);
    }





    /**
     *
     * 获取当前日期前一天
     * @param date
     * @return
     */
    public static String getYesterDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    /**
     * 获取前天日期
     * @param date
     * @return
     */
    public static String getQianTianDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取后一天日期
     * @param date
     * @return
     */
    public static String getTomorrowDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    /**
     * 两个时间之间的差
     */

    public static String differBetweenDates(String starttime, String endtime)
    {
        String result="";
//        LogUtil.d("dfy", "startTime = " + starttime);
//        LogUtil.d("dfy", "endtime = " + endtime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = sdf.parse(starttime);
            Date endDate = sdf.parse(endtime);
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
             long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = endDate.getTime() - startDate.getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
             long sec = diff % nd % nh % nm / ns;

//            result =hour + "小时" + min + "分钟"+ sec + "秒";
//            LogUtil.d("dfy","diff = "+diff);

            /**
             * 向上取整:Math.ceil()   //只要有小数都+1
             向下取整:Math.floor()   //不取小数
             四舍五入:Math.round()  //四舍五入**/
            result =Math.round(Math.ceil((float)diff/nm))+"分";
        } catch (ParseException e) {

        }
        return result;
    }




    //两个时间相差多少天
    public static String differDaysBetweenDates(String starttime, String endtime)
    {
        String result="";
//        LogUtil.d("dfy", "startTime = " + starttime);
//        LogUtil.d("dfy", "endtime = " + endtime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(starttime);
            Date endDate = sdf.parse(endtime);
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = endDate.getTime() - startDate.getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            long sec = diff % nd % nh % nm / ns;

//            result =hour + "小时" + min + "分钟"+ sec + "秒";
//            LogUtil.d("dfy","day = "+day);
            result =day+"";
        } catch (ParseException e) {

        }
        return result;
    }


    public static String getHour(float value)
    {
        String result="";
        try {
            int time = (int)value;
            int hour = time/60;
            result =hour+"时";
        } catch (Exception e) {

        }
        return result;
    }


    public static String getMin(float value)
    {
        String result="";
        try {
            int time = (int)value;

            int min = time%60;
            result =min+"分";
        } catch (Exception e) {

        }
        return result;
    }


    public static String getWorkTime(long worktime )
    {
        String result="";

        long time = worktime*1000;
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 计算差多少天
        long day = time / nd;
        // 计算差多少小时
        long hour = time % nd / nh;
        // 计算差多少分钟
        long min = time % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = time % nd % nh % nm / ns;
        result =hour + "小时" + min + "分钟"+ sec + "秒";
        return result;
    }


    public static boolean isValidToken()
    {
        long resultTime=0;

        long logintime = MyApplication.getInstance().getLongPerference("logintime");

        long curenttime = System.currentTimeMillis();
        resultTime = curenttime-logintime;
        long nm = 1000 * 60;
        long min = resultTime/nm;
        LogUtil.d("dfy","min = "+min);
        int diff = (int)min;
        LogUtil.d("dfy","diff = "+diff);
        if(diff>Constant.TOKENTIME)//超过10分钟，token过期
        {
            return true;
        }
        return false;
    }

    public static void sendLocalBroadcast(Context context, Intent localIntent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }





    public static String stringFilterForLetterNdNumber(String str)throws PatternSyntaxException {
        // 只允许字母和汉字
        String   regEx  =  "[^a-zA-Z\u4E00-\u9FA5]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    public static String stringFilter(String str)throws PatternSyntaxException{
        // 只允许字母、数字和汉字
        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }
    public static String stringFilterForText(String str)throws PatternSyntaxException{
        // 只允许汉字
        String   regEx  =  "[^\u4E00-\u9FA5]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }


    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号
     */
//    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
//    public static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[0-35-9]|17[0136-8]|18[0-9]|14[579])[0-9]{8}$";
    public static final String REGEX_MOBILE = "^1(3[0-9]|4[579]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";


    /**
     * 正则表达式：验证URL
     */
//    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    public static final String REGEX_URL = "http://([w-]+.)+[w-]+(/[w - ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 校验用户名
     *
     *  username
     *  校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     *  password
     *  校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     *  mobile
     *  校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     *  email
     *  校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     *  chinese
     *  校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     *  idCard
     *  校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     *  url
     *  校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     *  ipAddr
     *
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }



    public static String getReportDate(String str)
    {
        String[] strArr = str.split(" ");
        if(strArr!=null && strArr.length>0)
            return strArr[0];
        return "";
    }
    public static String getReportTime(String str)
    {
        if(!TextUtils.isEmpty(str))
        {
            String[] strArr = str.split(" ");
            if(strArr!=null && strArr.length>0)
                return strArr[1];

        }
        return "";
    }




    public static void writeObject(String key, Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String objBase64 = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));
            MyApplication.getInstance().setStringPerference(key, objBase64);
        } catch (Exception e) {
            Log.e("test", "saveObject error", e);
        }
    }

    public static Object readObject(String key) {
        try {
//            String objBase64 = getPreference(ctx).getString(key, null);
            String objBase64 = MyApplication.getInstance().getStringPerference(key);
            if (TextUtils.isEmpty(objBase64)) {
                return null;
            }
            byte[] base64 = Base64.decode(objBase64,Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);
            ObjectInputStream bis = new ObjectInputStream(bais);
            return bis.readObject();
        } catch (Exception e) {
            Log.e("test", "readObject error", e);
        }
        return null;
    }


    /**
     * 是否有中文
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
    /**
     * 判断字符串是否包含字母
     *
     * @param str
     * @return
     */
    public static boolean isContainLetters(String str) {
        for (char i = 'A'; i <= 'Z'; i++) {
            if (str.contains(String.valueOf(i))) {
                return true;
            }
        }
        for (char i = 'a'; i <= 'z'; i++) {
            if (str.contains(String.valueOf(i))) {
                return true;
            }
        }
        return false;
    }





    public static String getGroupValue(String oldStr,String newStr)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(oldStr).append("|").append(newStr);
        return sb.toString();
    }



    public static String getMuShuValue(Object value)
    {
        DecimalFormat numberFormat = new DecimalFormat("0.0");
        return numberFormat.format(value);
    }


    public static void SystemMsg(Context  context, String strTitle, String strMsg) {
        // 信息提示
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        if (strTitle != "") {
            builder.setTitle("系统提示");
        }
        builder.setMessage(strMsg);
        builder.setPositiveButton("确认", null);
        builder.create().show();
    }

    public static AlertDialog alterDialog(Context context,View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog dialog = builder.show();
        dialog.setCancelable(false);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.popwinAnim);
        return dialog;
    }

}
