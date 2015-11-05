package com.ormlitedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class TemperatureTableUtil {
	
	private static Properties prop;
	
	public static Properties getProperties(Context c){
		Properties prop=new Properties();
		try {
			InputStream in=c.getAssets().open("temperTable.properties");
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prop;
	}
	
	public static String queryTemperatureByData(Context context,String tempData){
		
		return getProperties(context).getProperty(tempData,tempData);
		
	}

}
