package com.ormlitedemo.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ormlitedemo.wifi.MySocket;

public class MyWifiActivityTest extends AndroidTestCase {
	private static final String TAG="MyWifiActivityTest";
	public void testUpdateData(){
		
	}
	public void testTranslate(){

    	String str=null;
    	char[] datas = new char[] {0xff,01,0xea,00,12,46,00,07,24,0xc8,06};
    	 for (int i=0;i<11;i++){
             //System.out.print(datas[i]);
            // Log.i(TAG, datas[i]);
             Log.i(TAG, String.valueOf(datas[i]));
            // System.out.print(String.valueOf(datas[i]));;
             
         }
    	 str = new String(datas);   //�ַ���ת������
    	 //System.out.println("�ַ�����" + str);
    	 Log.i(TAG, String.valueOf(str));
    	 StringBuffer text1 = new StringBuffer();
    	 text1.append(datas[0]).append(datas).append("\n");
    	 System.out.print("test:" + text1);
    	 Log.i(TAG, String.valueOf(text1));
    
	}
	
	public void testMySocket(){
		new Thread(new MySocket()).start();
	}
	
	

}
