package com.ormlitedemo.test;

import com.ormlitedemo.db.DatabaseHelper;

import android.content.Context;
import android.test.AndroidTestCase;

public class DataBaseHelperTest {
	
	public void testGetHelper() throws Exception{
	    Context context=getContext();
		DatabaseHelper instance=DatabaseHelper.getHelper(context);
		
	}

	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
