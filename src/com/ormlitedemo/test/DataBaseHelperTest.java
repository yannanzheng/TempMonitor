package com.ormlitedemo.test;

import com.ormlitedemo.db.TemperatureMonitorDatabaseHelper;

import android.content.Context;
import android.test.AndroidTestCase;

public class DataBaseHelperTest {
	
	public void testGetHelper() throws Exception{
	    Context context=getContext();
		TemperatureMonitorDatabaseHelper instance=TemperatureMonitorDatabaseHelper.getHelper(context);
		
	}

	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
