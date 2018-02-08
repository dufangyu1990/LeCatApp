package com.jjhome.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jjhome.demo.YeeApplication;


public class SharePrefUtil {

	private static final String PREF_NAME = "pref.share";
	private static final String SETTING = "setting.pref";
	static Context sContext = YeeApplication.getContext();
	static SharedPreferences sPreferences = sContext.getSharedPreferences(PREF_NAME, 0);
	// 持久化存储，退出登录不会被清除
	static SharedPreferences sSettings = sContext.getSharedPreferences(SETTING, 0);

	public static void clear() {
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.clear();
		editor.apply();
		editor.commit();
	}

	public static void putIntPersist(String key, int value) {
		SharedPreferences.Editor editor = sSettings.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public static int getIntPersist(String key, int value) {
		return sSettings.getInt(key, value);
	}

	public static void putStringPersist(String key, String value) {
		SharedPreferences.Editor editor = sSettings.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static String getStringPersist(String key, String def) {
		return sSettings.getString(key, def);
	}

	public static void putBooleanPersist(String key, boolean value) {
		SharedPreferences.Editor editor = sSettings.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public static boolean getBooleanPersist(String key, boolean def) {
		return sSettings.getBoolean(key, def);
	}

	public static void putInt(String key, int value) {
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public static int getInt(String key, int def) {
		return sPreferences.getInt(key, def);
	}

	public static void putLong(String key, long value) {
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	public static long getLong(String key, long def) {
		return sPreferences.getLong(key, def);
	}

	public static boolean getBoolen(String key, boolean def) {
		return sPreferences.getBoolean(key, def);
	}

	public static void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public static String getString(String key, String def) {
		return sPreferences.getString(key, def);
	}

	public static void putString(String key, String value) {
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

}
