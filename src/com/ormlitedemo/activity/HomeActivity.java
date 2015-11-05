package com.ormlitedemo.activity;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ormlitedemo.R;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;
import com.ormlitedemo.wifi.NetworkStateChangedCallback;
import com.ormlitedemo.wifi.TemperatureData;
import com.ormlitedemo.wifi.TemperatureObserver;

public class HomeActivity extends Activity implements TemperatureObserver,NetworkStateChangedCallback{
	   
    private Context mContext;  
    private ListView stuListView;  
    private static final String TAG = "HomeActivity";
    private static final int TEMPERATURE_CHANGED=1;
    private List<Student> allStudentsList=null;  
    private List<Student> adapterStudents=new ArrayList<Student>();  
    private StudentsAdapter adapter;  
    String temp;
    public static HomeActivity homeActivity;
    
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TEMPERATURE_CHANGED:
			{
				String strData=(String)msg.obj;
				String strDeviceId=StringUtils.parseDeviceId(strData);
				String strTemp=StringUtils.parseTemperature(strData);
				Log.i(TAG, "����Ϣ����"+strData);
				
				
				Student stu=new Student();
				stu.setDeviceID(strDeviceId);
				stu.setTemper(strTemp);
				if (StudentDao.isExistDevice(strDeviceId)) {
					Log.i(TAG, "����ѧ��"+strData);
					StudentDao.getStudentDao(mContext).updateTemperatureById(strDeviceId, strTemp);
					
				}else{
					Log.i(TAG, "������ѧ��"+strData);
					stu.setName("zhangsan");
					Log.i(TAG, "����ѧ��"+stu.toString());
					StudentDao.getStudentDao(mContext).addStudent(stu);
					
					
				}
				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
				Log.i(TAG, "��ѯѧ��"+allStudentsList.toString());
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
        mContext = getApplicationContext(); 
        homeActivity=this;
        Properties props=new Properties();
        try {
			InputStream in=mContext.getAssets().open("temperTable.properties");
			props.load(in);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
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
			Toast.makeText(mContext, "��������R2WiFi����������Ӧ��", 1).show();
		}
        initView();  
        
        
        adapter = new StudentsAdapter(); 
     	stuListView.setAdapter(adapter); 
     	
     	stuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				Intent intent=new Intent(getApplicationContext(),AddStudentActivity.class);
				Student stu=(Student) parent.getAdapter().getItem(position);
				Log.i(TAG, "ѧ����Ŀ����"+stu.toString());
				intent.putExtra("student", stu);
				//intent.put
				startActivityForResult(intent, 40);
				//Toast.makeText(mContext, "�����Ŀ��"+position, 0).show();
				
			}
		});
     	
     	stuListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, "长按了"+position+"条目", 1).show();
				return false;
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





	private void initView() {
        stuListView = (ListView)findViewById(R.id.stu_lv);
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
    

}
