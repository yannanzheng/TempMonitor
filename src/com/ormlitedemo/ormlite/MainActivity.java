package com.ormlitedemo.ormlite;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import com.ormlitedemo.bean.Student;
import com.ormlitedemo.db.DatabaseHelper;
import com.ormlitedemo.wifi.MyWifiActivity;
import com.example.ormlitedemo.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText stuNO;
	private EditText stuName;
	private EditText stuAge;
	private EditText stuSex;
	static EditText stuScore;
	private EditText stuAddress;

	private Student mStudent;
	private Dao<Student, String> stuDao;

	private Timer dataTimer;
	private TimerTask dataTimerTask = null;

	// 增加菜单选项
	private final int MENU_ADD = Menu.FIRST;
	// 查看菜单选项
	private final int MENU_VIEWALL = Menu.FIRST + 1;
	// 编辑菜单选项
	private final int MENU_EDIT = Menu.FIRST + 2;
	// 数据流菜单选项
	private final int MENU_DATA = Menu.FIRST + 3;

	private Bundle mBundle = new Bundle();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initializeViews();

		dataTimer = new Timer("Light");
		dataTimerTask = new TimerTask() {
			@Override
			public void run() {
				updateTemp();
			}
		};

		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);

	}

	/**
	 * 初始化UI界面
	 */
	private void initializeViews() {
		stuNO = (EditText) findViewById(R.id.stuno);
		stuName = (EditText) findViewById(R.id.name);
		stuAge = (EditText) findViewById(R.id.age);
		stuSex = (EditText) findViewById(R.id.sex);
		stuScore = (EditText) findViewById(R.id.score);
		stuAddress = (EditText) findViewById(R.id.address);

		mBundle = getIntent().getExtras();
		if (mBundle != null && mBundle.getString("action").equals("viewone")) {
			// 长按一个用户信息显示查看菜单
			mStudent = (Student) getIntent().getSerializableExtra("entity");
			setStudentUIData(mStudent);
		}

		if (mBundle != null && mBundle.getString("action").equals("edit")) {
			// 长按一个用户信息显示编辑菜单
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
			// 编辑状态下，就只有一个“保存”菜单键
			menu.add(1, MENU_EDIT, 0, "保存");
		} else {
			// 其他状态下，有“增加”“查看”“数据流”菜单选项
			menu.add(0, MENU_ADD, 0, "增加");
			menu.add(0, MENU_VIEWALL, 0, "查看");
			menu.add(0, MENU_DATA, 0, "数据流");
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD:
			try {
				stuDao = getHelper().getStudentDao();
				getStudentData();
				if (mStudent != null) {
					// 创建记录项
					stuDao.create(mStudent);
				} else {
					// 编辑时信息不能为空
					Toast.makeText(getApplicationContext(), "用户信息不能为空",
							Toast.LENGTH_LONG);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case MENU_VIEWALL:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, Studentlist.class);
			startActivity(intent);
			break;
		case MENU_DATA:
			Intent intentData = new Intent();
			intentData.setClass(MainActivity.this, MyWifiActivity.class);
			startActivity(intentData);
			break;
		case MENU_EDIT:
			try {
				getStudentData();
				stuDao = getHelper().getStudentDao();
				if (mStudent != null) {
					// 更新某记录项
					stuDao.update(mStudent);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 获取界面值(实体信息)
	 */
	private void getStudentData() {
		mStudent = new Student();
		mStudent.setStuNO(stuNO.getText().toString());
		mStudent.setName(stuName.getText().toString());
		mStudent.setAge(Integer.parseInt(stuAge.getText().toString()));
		mStudent.setSex(stuSex.getText().toString());
		// mStudent.setScore(Double.parseDouble(stuScore.getText().toString()));
		// ***使数据列表中的数据实时显示
		// mStudent.setScore();
		mStudent.setAddress(stuAddress.getText().toString());
	}

	/**
	 * 赋值给UI界面
	 * 
	 * @param student
	 */
	private void setStudentUIData(Student student) {
		stuNO.setText(student.getStuNO());
		stuName.setText(student.getName());
		stuAge.setText(String.valueOf(student.getAge()));
		stuSex.setText(student.getSex());
		stuScore.setText(student.getScore());
		// stuScore.setText(MyWifiActivity.strTemp);
		stuAddress.setText(student.getAddress());
		updateTemp();
	}

	public void updateTemp() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				stuScore.setText(MyWifiActivity.strTemp);
			}
		});
	}

}
