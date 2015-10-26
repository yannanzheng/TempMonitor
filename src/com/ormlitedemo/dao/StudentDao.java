package com.ormlitedemo.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.ormlitedemo.activity.MainActivity;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.db.DatabaseHelper;
import com.ormlitedemo.ormlite.Studentlist;
/**
 * ���ݿ����
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
	 * �Խ��յ����ݽ������ݿ�ƥ�䣬��û�и��û�����������
	 * @param stu
	 */
	public boolean addStudent(String stuNO){
		try {
			
					if (!isExistStudent(stuNO)) {
						Log.i(TAG, "addStudent ��ѧ��������");
						Student stu = new Student();
						stu.setStuNO(stuNO);
						stu.setAddress("00");
						stu.setAge(00);
						stu.setName("00");
						stu.setSex("00");
						studentDaoOpe.createOrUpdate(stu);
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
				String stuNO=stu.getStuNO();
				if (!(stuNO==null)) {
					Log.i(TAG, "addStudent stuNO��Ϊ��");
					if (!isExistStudent(stuNO)) {
						Log.i(TAG, "addStudent ��ѧ��������");
						
						//stucentDaoOpe.create(stu);
						studentDaoOpe.createOrUpdate(stu);
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
			stu = studentDaoOpe.queryForId(stuNO);
			
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
			stus=studentDaoOpe.queryForAll();
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
	 * ɾ��ѧ��
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
