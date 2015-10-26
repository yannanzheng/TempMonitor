package com.ormlitedemo.wifi;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.ormlitedemo.R;

/**
 * TODO 应该改造成一个服务，在打开主界面的时候就开启。 该类驱动体温的改变。 不断接受来自硬件端的数据
 * 
 * @author jfy
 * 
 */
public class MyWifiActivity extends Activity {
	
	/**
	 * 存储体温数据
	 */
	public static StringBuffer strTemp;
	
	private static final String TAG = "MyWifiActivity";
	private Button wifi_start_bt;
	private EditText dataview;
	Socket socket;
	StringBuffer text;

	
	public static StringBuffer strNO;
	byte[] buf;
	int length = 0;

	private Timer dataTimer;
	private TimerTask dataTimerTask = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywifi);
		new Thread(new MySocket()).start();

		dataview = (EditText) findViewById(R.id.dataview);
		wifi_start_bt = (Button) findViewById(R.id.wifi_start_bt);
		dataview.setText(text);
		dataTimer = new Timer("Light");// 设置定时器，对数据进行显示
		dataTimerTask = new TimerTask() {
			@Override
			public void run() {
				updateData();
			}
		};

		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);

	}

	// 接收到的数据更新显示，测试
	public void updateData() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				dataview.setText(text);
			}
		});
	}

	// 创建socket内部类，新建线程实时接收数据
	class MySocket implements Runnable {

		@Override
		public void run() {
			try {

				Socket s = new Socket("192.168.2.3", 5000);
				System.out.print(s.isConnected());

				InputStream in = s.getInputStream();
				buf = new byte[1024];

				/*
				 * length = in.read(buf); for (int i=0;i<length;i++){
				 * System.out.print(buf[i] + " "); }
				 */

				DataInputStream input = new DataInputStream(in);
				while (true) {
					int length = input.read(buf);
					for (int i = 0; i < length; i++) {
						System.out.print(buf[i] + " ");
						if (buf[i] == -1) {
							byte[] byteTemp = new byte[2];
							byteTemp[0] = buf[i + 1];
							byteTemp[1] = buf[i + 2];
							strTemp = new StringBuffer(
									bytesToHexString(byteTemp));
							byte[] byteAddr = new byte[2];
							byteAddr[0] = buf[i + 9];
							byteAddr[1] = buf[i + 10];
							strNO = new StringBuffer(bytesToHexString(byteAddr));
						}
					}

					text = new StringBuffer(bytesToHexString(buf));
					System.out.println("收到客户端消息：" + text);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("错误" + e);
			}
		}
	}

	public static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xff;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();

	}

}
