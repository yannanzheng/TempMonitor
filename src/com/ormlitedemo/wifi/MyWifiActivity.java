package com.ormlitedemo.wifi;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.ormlitedemo.R;
import com.ormlitedemo.App;

/**
 * TODO 应该改造成一个服务，在打开主界面的时候就开启。 该类驱动体温的改变。 不断接受来自硬件端的数据
 * 
 * @author jfy
 * 
 */
public class MyWifiActivity extends Activity {
	
	
	private static final String TAG = "MyWifiActivity";
	private Button wifi_start_bt;
	private EditText dataview;
	Socket socket;
	
	byte[] buf;
	int length = 0;

	private Timer dataTimer;
	private TimerTask dataTimerTask = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywifi);
		new Thread(TemperatureData.getTemperatureData()).start();

		initView();
		dataTimer = new Timer("Light");// 设置定时器，对数据进行显示
		dataTimerTask = new TimerTask() {
			@Override
			public void run() {
				updateData();
			}
		};

		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);

	}

	private void initView() {
		dataview = (EditText) findViewById(R.id.dataview);
		wifi_start_bt = (Button) findViewById(R.id.wifi_start_bt);
//		dataview.setText(App.text);
	}

	// 接收到的数据更新显示，测试
	public void updateData() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

//				dataview.setText(App.text);
			}
		});
	}


}
