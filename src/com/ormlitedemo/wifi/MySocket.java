package com.ormlitedemo.wifi;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import android.util.Log;

import com.ormlitedemo.TemperData;
import com.ormlitedemo.utils.StringUtils;

public class MySocket implements Runnable {

	private static final String TAG = "MySocket";
	private TemperatureChangeListener temperListener;
	private String temperature="";

	public MySocket(TemperatureChangeListener temperListener) {
		this.temperListener = temperListener;
	}


	public MySocket() {
		super();
	}


	@Override
	public void run() {
		try {

			Socket s = new Socket("192.168.2.3", 5000);
			Log.i(TAG, "Socket连接状态为"+s.isConnected());
			
			InputStream in = s.getInputStream();
			byte[] buf = new byte[1024];

			DataInputStream input = new DataInputStream(in);
			//Log.i(TAG, "接收到的数据长度"+buf.length);
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
						
						
						if (temperListener!=null) {
							//temperListener为空
							temperListener.changeTemperature(temperature);
						}
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
	
	public interface TemperatureChangeListener{
    	public void changeTemperature(String tem);
    }



}
