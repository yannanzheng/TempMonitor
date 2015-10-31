package com.ormlitedemo.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.ormlitedemo.activity.AddStudentActivity;
import com.ormlitedemo.activity.HomeActivity;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.db.TemperatureMonitorDatabaseHelper;
/**
 * ���ݿ����
 */
public class StudentDao {
	
	private Dao<Student, String> daoOpe;
	private TemperatureMonitorDatabaseHelper helper;
	private static final String TAG="STUDENTDAO";
	private  static StudentDao stuDao=null;
	
	private StudentDao(Context contxt) {
		try {
			helper=TemperatureMonitorDatabaseHelper.getHelper(contxt);
			daoOpe=this.getSingleOrmliteDao();
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	
	
	 public Dao<Student,String> getSingleOrmliteDao() throws SQLException{  
	        if(daoOpe == null){  
	        	daoOpe = helper.getDao(Student.class);  
	        }  
	        return daoOpe;  
	    }  
	
	public static StudentDao getStudentDao(Context contxt){
		if(stuDao == null){
			return new StudentDao(contxt);
		}
		
		return stuDao;
		
		
	}
	
	
	/**
	 * �Խ��յ����ݽ������ݿ�ƥ�䣬��û�и��û�����������
	 * @param stu
	 */
	public boolean addStudentByID(String stuNO){
		try {
			
					if (!isExistStudent(stuNO)) {
						Log.i(TAG, "addStudent ��ѧ��������");
						Student stu = new Student();
						stu.setDeviceID(stuNO);
						stu.setAddress("00");
						stu.setAge("00");
						stu.setName("00");
						stu.setSex("00");
						daoOpe.create(stu);
						return true;
					}else{
						Log.i(TAG, "addStudent ��ѧ���Ѵ���");
					}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean addStudent(Student stu){
		try {
			
			if (!(stu==null)) {
				Log.i(TAG, "addStudent stu��Ϊ��");
				String stuNO=stu.getStuNo();
				if (!(stuNO==null)) {
					Log.i(TAG, "addStudent stuNO��Ϊ��");
					if (!isExistStudent(stuNO)) {
						Log.i(TAG, "addStudent ��ѧ��������");
						
						//stucentDaoOpe.create(stu);
						daoOpe.create(stu);
						Log.i(TAG, "addStudent ��ѧ������"+stu.toString());
						
						return true;
					}else{
						Log.i(TAG, "addStudent ��ѧ���Ѵ���");
					}
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * ���ݿ����Ƿ��и�ѧ��
	 * @param stuNO
	 */
	public boolean isExistStudent(String stuNO) {
		Student stu=getStudent(stuNO);
		if (stu==null) {
			return false;
		}
		return true;
		
	}

	/**
	 * ����id��ѯѧ������
	 * @param stuNO
	 * @return
	 */
	public Student getStudent(String stuNO){
		
		Student stu=null;
		try {
			stu = daoOpe.queryForId(stuNO);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stu;
	}
	
	/**
	 * ��ѯ����ѧ��
	 * @return
	 */
	public List<Student> getAllStudent(){
		List<Student> stus=new ArrayList<Student>();
		try {
			stus=daoOpe.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stus;
		
	}
	
	
	/**
	 * TODO ����ѧ����Ϣ
	 * @param stu
	 * @return
	 */
	public boolean updateStudent(Student stu){
		try {
			Student dbStu=daoOpe.queryForSameId(stu);
			if (!dbStu.equals(stu)) {
				daoOpe.update(stu);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * ɾ��ѧ��
	 * @param stuNO
	 * @return
	 */
	public boolean deleteStudentByID(String stuNO){
		try {
			daoOpe.deleteById(stuNO);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean deleteStudent(Student stu){
		try {
			daoOpe.delete(stu);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	


}
