package com.ormlitedemo.wifi;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;

import com.ormlitedemo.TemperData;
import com.ormlitedemo.utils.StringUtils;

/**
 * 采用观察者模式对体温变化进行监控
 * @author jfy
 *
 */
public class TemperatureData implements Runnable,TemperatureSubject {

	//TODO 应该监控wifi连接，一旦连接上wifi，通知这边开启，没有的话就提示网络连接之类的
	private static final String TAG = "MySocket";
	//private TemperatureObserver temperListener;
	private String temperature="";
	private ArrayList<TemperatureObserver> observers;
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

			Socket s = new Socket("192.168.2.3", 5000);
			Log.i(TAG, "Socket连接状态为"+s.isConnected());
			
			InputStream in = s.getInputStream();
			byte[] buf = new byte[1024];

			DataInputStream input = new DataInputStream(in);
			while (true) {
				//Log.i(TAG, "MySocket在运行....****");
				int length = input.read(buf);
				//Log.i(TAG, "接收到的数据长度"+length);
				for (int i = 0; i < length; i++) {
					if (buf[i] == -1) {
						
						byte[] byteTemp = new byte[2];//体温字节码数据
						
						
						byteTemp[0] = buf[i + 1];
						byteTemp[1] = buf[i + 2];
						
						temperature=StringUtils.bytesToHexString(byteTemp);//十六进制字符串
						
						
						//通知
						notifyObservers(temperature);
						
						
						//调用方法将数据传递到HomeActivity中
						
						
						Log.i(TAG, "当前温度"+temperature);
						byte[] byteAddr = new byte[2];
						byteAddr[0] = buf[i + 9];
						byteAddr[1] = buf[i + 10];
						TemperData.strNO = new StringBuffer(StringUtils.bytesToHexString(byteAddr));
					}
				}

				TemperData.text = new StringBuffer(StringUtils.bytesToHexString(buf));
				System.out.println("收到客户端消息：" + TemperData.text);
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

}
