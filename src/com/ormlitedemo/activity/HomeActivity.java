package com.ormlitedemo.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ormlitedemo.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.ormlitedemo.TemperData;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.db.DatabaseHelper;
import com.ormlitedemo.wifi.MyWifiActivity;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText stuNO;
	private EditText stuName;
	private EditText stuAge;
	private EditText stuSex;
	static EditText stuTemper;
	private EditText stuAddress;

	private Student mStudent;
	private StudentDao stuDao;

	private Timer dataTimer;
	private TimerTask dataTimerTask = null;

	// ���Ӳ˵�ѡ��
	private final int MENU_ADD = Menu.FIRST;
	// �鿴�˵�ѡ��
	private final int MENU_VIEWALL = Menu.FIRST + 1;
	// �༭�˵�ѡ��
	private final int MENU_EDIT = Menu.FIRST + 2;
	// �������˵�ѡ��
	private final int MENU_DATA = Menu.FIRST + 3;

	private Bundle mBundle = new Bundle();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);

		initializeViews();

		//��ʱ��
		dataTimer = new Timer("Light");
		dataTimerTask = new TimerTask() {
			@Override
			public void run() {
				updateTemp();
			}
		};

		//��ʱ
		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);

	}

	/**
	 * ��ʼ��UI����
	 */
	private void initializeViews() {
		stuNO = (EditText) findViewById(R.id.stuno);
		stuName = (EditText) findViewById(R.id.name);
		stuAge = (EditText) findViewById(R.id.age);
		stuSex = (EditText) findViewById(R.id.sex);
		stuTemper = (EditText) findViewById(R.id.temper);
		stuAddress = (EditText) findViewById(R.id.address);

		mBundle = getIntent().getExtras();
		if (mBundle != null && mBundle.getString("action").equals("viewone")) {
			// ����һ���û���Ϣ��ʾ�鿴�˵�
			mStudent = (Student) getIntent().getSerializableExtra("entity");
			setStudentUIData(mStudent);
		}

		if (mBundle != null && mBundle.getString("action").equals("edit")) {
			// ����һ���û���Ϣ��ʾ�༭�˵�
			mStudent = (Student) getIntent().getSerializableExtra("entity");
			setStudentUIData(mStudent);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mBundle != null && mBundle.getString("action").equals("viewone"))
			return false;
		else
			return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mBundle != null && mBundle.getString("action").equals("edit")) {
			// �༭״̬�£���ֻ��һ�������桱�˵���
			menu.add(1, MENU_EDIT, 0, "����");
		} else {
			// ����״̬�£��С����ӡ����鿴�������������˵�ѡ��
			menu.add(0, MENU_ADD, 0, "����");
			menu.add(0, MENU_VIEWALL, 0, "�鿴");
			menu.add(0, MENU_DATA, 0, "������");
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD:
			
				stuDao = StudentDao.getStudentDao(getApplicationContext());
				getStudentData();
				
				if (mStudent != null) {
					// ������¼��
					stuDao.addStudent(mStudent);
					
				} else {
					// �༭ʱ��Ϣ����Ϊ��
					Toast.makeText(getApplicationContext(), "�û���Ϣ����Ϊ��",
							Toast.LENGTH_LONG);
				}
			 
			break;
		case MENU_VIEWALL:
			Intent intent = new Intent();
			intent.setClass(HomeActivity.this, StudentListActivity.class);
			startActivity(intent);
			break;
		case MENU_DATA:
			Intent intentData = new Intent();
			intentData.setClass(HomeActivity.this, MyWifiActivity.class);
			startActivity(intentData);
			break;
		case MENU_EDIT:
			
				getStudentData();
				stuDao = StudentDao.getStudentDao(getApplicationContext());
				if (mStudent != null) {
					// ����ĳ��¼��
					stuDao.updateStudent(mStudent);
				}
			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ȡ����ֵ(ʵ����Ϣ)
	 */
	private void getStudentData() {
		mStudent = new Student();
		mStudent.setDeviceID(stuNO.getText().toString());
		mStudent.setName(stuName.getText().toString());
		mStudent.setAge(Integer.parseInt(stuAge.getText().toString()));
		mStudent.setSex(stuSex.getText().toString());
		// mStudent.setScore(Double.parseDouble(stuScore.getText().toString()));
		// ***ʹ�����б��е�����ʵʱ��ʾ
		// mStudent.setScore();
		mStudent.setAddress(stuAddress.getText().toString());
	}

	/**
	 * ��ֵ��UI����
	 * 
	 * @param student
	 */
	private void setStudentUIData(Student student) {
		stuNO.setText(student.getDeviceID());
		stuName.setText(student.getName());
		stuAge.setText(String.valueOf(student.getAge()));
		stuSex.setText(student.getSex());
		stuTemper.setText(student.getTemper());
		// stuScore.setText(MyWifiActivity.strTemp);
		stuAddress.setText(student.getAddress());
		updateTemp();
	}

	/**
	 * ����ui
	 */
	public void updateTemp() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				stuTemper.setText(TemperData.strTemp);
			}
		});
	}

}
