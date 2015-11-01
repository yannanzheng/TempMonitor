package com.ormlitedemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
		
		
		Log.i(TAG, "接收到广播了");

	}

}
