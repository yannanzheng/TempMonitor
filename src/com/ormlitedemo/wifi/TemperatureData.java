package com.ormlitedemo.wifi;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;

/**
 * 采用观察者模式对体温变化进行监控
 * @author jfy
 *
 */
public class TemperatureData implements Runnable,TemperatureSubject {

	//TODO 应该监控wifi连接，一旦连接上wifi，通知这边开启，没有的话就提示网络连接之类的
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

			Socket s = new Socket("192.168.2.3", 5000);
			Log.i(TAG, "Socket连接状态为"+s.isConnected());
			
			
			InputStream in = s.getInputStream();
			byte[] byteSrc = new byte[1024];
			//int length = -1;
			while (true) {
				int length = in.read(byteSrc);
				//System.out.println("读取到的数据的长度--->"+length);
				Log.i(TAG, "原始字节数据" + byteSrc.toString());
				String strSrc=StringUtils.bytesToHexString(byteSrc, length);
//				Log.i(TAG, "原始数据转为16进制字符串" + strSrc);
				
				
//				if (strSrc.contains("ff")) {
//					//对字符串进行切割
//					String strData=strSrc.substring(strSrc.indexOf("ff")+2);
//					Log.i(TAG, "切割一次的字符串"+strData);
//					String strTemp=strData.substring(0, 4);
//					Log.i(TAG, "温度"+strTemp);
//					String strDeivceId=strData.substring(4);
//					Log.i(TAG, "设备id"+strDeivceId);
//					
//					
//				}
				
				
				//字符串
				for (int i = 0; i < length; i++) {
					
					if (byteSrc[i]==-1) {
						Log.i(TAG, "***********************开始一条**************************");
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
						
//						//解析出该设备的温度数据和设备id
//						byte[] byteTemp=new byte[2];
//						byteTemp[0]=byteSrc[i+1];
//						byteTemp[1]=byteSrc[i+2];
//						//转换成字符串
//						String strTemper=StringUtils.bytesToHexString(byteTemp, byteTemp.length);
//						Log.i(TAG, "温度数据:strTemper=" +strTemper);
//						//截取体温数据后面的几个字节作为设备的id
//						byte[] byteDeviceNo=new byte[8];
//						byteDeviceNo[0]=byteSrc[i+3];
//						byteDeviceNo[1]=byteSrc[i+4];
//						byteDeviceNo[2]=byteSrc[i+5];
//						byteDeviceNo[3]=byteSrc[i+6];
//						byteDeviceNo[4]=byteSrc[i+7];
//						byteDeviceNo[5]=byteSrc[i+8];
//						byteDeviceNo[6]=byteSrc[i+9];
//						byteDeviceNo[7]=byteSrc[i+10];
//						String strDeviceNo=StringUtils.bytesToHexString(byteDeviceNo, byteDeviceNo.length);
//						
//						//通知温度改变
//						//notifyObservers(strTemper);
//						Log.i(TAG, "设备strDeviceNo=" + strDeviceNo);
						Log.i(TAG, "***********************结束一条**************************");
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

}
