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
 * 数据库操作
 */
public class StudentDao {
	
	private static Dao<Student, String> daoOpe;
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
	 * 对接收的数据进行数据库匹配，若没有该用户，则进行添加
	 * @param stu
	 */
	public boolean addStudentByID(String stuNO){
		try {
			
					if (!isExistDevice(stuNO)) {
						Log.i(TAG, "addStudent 该学生不存在");
						Student stu = new Student();
						stu.setDeviceID(stuNO);
						stu.setAddress("00");
						stu.setAge("00");
						stu.setName("00");
						stu.setSex("00");
						daoOpe.create(stu);
						return true;
					}else{
						Log.i(TAG, "addStudent 该学生已存在");
					}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean addStudent(Student stu){
		try {
			
			if (!(stu==null)) {
				Log.i(TAG, "addStudent stu不为空");
				//String stuNO=stu.getStuNo();
				String strDeviceId=stu.getDeviceID();
				if (!(strDeviceId==null)) {
					Log.i(TAG, "addStudent stuNO不为空");
					if (!isExistDevice(strDeviceId)) {
						Log.i(TAG, "addStudent 该设备不存在");
						
						//stucentDaoOpe.create(stu);
						daoOpe.create(stu);
						Log.i(TAG, "addStudent 该已经存在"+stu.toString());
						
						return true;
					}else{
						Log.i(TAG, "addStudent 该设备已存在");
					}
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 数据库中是否有该学生
	 * @param stuNO
	 */
	public static boolean isExistDevice(String id) {
		Student stu=getStudentById(id);
		if (stu==null) {
			return false;
		}
		return true;
		
	}

	/**
	 * 根据id查询学生对象
	 * @param stuNO
	 * @return
	 */
	public static Student getStudentById(String stuNO){
		
		Student stu=null;
		try {
			stu = daoOpe.queryForId(stuNO);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stu;
	}
	
	/**
	 * 查询所有学生
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
	 * TODO 更新学生信息
	 * @param stu
	 * @return
	 */
	public boolean updateStudent(Student stu){
		try {
			Student dbStu=daoOpe.queryForSameId(stu);
			//空指针异常
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
	public boolean updateTemperatureById(String strDeviceId,String temp){
		try {
			Student dbStu=daoOpe.queryForId(strDeviceId);
			//空指针异常
			if (dbStu!=null) {
				dbStu.setTemper(temp);
				daoOpe.update(dbStu);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * 删除学生
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
