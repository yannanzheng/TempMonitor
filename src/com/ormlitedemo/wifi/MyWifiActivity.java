package com.ormlitedemo.wifi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import com.example.ormlitedemo.R;
import com.ormlitedemo.ormlite.MainActivity;

import android.R.integer;
import android.app.Activity;
import android.net.wifi.WifiEnterpriseConfig.Eap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyWifiActivity extends Activity{
	 private Button wifi_start_bt;
	 private EditText dataview;
	 Socket socket;
	 String str;
	 StringBuffer text;
	 public static StringBuffer strTemp,strNO;
	 byte[] buf;
	 int length = 0;
	 
	 private Timer dataTimer;
	 private TimerTask dataTimerTask = null;

	    
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_mywifi);
	        new Thread(new MySocket()).start();
	  //      test();

            dataview = (EditText) findViewById(R.id.dataview);
	        wifi_start_bt = (Button) findViewById(R.id.wifi_start_bt );
	        
	        //��������wifiģ��,��������ʵʱ��ʾ           	
        	dataview.setText(text);
        	
        	//���ö�ʱ���������ݽ�����ʾ
        	dataTimer = new Timer("Light");
			dataTimerTask = new TimerTask() {
				@Override
				public void run() {
					updateData();
				}
			};

			dataTimer.scheduleAtFixedRate(dataTimerTask, 0, 500);
	        

	    }
	    
	    
	    //���յ������ݸ�����ʾ
	    public void updateData() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {	
	
					dataview.setText(text);		
				}
			});
		}
	    
	    //g�����ַ�ת���Ĳ��Ժ���
	    public void test(){
	    	
	    	char[] datas = new char[] {0xff,01,0xea,00,12,46,00,07,24,0xc8,06};
	    	 for (int i=0;i<11;i++){
                 System.out.print(datas[i]);
                 System.out.print(String.valueOf(datas[i]));;
             }
	    	 str = new String(datas);   //�ַ���ת������
	    	 System.out.println("�ַ�����" + str);
	    	 
	    	 StringBuffer text1 = new StringBuffer();
	    	 text1.append(datas[0]).append(datas).append("\n");
	    	 System.out.print("test:" + text1);
	    }
	    

	    //����socket�ڲ��࣬�½��߳�ʵʱ��������
	    class MySocket implements Runnable{

	        @Override
	        public void run() {
	            try {
	            	
	            	Socket s=new Socket("192.168.2.3",5000);
	                System.out.print(s.isConnected());
	                
	                InputStream in=s.getInputStream();	       
	                buf = new byte[1024];
	                
	              /*  length = in.read(buf);  
	                	  for (int i=0;i<length;i++){
		                        System.out.print(buf[i] + " ");		                        
		                   }	*/
	                
	                DataInputStream input = new DataInputStream(in);
	                while(true){
	                	int length = input.read(buf);
	                	 for (int i=0;i<length;i++){
		                        System.out.print(buf[i] + " ");
		                        if (buf[i] == -1) {
		                        	byte[] byteTemp = new byte[2];
		                        	byteTemp[0] = buf[i+1];
		                        	byteTemp[1] = buf[i+2];
		                        	strTemp = new StringBuffer( bytesToHexString(byteTemp) );
		                        	byte[] byteAddr = new byte[2];
		                        	byteAddr[0] = buf[i+9];
		                        	byteAddr[1] = buf[i+10];
		                        	strNO = new StringBuffer( bytesToHexString(byteAddr) );
								}
	                	 }
		                        
	                	text = new StringBuffer( bytesToHexString(buf) ); 	
		                System.out.println("�յ��ͻ�����Ϣ��" + text);
	                }

	            } catch (Exception e) {
	                e.printStackTrace();
	                System.out.println("����"+e);
	            }
	        }
	    }
	    
	    public static String  bytesToHexString(byte[] src){
	    	
	    	StringBuilder stringBuilder = new StringBuilder("");
	    	if (src == null || src.length <= 0) {
				return null;
			}
	    	for(int i = 0; i < src.length; i++){
	    		int v = src[i] & 0xff;
	    		String hv = Integer.toHexString(v);
	    		if (hv.length() < 2) {
					stringBuilder.append(0);
				}
	    		stringBuilder.append(hv);
	    	}
			return stringBuilder.toString();
		
	    }	    

}
