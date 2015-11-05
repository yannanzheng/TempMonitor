package com.ormlitedemo.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ormlitedemo.R;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;
import com.ormlitedemo.utils.TemperatureTableUtil;
import com.ormlitedemo.wifi.NetworkStateChangedCallback;
import com.ormlitedemo.wifi.TemperatureData;
import com.ormlitedemo.wifi.TemperatureObserver;

public class HomeActivity extends Activity implements TemperatureObserver,NetworkStateChangedCallback{
	   
    private Context mContext;  
    private ListView stuListView;  
    private static final String TAG = "HomeActivity";
    private static final int TEMPERATURE_CHANGED=1;
    private static final int STUDENTDELETED=2;
    private List<Student> allStudentsList=null;  
    private List<Student> adapterStudents=new ArrayList<Student>();  
    private StudentsAdapter adapter;  
    String temp;
    public static HomeActivity homeActivity;
    private View contentView;
    
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TEMPERATURE_CHANGED:
			{
				String strData=(String)msg.obj;
				String strDeviceId=StringUtils.parseDeviceId(strData);
				String strTempData=StringUtils.parseTemperature(strData);
				String strTemp=TemperatureTableUtil.queryTemperatureByData(mContext, strTempData);
				Log.w(TAG, "查询到的体温为: "+strTemp);
				Student stu=null;
				if (StudentDao.isExistDevice(strDeviceId)) {
					StudentDao.getStudentDao(mContext).updateTemperatureById(strDeviceId, strTemp);
					
				}else{
					stu=new Student();
					stu.setDeviceID(strDeviceId);
					stu.setTemper(strTemp);
					StudentDao.getStudentDao(mContext).addStudent(stu);
					
					
				}
				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
				Log.i(TAG, "��ѯѧ��"+allStudentsList.toString());
				adapterStudents.clear();
				adapterStudents.addAll(allStudentsList);
				
				adapter.notifyDataSetChanged();
				
				
			}
				break;
				
			case STUDENTDELETED:
			{
				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
				adapterStudents.clear();
				adapterStudents.addAll(allStudentsList);
				
				adapter.notifyDataSetChanged();
			}
				break;

			default:
				break;
			}
		
		}
	};
	
	
	

    @Override
	protected void onPause() {
		super.onPause();
		temperatureData.removeObserver(this);
		
	}


	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);  
        //TODO 空指针异常
        mContext = getApplicationContext(); 
        contentView = View.inflate(mContext, R.layout.popup_window, null);
        homeActivity=this;
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
		strWifiSSID = wifiInfo.getSSID();
		String strWiFi="\"R2WiFi\"";
		
		temperatureData = TemperatureData.getTemperatureData();
		if (strWifiSSID.equals(strWiFi)) {
			Log.i(TAG, "连接上R2WiFi");
			thread = getThread(thread, temperatureData);
			thread.start();
		}else{
			Toast.makeText(mContext, "请连接名为R2WiFi的wifi", 1).show();
		}
        initView();  
        
        
        adapter = new StudentsAdapter(); 
     	stuListView.setAdapter(adapter); 
     	
     	stuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startAddStudentActivity(parent, position);
				
			}
		});
     	
     	stuListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				
				final PopupWindow pw=new PopupWindow(contentView,ViewGroup.LayoutParams.WRAP_CONTENT, -2);
				pw.setBackgroundDrawable(new BitmapDrawable());
				pw.setFocusable(true);
				int[] location = new int[2];//x 轴和 Y 轴
				view.getLocationInWindow(location);//给location数组赋值
				//显示  在x轴上偏移
				int x = location[0] + 200;
				int y = location[1];
				pw.showAtLocation(view, Gravity.TOP|Gravity.LEFT, x, y);
				pw_edit_bt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startAddStudentActivity(parent, position);
						pw.dismiss();
					}
				});
				
				pw_delete_bt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//TODO 删除该学生
						//1，删除学生
						StudentDao.getStudentDao(mContext).deleteStudent((Student)parent.getAdapter().getItem(position));
						//2，dismiss那个玩意
						pw.dismiss();
						//3，通知刷新
						Message msg=Message.obtain();
						//msg.what=TEMPERATURE_CHANGED;
						mHandler.sendEmptyMessage(STUDENTDELETED);
						
					}
				});
				
				
				
				return true;
			}
		});
     	
     	
     
    }
	
	private Thread getThread(Thread thread,Runnable runnable){
		if (thread==null) {
			thread=new Thread(runnable);
		}
		
		return thread;
	}


