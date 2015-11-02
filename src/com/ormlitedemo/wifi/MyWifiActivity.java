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
 * TODO Ӧ�ø����һ�������ڴ��������ʱ��Ϳ����� �����������µĸı䡣 ���Ͻ�������Ӳ���˵�����
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
		dataTimer = new Timer("Light");// ���ö�ʱ���������ݽ�����ʾ
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

	// ���յ������ݸ�����ʾ������
	public void updateData() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

//				dataview.setText(App.text);
			}
		});
	}


}
