package com.ormlitedemo.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ormlitedemo.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.ormlitedemo.TemperData;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.db.DatabaseHelper;
import com.ormlitedemo.wifi.MySocket;
import com.ormlitedemo.wifi.MySocket.TemperatureChangeListener;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> implements TemperatureChangeListener{
	   
    private Context mContext;  
    private ListView stuListView;  
    private StudentDao stuDao;
    private static final String TAG = "HomeActivity";
    
    private List<Student> allStudentsList=null;  
    private List<Student> adapterStudents=new ArrayList<Student>();  
    private StudentsAdapter adapter;  
    private Student mStudent; 
    String temp;
      
    private final int MENU_VIEW = Menu.FIRST;  
    private final int MENU_EDIT = Menu.FIRST+1;  
    private final int MENU_DELETE = Menu.FIRST+2;  
      
    private int position; 
    
    private Timer dataTimer;
	private TimerTask dataTimerTask = null;
	private Button add_student_bt;
	
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_home);  
        mContext = getApplicationContext(); 
        
        //�����߳̽�������
        new Thread(new MySocket(HomeActivity.this)).start();
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //updateTemper();
		
        initTimer();
        initView();  
        //TODO registerForContextMenu(stuListView);  //ע�������Ĳ˵�    
        add_student_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "���ѧ��", 0).show();
				
				
				
			}
		});
        
        adapter = new StudentsAdapter(adapterStudents); 
        
        
        
     	stuListView.setAdapter(adapter); 
     	adapter.notifyDataSetChanged();
     	stuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, "�����Ŀ��"+position, 0).show();
				
			}
		});
     	
     	
     
    }



    /**
     * �����ݿ��е��¶ȸ���Ϊ���µ��¶�
     */
	private void updateTemper(String temp) {
		allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
		
        for (int i = 0; i < allStudentsList.size(); i++) {
			if (temp!=null) {
				
				allStudentsList.get(i).setTemper(temp);
				//Log.i(TAG, "wendu"+stus.get(i).getTemper());
				StudentDao.getStudentDao(mContext).updateStudent(allStudentsList.get(i));
				adapterStudents.clear();
				adapterStudents.addAll(allStudentsList);
				
				//stuListView.setAdapter(adapter);
				
				
			}else {
				Log.i(TAG, "strTempΪ��");
				allStudentsList.get(i).setTemper("36.5���϶�");
				
				StudentDao.getStudentDao(mContext).updateStudent(allStudentsList.get(i));
			}
		}
	}



	private void initView() {
		add_student_bt = (Button) findViewById(R.id.add_student_bt);
        stuListView = (ListView)findViewById(R.id.stu_lv);
	}



    /**
     * ��ʼ����ʱ����ÿ��5s�����ݿ��ѯһ�����ݣ����������������󶨵ļ���
     */
	private void initTimer() {
		//��ʱ��
        dataTimer = new Timer("Light");
 		dataTimerTask = new TimerTask() {
 			@Override
 			public void run() {
 				temp = TemperData.strTemp;
 				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
 				adapterStudents.clear();
 				adapterStudents.addAll(allStudentsList);
 				
 			}
 		};
 		
 		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);
	}  
    
   
  
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,  
            ContextMenuInfo menuInfo) {  
        if(v == stuListView)  
            position = ((AdapterContextMenuInfo)menuInfo).position;  
          
        menu.add(0,MENU_VIEW, 0, "�鿴");  
        menu.add(0,MENU_EDIT, 0, "�༭");  
        menu.add(0,MENU_DELETE,0,"ɾ��");  
        super.onCreateContextMenu(menu, v, menuInfo);  
    }  
      
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
        switch (item.getItemId()) {  
        case MENU_VIEW:  
            viewListViewItem(position);  
            break; 
        case MENU_EDIT:  
            editListViewItem(position);  
            break;  
        case MENU_DELETE:  
            //TODO deleteListViewItem(position);  
            break;  
        default:  
            break;  
        }  
        return super.onContextItemSelected(item);  
    }  
      
      
    /**  
     * �鿴��¼��  
     * @param position  
     */  
    private void viewListViewItem(int position){  
        mStudent = allStudentsList.get(position);  
        Intent intent = new Intent();  
        intent.setClass(mContext, StudentDetailActivity.class);  
        intent.putExtra("action", "viewone");  
        intent.putExtra("entity", mStudent);  
        startActivity(intent);  
    }  
      
    /**  
     * �༭��¼��  
     */  
    private void editListViewItem(int position){  
        mStudent = allStudentsList.get(position);  
        Intent intent = new Intent();  
        intent.setClass(mContext, StudentDetailActivity.class);  
        intent.putExtra("action", "edit");  
        intent.putExtra("entity", mStudent);  
        startActivity(intent);  
    }  
      
//    /**  
//     * ɾ����¼��  
//     * @param position  
//     */  
//    private void deleteListViewItem(int position){  
//        final int pos = position;  
//        AlertDialog.Builder builder2 = new AlertDialog.Builder(StudentListActivity.this);  
//        builder2.setIcon(android.R.drawable.ic_dialog_alert)  
//                .setTitle("����")  
//                .setMessage("ȷ��Ҫɾ���ü�¼");  
//        builder2.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
//              
//            @Override  
//            public void onClick(DialogInterface dialog, int which) {  
//                Student mDelStudent = (Student)stuListView.getAdapter().getItem(pos);  
//                 
//                    stuDao.deleteStudent(mDelStudent) ;//ɾ����¼  
//                    queryListViewItem();  
//                 
//                  
//            }  
//        });  
//        builder2.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
//              
//            @Override  
//            public void onClick(DialogInterface dialog, int which) {  
//                dialog.dismiss();  
//            }  
//        });  
//        builder2.show();  
//    }  
//      
    
    
    class StudentsAdapter extends BaseAdapter{  
          
        private List<Student> listStu;
          
        public StudentsAdapter(List<Student> students){  
            super();  
            this.listStu = students;  
        }  
  
        @Override  
        public int getCount() {  
            return listStu.size();  
        }  
  
        @Override  
        public Student getItem(int position) {  
            return listStu.get(position);  
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
              
            Student objStu = listStu.get(position);  
            holder.student_number_tv.setText(objStu.getStuNo());  
            holder.student_name_tv.setText(objStu.getName());  
            holder.student_temper_tv.setText(objStu.getTemper());
            //holder.student_temper_tv.setText("36.5���϶�");
       
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
	public void changeTemperature(String temper) {
		//�¶ȸı���
		Log.i(TAG, "�¶ȸı��ˡ�����"+temper);//���Իص��ɹ�
		//1���������ݿ��е��¶�Ϊ�ող鵽���¶�
		
		updateTemper(temper);
		
		
		//2����ѯ����
		
		
		
		
	}
    
    
    


}
