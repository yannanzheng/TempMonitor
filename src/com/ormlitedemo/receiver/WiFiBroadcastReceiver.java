package com.ormlitedemo.receiver;

import com.ormlitedemo.activity.HomeActivity;
import com.ormlitedemo.wifi.TemperatureData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;



/**
 * 
 * @author jfy
 *
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver {

	/**
	 * �л�wifi��ʱ�򲻻�õ�֪ͨ
	 */
	
	public static final String TAG="WiFiBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		//����״̬�ı�ʱ�ܽ��յ��㲥
		//֪ͨsocket�������Ӳ��������ݡ�
		//TemperatureData.getTemperatureData().networkStateChangedCallback();
		HomeActivity.homeActivity.networkStateChangedCallback();
		
		
		//ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		WifiManager wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		Log.i(TAG, "���յ��㲥��");
//		Log.i(TAG, "���ӵ���wifiΪ"+wifiInfo.getSSID());

	
	
	}

}
