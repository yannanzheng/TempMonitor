package com.ormlitedemo.test;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;

public class StudentDaoTest extends AndroidTestCase{
	
	private static final String TAG="STUDENTDAOTEST";
	public void testSava() throws Exception{
		assertEquals(3, 3);
		
	}
	
	
	public void testGetAllStudent() throws Exception{
		List<Student> stus=new ArrayList<Student>();
		
		Student stu1=new Student();
		stu1.setDeviceID(1+"");
		stu1.setName("zhangsan");
		stu1.setAge("2");
		Log.i(TAG, "ѧ��1��"+stu1.toString());
		
		Student stu2=new Student();
		Student stu3=new Student();
		Student stu4=new Student();
		Student stu5=new Student();
		Student stu6=new Student();
		stu2.setDeviceID(2+"");
		stu2.setName("Lisi");
		stu3.setDeviceID(3+"");
		stu4.setDeviceID(4+"");
		stu5.setDeviceID(5+"");
		stu6.setDeviceID(6+"");
		stu5.setName("wangwu");
		
		Context context=getContext();
		StudentDao dao=StudentDao.getStudentDao(getContext());
		Log.i(TAG, "���ݿ����ѧ����"+stus.size()+"��");
		dao.addStudent(stu1);
		dao.addStudent(stu2);
		dao.addStudent(stu3);
		dao.addStudent(stu4);
		dao.addStudent(stu5);
		dao.addStudent(stu6);
		
		
		stus.addAll(dao.getAllStudent());
		
		
		printStudents(stus);
		Log.i(TAG, "���ݿ����ѧ����"+stus.size()+"��");
		
	}
	
	
	/**
	 * �����ҵ�һ��ѧ��
	 */
	public void testGetStudent(){
		String stuNO="4";
		StudentDao dao=StudentDao.getStudentDao(getContext());
		Student stu=dao.getStudent(stuNO);
		Log.i(TAG, "�ҵ���ѧ����"+stu.toString());
		
	}
	
	public void testIsExistStudent() throws Exception{
		String stuNO="1";
		StudentDao dao=StudentDao.getStudentDao(getContext());
		boolean right=dao.isExistStudent(stuNO);
		assertEquals(right, true);
	
	}
	
	public void testAddStudent(){
		Student stu1=new Student();
		Student stu2=new Student();
		Student stu3=new Student();
		Student stu4=new Student();
		stu1.setDeviceID(1+"");
		stu1.setStuNo("1234");
		stu1.setName("zhangsan");
		stu2.setStuNo(2+"");
		stu2.setName("Lisi");
		stu3.setStuNo(3+"");
		stu4.setStuNo(4+"");
		Student stu=new Student();
		StudentDao dao=StudentDao.getStudentDao(getContext());
		dao.addStudent(stu1);
		dao.addStudent(stu2);
		dao.addStudent(stu3);
		dao.addStudent(stu4);
		
		
		stu.setStuNo("8");
		boolean right=dao.addStudent(stu);
		assertEquals(right, true);
		
		
	}
	
	
	public void printStudents(List<Student> stus){
		for (Student stu : stus) {
			if (!stus.isEmpty()) {
				Log.i(TAG, "�W����:"+stu.toString());
			}else {
				Log.i(TAG, "��û��ѧ��");
			}
		}
	}
	
	
	
	

}
