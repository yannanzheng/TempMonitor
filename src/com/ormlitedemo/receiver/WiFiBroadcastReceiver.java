package com.ormlitedemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * ��������R2wifi��ʱ��֪ͨ����socket����
 * @author jfy
 *
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG="WiFiBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		//����״̬�ı�ʱ�ܽ��յ��㲥
		
		
		Log.i(TAG, "���յ��㲥��");

	}

}
