package com.ormlitedemo.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.ormlitedemo.activity.HomeActivity;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.db.DatabaseHelper;
import com.ormlitedemo.ormlite.StudentList;
/**
 * 数据库操作
 */
public class StudentDao {
	private Context context=null;
	private Dao<Student, String> studentDaoOpe;
	private DatabaseHelper helper;
	private static final String TAG="STUDENTDAO";
	
	public StudentDao(Context contxt) {
		this.context=context;
		try {
			helper=DatabaseHelper.getHelper(contxt);
			studentDaoOpe=helper.getStudentDao();
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	
	/**
	 * 对接收的数据进行数据库匹配，若没有该用户，则进行添加
	 * @param stu
	 */
	public boolean addStudent(String stuNO){
		try {
			
					if (!isExistStudent(stuNO)) {
						Log.i(TAG, "addStudent 该学生不存在");
						Student stu = new Student();
						stu.setStuNO(stuNO);
						stu.setAddress("00");
						stu.setAge(00);
						stu.setName("00");
						stu.setSex("00");
						studentDaoOpe.createOrUpdate(stu);
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
				String stuNO=stu.getStuNO();
				if (!(stuNO==null)) {
					Log.i(TAG, "addStudent stuNO不为空");
					if (!isExistStudent(stuNO)) {
						Log.i(TAG, "addStudent 该学生不存在");
						
						//stucentDaoOpe.create(stu);
						studentDaoOpe.createOrUpdate(stu);
						Log.i(TAG, "addStudent 该学生存在"+stu.toString());
						
						return true;
					}else{
						Log.i(TAG, "addStudent 该学生已存在");
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
	public boolean isExistStudent(String stuNO) {
		Student stu=getStudent(stuNO);
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
	public Student getStudent(String stuNO){
		
		Student stu=null;
		try {
			stu = studentDaoOpe.queryForId(stuNO);
			
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
			stus=studentDaoOpe.queryForAll();
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
			Student dbStu=studentDaoOpe.queryForSameId(stu);
			if (!dbStu.equals(stu)) {
				studentDaoOpe.update(stu);
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
			studentDaoOpe.deleteById(stuNO);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	


}
