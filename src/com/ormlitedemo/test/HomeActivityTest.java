package com.ormlitedemo.test;

import java.util.List;

import com.ormlitedemo.App;
import com.ormlitedemo.bean.Student;
import com.ormlitedemo.dao.StudentDao;

import android.test.AndroidTestCase;
import android.util.Log;

public class HomeActivityTest extends AndroidTestCase {
	private static final String TAG="HomeActivityTest";

	public void notifyDataSetChangedTest(){
		
		List<Student> stus=StudentDao.getStudentDao(getContext()).getAllStudent();
		
		
//			for (int i = 0; i < stus.size(); i++) {
//				if (!(App.strTemp==null)) {
//					
//					stus.get(i).setTemper(App.strTemp.toString());
//					Log.i(TAG, stus.get(i).getTemper());
//				}else {
//					Log.i(TAG, "strTempΪ��");
//				}
//			
//		}
		
		
	}
}
