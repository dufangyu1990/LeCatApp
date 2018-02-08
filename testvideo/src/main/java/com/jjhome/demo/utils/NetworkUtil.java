package com.jjhome.demo.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.telephony.TelephonyManager;

/**
 * �?查网络类
 * 
 * @author 907170
 * 
 */
public class NetworkUtil {

	/**NetworkUtil
	 * 未知网络
	 */
	public static final int NETWORKTYPE_INVALID = 0;

	/**
	 * wifi网络
	 */
	public static final int NETWORKTYPE_WIFI = 1;

	/**
	 * 2G网络
	 */
	public static final int NETWORKTYPE_2G = 2;

	/**
	 * 3G网络
	 */
	public static final int NETWORKTYPE_3G = 3;

	/**
	 * 4G网络
	 */
	public static final int NETWORKTYPE_4G = 4;

	
	/**
	 * 给后台上报的数据
	 * 无网�?
	 */
	public static final String TYPE_NONE = "0";
	
	/**
	 * WIFI网络
	 */
	public static final String TYPE_WIFI = "1";
	
	/**
	 * 移动4G网络
	 */
	public static final String TYPE_CMCC_4G = "2";
	
	/**
	 * 移动2G�?3G网络
	 */
	public static final String TYPE_CMCC_2G = "3";
	
	/**
	 * 其他运营商网络（电信、联通）
	 */
	public static final String TYPE_OTHER = "4";
	

