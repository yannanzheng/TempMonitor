package com.ormlitedemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ormlitedemo.activity.HomeActivity;



/**
 * 
 * @author jfy
 *
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver {

	/**
	 * 切花wifi的时候不会得到通知
	 */
	
	public static final String TAG="WiFiBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		//网络状态改变时能接收到广播
		//通知socket尝试连接并接受数据。
		//TemperatureData.getTemperatureData().networkStateChangedCallback();
		HomeActivity.homeActivity.networkStateChangedCallback();
		
		
		//ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		WifiManager wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		Log.i(TAG, "接收到广播了");
//		Log.i(TAG, "连接到的wifi为"+wifiInfo.getSSID());

	
	
	}

}
