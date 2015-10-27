package com.ormlitedemo.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ormlitedemo.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.ormlitedemo.TemperData;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.db.DatabaseHelper;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	   
    private Context mContext;  
    private ListView stuListView;  
    
    //private Dao<Student,String> stuDao; 
    private StudentDao stuDao;
    
    private List<Student> allStudentsList=null;  
    private List<Student> adapterStudents=new ArrayList<Student>();  
    private StudentsAdapter adapter;  
    private Student mStudent; 
    StringBuffer temp;
      
    private final int MENU_VIEW = Menu.FIRST;  
    private final int MENU_EDIT = Menu.FIRST+1;  
    private final int MENU_DELETE = Menu.FIRST+2;  
      
    private int position; 
    
    private Timer dataTimer;
	private TimerTask dataTimerTask = null;
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.student_list);  
        mContext = getApplicationContext();  
        
        //定时器
        dataTimer = new Timer("Light");
 		dataTimerTask = new TimerTask() {
 			@Override
 			public void run() {
 				temp = TemperData.strTemp;
 				//queryListViewItem();
 				allStudentsList=StudentDao.getStudentDao(mContext).getAllStudent();
 				adapterStudents.clear();
 				adapterStudents.addAll(allStudentsList);
 				
 			}
 		};
 		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);
          
        stuListView = (ListView)findViewById(R.id.stu_lv);  
        registerForContextMenu(stuListView);  //注册上下文菜单    
        
        
        
        
        adapter = new StudentsAdapter(adapterStudents); 
        
     	stuListView.setAdapter(adapter); 
     	
     	adapter.notifyDataSetChanged();
     
    }  
    
   
  
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,  
            ContextMenuInfo menuInfo) {  
        if(v == stuListView)  
            position = ((AdapterContextMenuInfo)menuInfo).position;  
          
        menu.add(0,MENU_VIEW, 0, "查看");  
        menu.add(0,MENU_EDIT, 0, "编辑");  
        menu.add(0,MENU_DELETE,0,"删除");  
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
      
//    /**  
//     * 查询记录项  
//     */  
//    private void queryListViewItem(){  
//         
//            stuDao = StudentDao.getStudentDao(getApplicationContext());  
//            //查询所有的记录项  
//            students = stuDao.getAllStudent();  
//         
//    }  
      
    /**  
     * 查看记录项  
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
     * 编辑记录项  
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
//     * 删除记录项  
//     * @param position  
//     */  
//    private void deleteListViewItem(int position){  
//        final int pos = position;  
//        AlertDialog.Builder builder2 = new AlertDialog.Builder(StudentListActivity.this);  
//        builder2.setIcon(android.R.drawable.ic_dialog_alert)  
//                .setTitle("警告")  
//                .setMessage("确定要删除该记录");  
//        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//              
//            @Override  
//            public void onClick(DialogInterface dialog, int which) {  
//                Student mDelStudent = (Student)stuListView.getAdapter().getItem(pos);  
//                 
//                    stuDao.deleteStudent(mDelStudent) ;//删除记录  
//                    queryListViewItem();  
//                 
//                  
//            }  
//        });  
//        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
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
            return listStu.size();  //空指针异常
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
            holder.student_number_tv.setText(objStu.getDeviceID());  
            holder.student_name_tv.setText(objStu.getName());  
            holder.student_temper_tv.setText(objStu.getTemper());
            //holder.student_temper_tv.setText("36.5摄氏度");
       
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


}
