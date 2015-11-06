package com.ormlitedemo.wifi;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import android.util.Log;

import com.ormlitedemo.utils.StringUtils;

/**
 * 采用观察者模式对体温变化进行监控
 * @author jfy
 *
 */
public class TemperatureData implements 

Runnable,TemperatureSubject,NetworkStateChangedCallback {

	private static final String TAG = "TemperatureData";
	//private TemperatureObserver temperListener;
	private String temperature="";
	//TODO 重新考虑是否使用这个集合，毕竟线程并不安全
	public Vector<TemperatureObserver> observers;
	private static TemperatureData temperatureData=null;
	private Socket socket;


	private TemperatureData() {
		observers=new Vector<TemperatureObserver>();
		
	}
	
	public static TemperatureData getTemperatureData(){
		if(temperatureData==null){
			return new TemperatureData();
			
		}
		
		return temperatureData;
	}


	@Override
	public void run() {
		
		Log.i(TAG, "run方法运行一次");
		
		try {
			socket = new Socket("192.168.2.3", 5000);
			
			boolean connectState=socket.isConnected();
			
			Log.i(TAG, "Socket连接状态为"+connectState);
			
			if (connectState) {

				//连接上了该硬件的情况下，
				InputStream in = socket.getInputStream();
				byte[] byteSrc = new byte[1024];
				//int length = -1;
				while (true) {
					int length = in.read(byteSrc);
					//System.out.println("读取到的数据的长度--->"+length);
					Log.i(TAG, "原始字节数据" + byteSrc.toString());
					String strSrc=StringUtils.bytesToHexString

(byteSrc, length);
					
					
					//字符串
					for (int i = 0; i < length; i++) {
						
						if (byteSrc[i]==-1) {
							Log.i(TAG, 

"***********************开始一条**************************");
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
							String 

strData=StringUtils.bytesToHexString(data, data.length);
							
							notifyObservers(strData);
							
							Log.i(TAG, 

"***********************结束一条**************************");
						}
						
						
					}
					
				}
				
				
				
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public synchronized void registerObserver(TemperatureObserver tempObj) {
		observers.add(tempObj);
		
	}


	@Override
	public synchronized void removeObserver(TemperatureObserver tempObj) {

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
		Log.i(TAG, "回调，网络状态发生了改变");
		this.run();
		
	}

}
