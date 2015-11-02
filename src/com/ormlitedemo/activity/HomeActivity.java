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
				Log.i(TAG, "����Ϣ����"+strData);
				
				
				Student stu=new Student();
				stu.setDeviceID(strDeviceId);
				stu.setTemper(strTemp);
				if (StudentDao.isExistDevice(strDeviceId)) {
					//���ڸ�ѧ���������¶�
					Log.i(TAG, "����ѧ��"+strData);
					StudentDao.getStudentDao(mContext).updateStudent(stu);
					
				}else{
					//�����ڸ�ѧ�����½���ѧ�����ݲ������¶�����
					Log.i(TAG, "������ѧ��"+strData);
					stu.setName("zhangsan");
					Log.i(TAG, "����ѧ��"+stu.toString());
					StudentDao.getStudentDao(mContext).addStudent(stu);
					
					
				}
				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
				Log.i(TAG, "��ѯѧ��"+allStudentsList.toString());//��ѯ�����ʿ�
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
        //ģ�����ݽ��� 
 //       dataEngineMonitor(2000);
        initView();  
        
        //ȥ���Ĺ���
//        add_student_tv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//TODO ���ѧ��
//				//Toast.makeText(mContext, "���ѧ��", 0).show();
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
				//Toast.makeText(mContext, "�����Ŀ��"+position, 0).show();
				
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


	//*************************************ģ�������ϱ߽�***************************************************
private int j=0;
public static TemperatureData temperatureData;
private Thread thread;
   /**
    * ģ�����ݷ���
    * @param delay ���ݷ��͵ļ��ʱ��
    */
	private void dataEngineMonitor(long delay) {
		
		Timer timer=new Timer();
		
		
        timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				j++;
				Log.i(TAG, "��ʱ��������"+j+"��");
				
				for (int i = 0; i < adapterStudents.size(); i++) {
					adapterStudents.get(i).setTemper(j+"���϶�");
					
					//adapter.notifyDataSetChanged();
					Message msg=Message.obtain();
					msg.obj=""+j;
					msg.what=1;
					mHandler.sendEmptyMessage(1);
					
				}
				
			}
		}, 0, delay);
	}
	
	
	//*************************************ģ�������±߽�***************************************************


    /**
     * �����ݿ��е��¶ȸ���Ϊ���µ��¶�
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
				Log.i(TAG, "strTempΪ��");
				allStudentsList.get(i).setTemper("36.5���϶�");
				
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
            //����ɫ���
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
		//�¶ȸı���
		//Log.i(TAG, "�¶ȸı��ˡ�����"+temper);//���Իص��ɹ�
		Message msg=Message.obtain();
		msg.what=TEMPERATURE_CHANGED;
		msg.obj=strData;
		//updateTemper(temper);
//		for (int i = 0; i < adapterStudents.size(); i++) {
//			
//			adapterStudents.get(i).setTemper(strData+"���϶�");
//			
//		}
		mHandler.sendMessage(msg);
		
		
	}
    

}
