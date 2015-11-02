package com.ormlitedemo.wifi;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;

/**
 * ���ù۲���ģʽ�����±仯���м��
 * @author jfy
 *
 */
public class TemperatureData implements Runnable,TemperatureSubject,NetworkStateChangedCallback {

	//TODO Ӧ�ü��wifi���ӣ�һ��������wifi��֪ͨ��߿�����û�еĻ�����ʾ��������֮���
	private static final String TAG = "TemperatureData";
	//private TemperatureObserver temperListener;
	private String temperature="";
	public ArrayList<TemperatureObserver> observers;
	private static TemperatureData temperatureData=null;


	private TemperatureData() {
		observers=new ArrayList<TemperatureObserver>();
		
	}
	
	public static TemperatureData getTemperatureData(){
		if(temperatureData==null){
			return new TemperatureData();
			
		}
		
		return temperatureData;
	}


	@Override
	public void run() {
		try {

//			WifiManager wifiManager=(WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//			//Log.i(TAG, "���յ��㲥��");
//			Log.i(TAG, "���ӵ���wifiΪ"+wifiInfo.getSSID());
			
			Socket s = new Socket("192.168.2.3", 5000);
			boolean connectState=s.isConnected();
			
			Log.i(TAG, "Socket����״̬Ϊ"+connectState);
			
			
			InputStream in = s.getInputStream();
			byte[] byteSrc = new byte[1024];
			//int length = -1;
			while (true) {
				int length = in.read(byteSrc);
				//System.out.println("��ȡ�������ݵĳ���--->"+length);
				Log.i(TAG, "ԭʼ�ֽ�����" + byteSrc.toString());
				String strSrc=StringUtils.bytesToHexString(byteSrc, length);
				
				
				//�ַ���
				for (int i = 0; i < length; i++) {
					
					if (byteSrc[i]==-1) {
						Log.i(TAG, "***********************��ʼһ��**************************");
						byte[] data=new byte[10];
						data[0]=byteSrc[i+1];
						data[1]=byteSrc[i+2];
						data[2]=byteSrc[i+3];
						data[3]=byteSrc[i+4];
						data[4]=byteSrc[i+5];
						data[5]=byteSrc[i+6];
						data[6]=byteSrc[i+7];
						data[7]=byteSrc[i+8];
						data[8]=byteSrc[i+9];
						data[9]=byteSrc[i+10];
						String strData=StringUtils.bytesToHexString(data, data.length);
						
						notifyObservers(strData);
						
						Log.i(TAG, "***********************����һ��**************************");
					}
					
					
				}
				
				
			
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void registerObserver(TemperatureObserver tempObj) {
		observers.add(tempObj);
		
	}


	@Override
	public void removeObserver(TemperatureObserver tempObj) {

		int i=observers.indexOf(tempObj);
		if (i>=0) {
			observers.remove(i);
		}
	}


	
	@Override
	public void notifyObservers(String temp) {
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).updateTemperature(temp);
		}
		
	}

	@Override
	public void networkStateChangedCallback() {
		Log.i(TAG, "�ص�������״̬�����˸ı�");
		
	}

}
