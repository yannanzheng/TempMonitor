package com.ormlitedemo.activity;


import java.sql.SQLException;  
import java.util.List;  
  


import java.util.Timer;
import java.util.TimerTask;

import android.R.string;
import android.app.AlertDialog;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;  
import android.os.Bundle;  
import android.view.ContextMenu;  
import android.view.ContextMenu.ContextMenuInfo;  
import android.view.LayoutInflater;  
import android.view.Menu;  
import android.view.MenuItem;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.AdapterView.AdapterContextMenuInfo;  
import android.widget.BaseAdapter;  
import android.widget.ListView;  
import android.widget.TextView;  
  





import com.ormlitedemo.TemperData;
import com.ormlitedemo.bean.Student;  
import com.ormlitedemo.db.DatabaseHelper;  
import com.ormlitedemo.wifi.MyWifiActivity;
import com.example.ormlitedemo.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;  
import com.j256.ormlite.dao.Dao;  

public class StudentListActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	   
    private Context mContext;  
    private ListView stuListView;  
    private Dao<Student,String> stuDao;  
    private List<Student> students;  
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
        
        dataTimer = new Timer("Light");
 		dataTimerTask = new TimerTask() {
 			@Override
 			public void run() {
 				temp = TemperData.strTemp;	
 			}
 		};
 		dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);
          
        stuListView = (ListView)findViewById(R.id.stu_lv);  
        registerForContextMenu(stuListView);  //ע�������Ĳ˵�    
        
        queryListViewItem();
        
        adapter = new StudentsAdapter(students);  
     	stuListView.setAdapter(adapter); 
     	
     	adapter.notifyDataSetChanged();
     
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
            deleteListViewItem(position);  
            break;  
        default:  
            break;  
        }  
        return super.onContextItemSelected(item);  
    }  
      
    /**  
     * ��ѯ��¼��  
     */  
    private void queryListViewItem(){  
        try {  
            stuDao = getHelper().getStudentDao();  
            //��ѯ���еļ�¼��  
            students = stuDao.queryForAll();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /**  
     * �鿴��¼��  
     * @param position  
     */  
    private void viewListViewItem(int position){  
        mStudent = students.get(position);  
        Intent intent = new Intent();  
        intent.setClass(mContext, HomeActivity.class);  
        intent.putExtra("action", "viewone");  
        intent.putExtra("entity", mStudent);  
        startActivity(intent);  
    }  
      
    /**  
     * �༭��¼��  
     */  
    private void editListViewItem(int position){  
        mStudent = students.get(position);  
        Intent intent = new Intent();  
        intent.setClass(mContext, HomeActivity.class);  
        intent.putExtra("action", "edit");  
        intent.putExtra("entity", mStudent);  
        startActivity(intent);  
    }  
      
    /**  
     * ɾ����¼��  
     * @param position  
     */  
    private void deleteListViewItem(int position){  
        final int pos = position;  
        AlertDialog.Builder builder2 = new AlertDialog.Builder(StudentListActivity.this);  
        builder2.setIcon(android.R.drawable.ic_dialog_alert)  
                .setTitle("����")  
                .setMessage("ȷ��Ҫɾ���ü�¼");  
        builder2.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                Student mDelStudent = (Student)stuListView.getAdapter().getItem(pos);  
                try {  
                    stuDao.delete(mDelStudent); //ɾ����¼  
                    queryListViewItem();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
                  
            }  
        });  
        builder2.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder2.show();  
    }  
      
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
                LayoutInflater mInflater = (LayoutInflater) mContext  
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                convertView = mInflater.inflate(R.layout.studentitem, null);  
                holder = new ViewHolder();  
                holder.tvNO = (TextView)convertView.findViewById(R.id.itemno);  
                holder.tvName = (TextView)convertView.findViewById(R.id.itemname);  
                holder.tvScore = (TextView)convertView.findViewById(R.id.itemscore);  
                convertView.setTag(holder);  
            }else{  
                holder = (ViewHolder)convertView.getTag();  
            }  
              
            Student objStu = listStu.get(position);  
            holder.tvNO.setText(objStu.getStuNO());  
            holder.tvName.setText(objStu.getName());  
            holder.tvScore.setText(objStu.getScore());
       
            //����ɫ���
            if ("107743" == objStu.getStuNO())
                 holder.tvScore.setTextColor(android.graphics.Color.RED);
            
            return convertView;  
        }         
        
        @Override
        public void notifyDataSetChanged() {
        	// TODO Auto-generated method stub
        	super.notifyDataSetChanged();
        }
        
        
    }  
      
    static class ViewHolder{  
        TextView tvNO;  
        TextView tvName;  
        TextView tvScore;  
    }  


}
