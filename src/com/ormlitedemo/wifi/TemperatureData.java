package com.ormlitedemo.wifi;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;

import com.ormlitedemo.TemperData;
import com.ormlitedemo.utils.StringUtils;

/**
 * ���ù۲���ģʽ�����±仯���м��
 * @author jfy
 *
 */
public class TemperatureData implements Runnable,TemperatureSubject {

	//TODO Ӧ�ü��wifi���ӣ�һ��������wifi��֪ͨ��߿�����û�еĻ�����ʾ��������֮���
	private static final String TAG = "MySocket";
	//private TemperatureObserver temperListener;
	private String temperature="";
	private ArrayList<TemperatureObserver> observers;
	private static TemperatureData temperatureData=null;


	private TemperatureData() {
		observers=new ArrayList<TemperatureObserver>();
		
	}
	
	public static TemperatureData getTemperatureData(){
		if(temperatureData==null){
			return new TemperatureData();
			
		}
		
		return temperatureData;
	}


	@Override
	public void run() {
		try {

			Socket s = new Socket("192.168.2.3", 5000);
			Log.i(TAG, "Socket����״̬Ϊ"+s.isConnected());
			
			InputStream in = s.getInputStream();
			byte[] buf = new byte[1024];

			DataInputStream input = new DataInputStream(in);
			while (true) {
				//Log.i(TAG, "MySocket������....****");
				int length = input.read(buf);
				//Log.i(TAG, "���յ������ݳ���"+length);
				for (int i = 0; i < length; i++) {
					if (buf[i] == -1) {
						
						byte[] byteTemp = new byte[2];//�����ֽ�������
						
						
						byteTemp[0] = buf[i + 1];
						byteTemp[1] = buf[i + 2];
						
						temperature=StringUtils.bytesToHexString(byteTemp);//ʮ�������ַ���
						
						
						//֪ͨ
						notifyObservers(temperature);
						
						
						//���÷��������ݴ��ݵ�HomeActivity��
						
						
						Log.i(TAG, "��ǰ�¶�"+temperature);
						byte[] byteAddr = new byte[2];
						byteAddr[0] = buf[i + 9];
						byteAddr[1] = buf[i + 10];
						TemperData.strNO = new StringBuffer(StringUtils.bytesToHexString(byteAddr));
					}
				}

				TemperData.text = new StringBuffer(StringUtils.bytesToHexString(buf));
				System.out.println("�յ��ͻ�����Ϣ��" + TemperData.text);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void registerObserver(TemperatureObserver tempObj) {
		observers.add(tempObj);
		
	}


	@Override
	public void removeObserver(TemperatureObserver tempObj) {

		int i=observers.indexOf(tempObj);
		if (i>=0) {
			observers.remove(i);
		}
	}


	@Override
	public void notifyObservers(String temp) {
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).updateTemperature(temp);
		}
		
	}

}