	/**
	 * true表示连接�?,false表示未连�?,网络分为两中状�??:mobile,wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkState(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile state
		State mobileNetState = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		// WIFI state
		State wifiNetState = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();

		if (mobileNetState == State.CONNECTED
				|| mobileNetState == State.CONNECTING) {
			return true;
		}
		if (wifiNetState == State.CONNECTED || wifiNetState == State.CONNECTING) {
			return true;
		}
		return false;
	}

	/**
	 * 不管网络是何种状态，只要能连上互联网就返回true
	 */
	public static boolean isConnectionAvailable(Context ctx) {
		boolean isConnectionFail = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isConnected()) {
				return isConnectionFail;
			}
			isConnectionFail = true;
		}

		return isConnectionFail;
	}

	/**
	 * 获取网络类型 wifi,mobile
	 */
	public static String getCurrentNetWorkName(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();

		if (cwjManager.getActiveNetworkInfo() != null) {
			return cwjManager.getActiveNetworkInfo().getTypeName();
		}
		return null;
	}

	/**
	 * 判断手机网络类型
	 * 
	 * @param context
	 * @return
	 */
	private static int getMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return NETWORKTYPE_2G;

		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return NETWORKTYPE_3G;

		case TelephonyManager.NETWORK_TYPE_LTE:
			return NETWORKTYPE_4G;

		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return NETWORKTYPE_INVALID;

		default:
			return NETWORKTYPE_INVALID;
		}
	}

	/**
	 * 获取网络类型 wifi,2G,3G
	 */
	public static int getCurrentNetWorkType(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cwjManager.getActiveNetworkInfo() != null) {
			if (cwjManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
				return NETWORKTYPE_WIFI;
			} else if (cwjManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
				return getMobileNetwork(context);
			}
		}
		return NETWORKTYPE_INVALID;
	}
	
	/**
	 * 判断手机网络运营�?
	 * 
	 * @param context
	 * @return
	 */
	private static String getMobileNetType(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return TYPE_CMCC_2G;
			
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_IDEN:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return TYPE_OTHER;

		case TelephonyManager.NETWORK_TYPE_LTE:
			return TYPE_CMCC_4G;

		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return TYPE_NONE;

		default:
			return TYPE_OTHER;
		}
	}

	/**
	 * A、wifi：�?�过WLAN方式接入的网�? B、移�?4G：移动的4G网络 C、移�?2/3G：移动的2G�?3G网络
	 * D、其他网络：剔除掉上诉三类网络的其他网络，包含电信�?�联通网络等
	 * 
	 * //用户网络制式 "0"=>array('typeId'=>0,'typeName'=>'�?'),
	 * "1"=>array('typeId'=>1,'typeName'=>'wifi'),
	 * "2"=>array('typeId'=>2,'typeName'=>'移动4G'),
	 * "3"=>array('typeId'=>3,'typeName'=>'移动2/3G'),
	 * "4"=>array('typeId'=>4,'typeName'=>'其他网络'), ),
	 * 
	 * 获取手机当前网络类型
	 * @param context
	 * @return
	 */
	public static String getMobileNetworkType(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cwjManager.getActiveNetworkInfo() != null) {
			if (cwjManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
				return TYPE_WIFI;
			} else if (cwjManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
				return getMobileNetType(context);
			}
		}
		return TYPE_NONE;
	}

	static final Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	public static String getCurrentApnInUse(Context context) {
		Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI,
				new String[] { "_id", "apn", "type" }, null, null, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast()) {
			return null;
		}
		// long id = cursor.getLong(0);
		String apn = cursor.getString(1);
		// String type = cursor.getString(2);

		return apn;
	}

	public static boolean isChinaMobile(Context ctx) {
		TelephonyManager manager = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = manager.getSimOperator();
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46001")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * �?测wifi是否连接
	 * 
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取当前的网络类型(WIFI,2G,3G,4G)
	 * <p>依赖上面的方法</p>
	 *
	 * @param cm ConnectivityManager
	 * @return 网络类型名称
	 * <ul>
	 * <li>NETWORK_WIFI   </li>
	 * <li>NETWORK_4G     </li>
	 * <li>NETWORK_3G     </li>
	 * <li>NETWORK_2G     </li>
	 * <li>NETWORK_UNKNOWN</li>
	 * <li>NETWORK_NO     </li>
	 * </ul>
	 */
	public static int getNetWorkTypeName(ConnectivityManager cm) {
		switch (getNetWorkType(cm)) {
			case NETWORK_WIFI:
				return NETWORK_WIFI;
			case NETWORK_4G:
				return NETWORK_4G;
			case NETWORK_3G:
				return NETWORK_3G;
			case NETWORK_2G:
				return NETWORK_2G;
			case NETWORK_NO:
				return NETWORK_NO;
			default:
				return NETWORK_UNKNOWN;
		}
	}

	/**
	 * 获取当前的网络类型(WIFI,2G,3G,4G)
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
	 *
	 * @param cm ConnectivityManager
	 * @return 网络类型
	 * <ul>
	 * <li>{@link #NETWORK_WIFI   } = 1;</li>
	 * <li>{@link #NETWORK_4G     } = 4;</li>
	 * <li>{@link #NETWORK_3G     } = 3;</li>
	 * <li>{@link #NETWORK_2G     } = 2;</li>
	 * <li>{@link #NETWORK_UNKNOWN} = 5;</li>
	 * <li>{@link #NETWORK_NO     } = -1;</li>
	 * </ul>
	 */
	private static int getNetWorkType(ConnectivityManager cm) {
		int netType = NETWORK_NO;
		NetworkInfo info = getActiveNetworkInfo(cm);
		if (info != null && info.isAvailable()) {

			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				netType = NETWORK_WIFI;
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (info.getSubtype()) {

					case NETWORK_TYPE_GSM:
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						netType = NETWORK_2G;
						break;

					case NETWORK_TYPE_TD_SCDMA:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						netType = NETWORK_3G;
						break;

					case NETWORK_TYPE_IWLAN:
					case TelephonyManager.NETWORK_TYPE_LTE:
						netType = NETWORK_4G;
						break;
					default:

						String subtypeName = info.getSubtypeName();
						if (subtypeName.equalsIgnoreCase("TD-SCDMA")
								|| subtypeName.equalsIgnoreCase("WCDMA")
								|| subtypeName.equalsIgnoreCase("CDMA2000")) {
							netType = NETWORK_3G;
						} else {
							netType = NETWORK_UNKNOWN;
						}
						break;
				}
			} else {
				netType = NETWORK_UNKNOWN;
			}
		}
		return netType;
	}
	/**
	 * 获取活动网络信息
	 *
	 * @param cm ConnectivityManager
	 * @return NetworkInfo
	 */
	private static NetworkInfo getActiveNetworkInfo(ConnectivityManager cm) {
		return cm.getActiveNetworkInfo();
	}

	/**
	 * 判断网络是否连接
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
	 *
	 * @param cm ConnectivityManager
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isConnected(ConnectivityManager cm) {
		NetworkInfo info = getActiveNetworkInfo(cm);
		return info != null && info.isConnected();
	}

	public static final int NETWORK_WIFI = 1;    // wifi network
	public static final int NETWORK_4G = 4;    // "4G" networks
	public static final int NETWORK_3G = 3;    // "3G" networks
	public static final int NETWORK_2G = 2;    // "2G" networks
	public static final int NETWORK_UNKNOWN = 5;    // unknown network
	public static final int NETWORK_NO = -1;   // no network
	private static final int NETWORK_TYPE_GSM = 16;
	private static final int NETWORK_TYPE_TD_SCDMA = 17;
	private static final int NETWORK_TYPE_IWLAN = 18;

}
