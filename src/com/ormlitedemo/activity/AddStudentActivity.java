package com.ormlitedemo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ormlitedemo.R;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;
import com.ormlitedemo.utils.TemperatureTableUtil;
import com.ormlitedemo.wifi.TemperatureData;
import com.ormlitedemo.wifi.TemperatureObserver;

public class AddStudentActivity extends Activity implements
		TemperatureObserver, OnClickListener {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		temperatureData.registerObserver(this);
		// 开启线程接收数据

	}

	@Override
	protected void onPause() {
		super.onPause();
		temperatureData.registerObserver(this);

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String strTempData = StringUtils.parseTemperature((String) msg.obj);
				String strTemp = TemperatureTableUtil.queryTemperatureByData(mContext, strTempData);
				add_student_temper_tv.setText(strTemp);
				break;

			default:

				break;
			}
		}

	};

	private Student mStudent;
	private Context mContext = null;

	private static final String TAG = "ADDSTUDENTACTIVITY";
	private static final int TEMPERATURE_CHANGED = 1;
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

	private TemperatureData temperatureData;

	private Thread thread;

	//private Student stu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_student);

		mContext = getApplicationContext();
		mStudent = (Student) getIntent().getSerializableExtra("student");
		Log.i(TAG, "传递过来的学生" + mStudent.toString());
		initializeViews();
		temperatureData = HomeActivity.temperatureData;
		// 注册，开启线程

		thread = new Thread(temperatureData);
		thread.start();

		mStudent = (Student) getIntent().getSerializableExtra("student");

		// 注册号了没有呢？
		// TODO 暂时粗暴的这么做了！
		// new Thread(temperatureData).start();
		Log.i(TAG, "oncreate()" + temperatureData.observers.toString());

	}

	/**
	 * 初始化UI界面
	 */
	private void initializeViews() {
		add_student_cancel_tv = (TextView) findViewById(R.id.add_student_cancel_tv);
		add_student_cancel_tv.setOnClickListener(this);
		add_student_title_tv = (TextView) findViewById(R.id.add_student_title_tv);
		add_student_finished_tv = (TextView) findViewById(R.id.add_student_finished_tv);
		add_student_finished_tv.setOnClickListener(this);
		add_student_device_id_et = (EditText) findViewById(R.id.add_student_device_id_et);
		add_student_device_id_et.setText(mStudent.getDeviceID());
		add_student_stuno_et = (EditText) findViewById(R.id.add_student_stuno_et);
		add_student_stuno_et.setText(mStudent.getStuNo());
		add_student_name_et = (EditText) findViewById(R.id.add_student_name_et);
		add_student_name_et.setText(mStudent.getName());
		add_student_age_et = (EditText) findViewById(R.id.add_student_age_et);
		add_student_age_et.setText(mStudent.getAge());
		add_student_gender_et = (EditText) findViewById(R.id.add_student_gender_et);
		add_student_gender_et.setText(mStudent.getSex());
		add_student_temper_tv = (TextView) findViewById(R.id.add_student_temper_tv);
		add_student_temper_tv.setText(mStudent.getTemper());
		add_student_address_et = (EditText) findViewById(R.id.add_student_address_et);
		add_student_phone_num_et = (EditText) findViewById(R.id.add_student_phone_num_et);
		add_student_phone_num_et.setText(mStudent.getPhoneNum());
		if (mStudent != null) {

			if (mStudent.getDeviceID() != null) {

				add_student_device_id_et.setText(mStudent.getDeviceID());// 空指针异常
			}
			if (mStudent.getName() != null) {

				add_student_name_et.setText(mStudent.getName());// 空指针异常
			}
			if (mStudent.getAge() != null) {
				
				add_student_age_et.setText(mStudent.getAge());// 空指针异常
			}
			if (mStudent.getSex() != null) {
				
				add_student_gender_et.setText(mStudent.getSex() );// 空指针异常
			}
			if (mStudent.getTemper()!= null) {
				
				add_student_temper_tv.setText(mStudent.getTemper());// 空指针异常
			}
			
			if (mStudent.getAddress()!= null) {
				
				add_student_address_et.setText(mStudent.getAddress());// 空指针异常
			}
			if (mStudent.getPhoneNum()!= null) {
				
				add_student_phone_num_et.setText(mStudent.getPhoneNum());// 空指针异常
			}
			
		}

	}

	@Override
	public void updateTemperature(String tem) {
		// TODO 实时更新温度显示值
		Log.i(TAG, "温度为：" + tem);

		Message msg = Message.obtain();
		msg.what = TEMPERATURE_CHANGED;
		msg.obj = tem;
		mHandler.sendMessage(msg);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_student_cancel_tv:
			finish();
			break;
		case R.id.add_student_finished_tv:

			saveStudent();

			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 保存学生信息
	 */
	private void saveStudent() {
		mStudent.setDeviceID(add_student_device_id_et.getText().toString());
		String name = add_student_name_et.getText().toString();
		if (!StringUtils.isEmptyOrNull(name)) {

			mStudent.setName(name);
		}
		String age = add_student_age_et.getText().toString();
		if (!StringUtils.isEmptyOrNull(age)) {

			mStudent.setAge(age);
		}

		String stuNo = add_student_stuno_et.getText().toString();
		if (!StringUtils.isEmptyOrNull(stuNo)) {

			mStudent.setStuNo(stuNo);
		}

		String gender = add_student_gender_et.getText().toString();
		if (!StringUtils.isEmptyOrNull(gender)) {

			mStudent.setSex(gender);
		}
		mStudent.setTemper(add_student_temper_tv.getText().toString());
		String address = add_student_address_et.getText().toString();
		if (!StringUtils.isEmptyOrNull(address)) {

			mStudent.setAddress(address);
		}
		String photoNum = add_student_phone_num_et.getText().toString();
		if (!StringUtils.isEmptyOrNull(photoNum)) {

			mStudent.setPhoneNum(photoNum);
		}

		if (!mStudent.getDeviceID().isEmpty()) {

			boolean isStudentExist = StudentDao.isExistDevice(mStudent
					.getDeviceID());

			if (isStudentExist) {
				// 存在该学生
				StudentDao.getStudentDao(mContext).updateStudent(mStudent);
			} else {

				StudentDao.getStudentDao(mContext).addStudent(mStudent);
			}
		}

	}

}
