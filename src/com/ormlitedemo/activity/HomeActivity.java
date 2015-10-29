package com.ormlitedemo.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ormlitedemo.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.db.DatabaseHelper;
import com.ormlitedemo.wifi.MySocket;
import com.ormlitedemo.wifi.MySocket.TemperatureChangeListener;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> implements TemperatureChangeListener{
	   
    private Context mContext;  
    private ListView stuListView;  
    private static final String TAG = "HomeActivity";
    private static final int TEMPERATURE_CHANGED=1;
    
    
    private List<Student> allStudentsList=null;  
    private List<Student> adapterStudents=new ArrayList<Student>();  
    private StudentsAdapter adapter;  
    String temp;
	private Button add_student_bt;
	
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TEMPERATURE_CHANGED:
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		
		}
	};
	
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_home);  
        mContext = getApplicationContext(); 
        
        //开启线程接收数据
        new Thread(new MySocket(HomeActivity.this)).start();
        
        //模拟数据接收 
 //       dataEngineMonitor(2000);
        
//        try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
       // updateTemper();
		
       // initTimer();
        initView();  
        //TODO registerForContextMenu(stuListView);  //注册上下文菜单    
        add_student_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "添加学生", 0).show();
				
			}
		});
        
        adapter = new StudentsAdapter(); 
     	stuListView.setAdapter(adapter); 
     	//adapter.notifyDataSetChanged();
     	allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
     	adapterStudents.clear();
     	adapterStudents.addAll(allStudentsList);
     	
     	
     	stuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, "点击项目是"+position, 0).show();
				
			}
		});
     	
     	
     
    }


//*************************************模拟数据上边界***************************************************
    private int j=0;
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
		add_student_bt = (Button) findViewById(R.id.add_student_bt);
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
	public void changeTemperature(String temper) {
		//温度改变了
		Log.i(TAG, "温度改变了。。。"+temper);//测试回调成功
		//1，更新数据库中的温度为刚刚查到的温度
		
		Message msg=Message.obtain();
		msg.what=TEMPERATURE_CHANGED;
		//updateTemper(temper);
		for (int i = 0; i < adapterStudents.size(); i++) {
			
			adapterStudents.get(i).setTemper(temper+"摄氏度");
			
		}
		mHandler.sendMessage(msg);
		
		
		
		
	}
    
    
    


}
