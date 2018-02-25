package com.example.dufangyu.lecatapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

import java.util.List;

public class WifiAdmin {

	private static final String TAG = "[WifiAdmin]";
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiList = null;
	private List<WifiConfiguration> mWifiConfiguration;
	private WifiLock mWifiLock;
	private DhcpInfo dhcpInfo;

	public WifiAdmin(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public boolean openWifi() {// 打开wifi
		if (!mWifiManager.isWifiEnabled()) {
			Log.i(TAG, "setWifiEnabled.....");
			mWifiManager.setWifiEnabled(true);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i(TAG, "setWifiEnabled.....end");
		}
		return mWifiManager.isWifiEnabled();
	}

	/**
	 * 判断wifi是否可用
	 * 
	 * @param inContext
	 * @return
	 */
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	public int checkState() {
		return mWifiManager.getWifiState();
	}

	public void acquireWifiLock() {// 锁定wifiLock
		mWifiLock.acquire();
	}

	public void releaseWifiLock() {// 解锁wifiLock
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	public void connectConfiguration(int index) {// 指定配置好的网络进行连接
		if (index > mWifiConfiguration.size()) {
			return;
		}
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}

	public void startScan() {// wifi扫描
		boolean scan = mWifiManager.startScan();
		Log.i(TAG, "startScan result:" + scan);
		mWifiList = mWifiManager.getScanResults();
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();

		if (mWifiList != null) {
			Log.i(TAG, "startScan result:" + mWifiList.size());
			for (int i = 0; i < mWifiList.size(); i++) {
				ScanResult result = mWifiList.get(i);
				Log.i(TAG, "startScan result[" + i + "]" + result.SSID + "," + result.BSSID);
			}
			Log.i(TAG, "startScan result end.");
		} else {
			Log.i(TAG, "startScan result is null.");
		}

	}

	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	public StringBuilder lookUpScan() {// 查看扫描结果
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public DhcpInfo getDhcpInfo() {
		return dhcpInfo = mWifiManager.getDhcpInfo();
	}

	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	public WifiInfo getWifiInfo() {
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo;
	}

	public void addNetwork(WifiConfiguration wcg) { // 添加一个网络配置并连接
		int wcgID = mWifiManager.addNetwork(wcg);
		 mWifiManager.enableNetwork(wcgID, true);
	}

	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	// 然后是一个实际应用方法，只验证过没有密码的情况：

	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	public WifiConfiguration IsExsits(String SSID) { // 查看以前是否已经配置过该SSID
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}
	public int wifiEncrye(String capabilities) {
		if (capabilities.contains("WPA2")) {
			return 3;
		}
		if (capabilities.contains("[WPA-PSK-TKIP+CCMP]") || capabilities.contains("[WPA2-PSK-TKIP+CCMP]")) {
			return 3;
		}
		if (capabilities.contains("[WEP]") && capabilities.contains("[IBSS]")) {
			return 2;
		}
		if (capabilities.contains("[WEP]")) {
			return 2;
		}
		if (capabilities.contains("[WPA-PSK-CCMP]") || capabilities.contains("[WPA-PSK-TKIP+CCMP]")) {
			return 3;
		}
		if (capabilities.contains("[WPA2-PSK-CCMP]") || capabilities.contains("[WPA2-PSK-TKIP+CCMP]")) {
			return 3;
		}
		if (capabilities.contains("[ESS]")) {
			return 1;
		}
		return 1;
	}
}
