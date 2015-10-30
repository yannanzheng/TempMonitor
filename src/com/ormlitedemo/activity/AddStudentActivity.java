package com.ormlitedemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ormlitedemo.R;
import com.ormlitedemo.wifi.TemperatureData;
import com.ormlitedemo.wifi.TemperatureObserver;

public class AddStudentActivity extends Activity implements TemperatureObserver{

	private static final String TAG="ADDSTUDENTACTIVITY";
	private TextView add_student_cancel_tv;
	private TextView add_student_title_tv;
	private TextView add_student_finished_tv;
	private EditText add_student_device_id_et;
	private EditText add_student_stuno_et;
	private EditText add_student_name_et;
	private EditText add_student_age_et;
	private EditText add_student_gender_et;
	private TextView add_student_temper_tv;
	private EditText add_student_address_et;
	private EditText add_student_phone_num_et;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_student);

		initializeViews();
		 TemperatureData temperatureData=TemperatureData.getTemperatureData();
	        //开启线程接收数据
	        temperatureData.registerObserver(this);
	        //new Thread(temperatureData).start();
	        Log.i(TAG, "oncreate()");
	        
	        

	}

	/**
	 * 初始化UI界面
	 */
	private void initializeViews() {
		add_student_cancel_tv = (TextView) findViewById(R.id.add_student_cancel_tv);
		add_student_title_tv = (TextView) findViewById(R.id.add_student_title_tv);
		add_student_finished_tv = (TextView) findViewById(R.id.add_student_finished_tv);
		add_student_device_id_et = (EditText) findViewById(R.id.add_student_device_id_et);
		add_student_stuno_et = (EditText) findViewById(R.id.add_student_stuno_et);
		add_student_name_et = (EditText) findViewById(R.id.add_student_name_et);
		add_student_age_et = (EditText) findViewById(R.id.add_student_age_et);
		add_student_gender_et = (EditText) findViewById(R.id.add_student_gender_et);
		add_student_temper_tv = (TextView) findViewById(R.id.add_student_temper_tv);
		add_student_address_et = (EditText) findViewById(R.id.add_student_address_et);
		add_student_phone_num_et = (EditText) findViewById(R.id.add_student_phone_num_et);
		

		

	}



	
	@Override
	public void updateTemperature(String tem) {
		//TODO 实时更新温度显示值
		Log.i(TAG, "温度为："+tem);
		
		add_student_temper_tv.setText(tem);
		
		
	}

	

}
