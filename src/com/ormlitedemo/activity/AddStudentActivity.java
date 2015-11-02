package com.ormlitedemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.ormlitedemo.wifi.TemperatureData;
import com.ormlitedemo.wifi.TemperatureObserver;

public class AddStudentActivity extends Activity implements TemperatureObserver,OnClickListener{

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		temperatureData.registerObserver(this);
		//�����߳̽�������
       
        
      
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		temperatureData.registerObserver(this);
		
	}

	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String strTem=StringUtils.parseTemperature((String) msg.obj);
				add_student_temper_tv.setText(strTem);
				break;

			default:
				
				break;
			}
		}
		
	};
	
	private Student mStudent;
	private Context mContext=null;
	
	private static final String TAG="ADDSTUDENTACTIVITY";
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

	private Student stu;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_student);

		mContext=getApplicationContext();
		stu = (Student) getIntent().getSerializableExtra("student");
		Log.i(TAG, "���ݹ�����ѧ��"+stu.toString());
		initializeViews();
		temperatureData = HomeActivity.temperatureData;
		//ע�ᣬ�����߳�
		
		thread = new Thread(temperatureData);
		thread.start();
		
		mStudent=(Student) getIntent().getSerializableExtra("student");
	    
	        //ע�����û���أ�
	        //TODO ��ʱ�ֱ�����ô���ˣ�
	       // new Thread(temperatureData).start();
	        Log.i(TAG, "oncreate()"+temperatureData.observers.toString());
	        
	        

	}

	/**
	 * ��ʼ��UI����
	 */
	private void initializeViews() {
		add_student_cancel_tv = (TextView) findViewById(R.id.add_student_cancel_tv);
		add_student_cancel_tv.setOnClickListener(this);
		add_student_title_tv = (TextView) findViewById(R.id.add_student_title_tv);
		add_student_finished_tv = (TextView) findViewById(R.id.add_student_finished_tv);
		add_student_finished_tv.setOnClickListener(this);
		add_student_device_id_et = (EditText) findViewById(R.id.add_student_device_id_et);
		add_student_device_id_et.setText(stu.getDeviceID());
		if (mStudent!=null) {
			
			if (mStudent.getDeviceID()!=null) {
				
				add_student_device_id_et.setText(mStudent.getDeviceID());//��ָ���쳣
			}
		}
		
		
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
		//TODO ʵʱ�����¶���ʾֵ
		Log.i(TAG, "�¶�Ϊ��"+tem);
		
		Message msg=Message.obtain();
		msg.what=TEMPERATURE_CHANGED;
		msg.obj=tem;
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

	//����ѧ����Ϣ
	private void saveStudent() {
		mStudent=new Student();
		mStudent.setDeviceID(add_student_device_id_et.getText().toString());
		mStudent.setName(add_student_name_et.getText().toString());
		mStudent.setAge(add_student_age_et.getText().toString());
		mStudent.setStuNo(add_student_stuno_et.getText().toString());
		mStudent.setSex(add_student_gender_et.getText().toString());
		mStudent.setTemper(add_student_temper_tv.getText().toString());
		mStudent.setAddress(add_student_address_et.getText().toString());
		mStudent.setPhoneNum(add_student_phone_num_et.getText().toString());
		
		if (!mStudent.getDeviceID().isEmpty()) {
			
			boolean isStudentExist=StudentDao.isExistDevice(mStudent.getDeviceID());
			
			if (isStudentExist) {
				//���ڸ�ѧ��
				StudentDao.getStudentDao(mContext).updateStudent(mStudent);
			}else{
				
				StudentDao.getStudentDao(mContext).addStudent(mStudent);
			}
		}
		
	}

	

}
