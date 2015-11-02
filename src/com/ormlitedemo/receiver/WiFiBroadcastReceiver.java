package com.ormlitedemo.receiver;

import com.ormlitedemo.wifi.TemperatureData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 当连接上R2wifi的时候通知开启socket连接
 * @author jfy
 *
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG="WiFiBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		//网络状态改变时能接收到广播
		//通知socket尝试连接并接受数据。
		TemperatureData.getTemperatureData().networkStateChangedCallback();
		
		
		
		//ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		WifiManager wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		Log.i(TAG, "接收到广播了");
//		Log.i(TAG, "连接到的wifi为"+wifiInfo.getSSID());

	
	
	}

}
