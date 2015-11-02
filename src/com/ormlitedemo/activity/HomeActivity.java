package com.ormlitedemo.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ormlitedemo.R;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;
import com.ormlitedemo.wifi.TemperatureData;
import com.ormlitedemo.wifi.TemperatureObserver;

public class HomeActivity extends Activity implements TemperatureObserver{
	   
    private Context mContext;  
    private ListView stuListView;  
    private static final String TAG = "HomeActivity";
    private static final int TEMPERATURE_CHANGED=1;
    private List<Student> allStudentsList=null;  
    private List<Student> adapterStudents=new ArrayList<Student>();  
    private StudentsAdapter adapter;  
    String temp;
    
//	private TextView add_student_tv;
	
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TEMPERATURE_CHANGED:
			{
				String strData=(String)msg.obj;
				String strDeviceId=StringUtils.parseDeviceId(strData);
				String strTemp=StringUtils.parseTemperature(strData);
				Log.i(TAG, "来信息啦！"+strData);
				
				
				Student stu=new Student();
				stu.setDeviceID(strDeviceId);
				stu.setTemper(strTemp);
				if (StudentDao.isExistDevice(strDeviceId)) {
					//存在该学生，更新温度
					Log.i(TAG, "存在学生"+strData);
					StudentDao.getStudentDao(mContext).updateStudent(stu);
					
				}else{
					//不存在该学生，新建该学生数据并保存温度数据
					Log.i(TAG, "不存在学生"+strData);
					stu.setName("zhangsan");
					Log.i(TAG, "创建学生"+stu.toString());
					StudentDao.getStudentDao(mContext).addStudent(stu);
					
					
				}
				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
				Log.i(TAG, "查询学生"+allStudentsList.toString());//查询到的问空
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
       
        
        
        temperatureData = TemperatureData.getTemperatureData();
        
        thread = new Thread(temperatureData);
        thread.start();
        //模拟数据接收 
 //       dataEngineMonitor(2000);
        initView();  
        
        //去掉改功能
//        add_student_tv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//TODO 添加学生
//				//Toast.makeText(mContext, "添加学生", 0).show();
//				Intent intent=new Intent(getApplicationContext(),AddStudentActivity.class);
//				//startActivity(intent);
//				
//				//startActivityForResult(intent, requestCode, options);
//				
//				startActivityForResult(intent, 40);
//			}
//		});
        
        
        
        adapter = new StudentsAdapter(); 
     	stuListView.setAdapter(adapter); 
     	
     	stuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				Intent intent=new Intent(getApplicationContext(),AddStudentActivity.class);
				Student stu=(Student) parent.getAdapter().getItem(position);
				intent.putExtra("student", stu);
				//intent.put
				startActivityForResult(intent, 40);
				//Toast.makeText(mContext, "点击项目是"+position, 0).show();
				
			}
		});
     	
     	
     
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


	//*************************************模拟数据上边界***************************************************
private int j=0;
public static TemperatureData temperatureData;
private Thread thread;
   /**
    * 模拟数据发送
    * @param delay 数据发送的间隔时间
    */
	private void dataEngineMonitor(long delay) {
		
		Timer timer=new Timer();
		
		
        timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				j++;
				Log.i(TAG, "定时器运行了"+j+"次");
				
				for (int i = 0; i < adapterStudents.size(); i++) {
					adapterStudents.get(i).setTemper(j+"摄氏度");
					
					//adapter.notifyDataSetChanged();
					Message msg=Message.obtain();
					msg.obj=""+j;
					msg.what=1;
					mHandler.sendEmptyMessage(1);
					
				}
				
			}
		}, 0, delay);
	}
	
	
	//*************************************模拟数据下边界***************************************************


    /**
     * 将数据库中的温度更新为最新的温度
     */
	private void updateTemper(String temp) {
		allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
		
        for (int i = 0; i < allStudentsList.size(); i++) {
			if (temp!=null) {
				
				allStudentsList.get(i).setTemper(temp);
				StudentDao.getStudentDao(mContext).updateStudent(allStudentsList.get(i));
				adapterStudents.clear();
				adapterStudents.addAll(allStudentsList);
				
			}else {
				Log.i(TAG, "strTemp为空");
				allStudentsList.get(i).setTemper("36.5摄氏度");
				
				StudentDao.getStudentDao(mContext).updateStudent(allStudentsList.get(i));
			}
		}
	}



	private void initView() {
//		add_student_tv = (TextView) findViewById(R.id.add_student_bt);
		
		
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
            //做颜色标记
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
		//温度改变了
		//Log.i(TAG, "温度改变了。。。"+temper);//测试回调成功
		Message msg=Message.obtain();
		msg.what=TEMPERATURE_CHANGED;
		msg.obj=strData;
		//updateTemper(temper);
//		for (int i = 0; i < adapterStudents.size(); i++) {
//			
//			adapterStudents.get(i).setTemper(strData+"摄氏度");
//			
//		}
		mHandler.sendMessage(msg);
		
		
	}
    

}
