package com.ormlitedemo.wifi;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import android.util.Log;

import com.ormlitedemo.TemperData;
import com.ormlitedemo.utils.StringUtils;

public class MySocket implements Runnable {

	private static final String TAG = "MySocket";


	@Override
	public void run() {
		try {

			Socket s = new Socket("192.168.2.3", 5000);
			Log.i(TAG, "Socket连接状态为"+s.isConnected());
			
			InputStream in = s.getInputStream();
			byte[] buf = new byte[1024];

			DataInputStream input = new DataInputStream(in);
			
			while (true) {
				int length = input.read(buf);
				for (int i = 0; i < length; i++) {
					if (buf[i] == -1) {
						byte[] byteTemp = new byte[2];
						
						byteTemp[0] = buf[i + 1];
						byteTemp[1] = buf[i + 2];
						TemperData.strTemp = new StringBuffer(
								StringUtils.bytesToHexString(byteTemp));
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
			System.out.println("错误" + e);
		}
	}


}