@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==40) {
			
		}

}


@Override
	protected void onResume() {
		super.onResume();
		allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
		adapterStudents.clear();
     	adapterStudents.addAll(allStudentsList);
     	
     	temperatureData.registerObserver(this);
		
	}


public static TemperatureData temperatureData;
private Thread thread;
private WifiManager wifiManager;
private WifiInfo wifiInfo;
private String strWifiSSID;
private Button pw_edit_bt;
private Button pw_delete_bt;





	private void initView() {
        stuListView = (ListView)findViewById(R.id.stu_lv);
        pw_edit_bt = (Button) contentView.findViewById(R.id.pw_edit_bt);
       // pw_edit_bt.setOnClickListener(this);
        pw_delete_bt = (Button) contentView.findViewById(R.id.pw_delete_bt);
       // pw_delete_bt.setOnClickListener(this);
	}
    
	
	
    class StudentsAdapter extends BaseAdapter{  
          
  
        @Override  
        public int getCount() {  
            return adapterStudents.size();  
        }  
  
        @Override  
        public Student getItem(int position) {  
            return adapterStudents.get(position);  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            ViewHolder holder;  
            if(convertView == null){  
                convertView = View.inflate(mContext, R.layout.studentitem, null);  
                holder = new ViewHolder();  
                holder.student_number_tv = (TextView)convertView.findViewById(R.id.student_number_tv);  
                holder.student_name_tv = (TextView)convertView.findViewById(R.id.student_name_tv);  
                holder.student_temper_tv = (TextView)convertView.findViewById(R.id.student_temper_tv);  
                convertView.setTag(holder);  
            }else{  
                holder = (ViewHolder)convertView.getTag();  
            }  
              
            Student objStu = adapterStudents.get(position);  
            holder.student_number_tv.setText(objStu.getStuNo());  
            holder.student_name_tv.setText(objStu.getName());  
            holder.student_temper_tv.setText(objStu.getTemper());
            if ("107743" == objStu.getDeviceID())
                 holder.student_temper_tv.setTextColor(android.graphics.Color.RED);
            
            return convertView;  
        }         
    }  
      
    static class ViewHolder{  
        TextView student_number_tv;  
        TextView student_name_tv;  
        TextView student_temper_tv;  
    }

	@Override
	public void updateTemperature(String strData) {
		Message msg=Message.obtain();
		msg.what=TEMPERATURE_CHANGED;
		msg.obj=strData;
		mHandler.sendMessage(msg);
		
		
	}


	@Override
	public synchronized void networkStateChangedCallback() {
		String strWifiSSID = wifiManager.getConnectionInfo().getSSID().trim();
		Log.i(TAG, "ssid="+strWifiSSID);
		temperatureData = TemperatureData.getTemperatureData();
		thread = getThread(thread, temperatureData);
		
		Log.i(TAG, "ssidequals(strWifiSSID)"+"R2WiFi".equals(strWifiSSID));
		if ("\"R2WiFi\"".equals(strWifiSSID)) {
			
			if (!thread.isAlive()) {
				
				thread.start();
				
			}
		}
		
		
	}



	private void startAddStudentActivity(AdapterView<?> parent, int position) {
		Intent intent=new Intent(getApplicationContext(),AddStudentActivity.class);
		Student stu=(Student) parent.getAdapter().getItem(position);
		intent.putExtra("student", stu);
		startActivityForResult(intent, 40);
	}
    

}
