package com.ormlitedemo.wifi;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.ormlitedemo.dao.StudentDao;
import com.ormlitedemo.utils.StringUtils;

/**
 * ���ù۲���ģʽ�����±仯���м��
 * @author jfy
 *
 */
public class TemperatureData implements Runnable,TemperatureSubject {

	//TODO Ӧ�ü��wifi���ӣ�һ��������wifi��֪ͨ��߿�����û�еĻ�����ʾ��������֮���
	private static final String TAG = "TemperatureData";
	//private TemperatureObserver temperListener;
	private String temperature="";
	public ArrayList<TemperatureObserver> observers;
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
			byte[] byteSrc = new byte[1024];
			//int length = -1;
			while (true) {
				int length = in.read(byteSrc);
				//System.out.println("��ȡ�������ݵĳ���--->"+length);
				Log.i(TAG, "ԭʼ�ֽ�����" + byteSrc.toString());
				String strSrc=StringUtils.bytesToHexString(byteSrc, length);
//				Log.i(TAG, "ԭʼ����תΪ16�����ַ���" + strSrc);
				
				
//				if (strSrc.contains("ff")) {
//					//���ַ��������и�
//					String strData=strSrc.substring(strSrc.indexOf("ff")+2);
//					Log.i(TAG, "�и�һ�ε��ַ���"+strData);
//					String strTemp=strData.substring(0, 4);
//					Log.i(TAG, "�¶�"+strTemp);
//					String strDeivceId=strData.substring(4);
//					Log.i(TAG, "�豸id"+strDeivceId);
//					
//					
//				}
				
				
				//�ַ���
				for (int i = 0; i < length; i++) {
					
					if (byteSrc[i]==-1) {
						Log.i(TAG, "***********************��ʼһ��**************************");
						byte[] data=new byte[10];
						data[0]=byteSrc[i+1];
						data[1]=byteSrc[i+2];
						data[2]=byteSrc[i+3];
						data[3]=byteSrc[i+4];
						data[4]=byteSrc[i+5];
						data[5]=byteSrc[i+6];
						data[6]=byteSrc[i+7];
						data[7]=byteSrc[i+8];
						data[8]=byteSrc[i+9];
						data[9]=byteSrc[i+10];
						
						String strData=StringUtils.bytesToHexString(data, data.length);
						notifyObservers(strData);
						
						//���������豸���¶����ݺ��豸id
						byte[] byteTemp=new byte[2];
						byteTemp[0]=byteSrc[i+1];
						byteTemp[1]=byteSrc[i+2];
						//ת�����ַ���
						String strTemper=StringUtils.bytesToHexString(byteTemp, byteTemp.length);
						Log.i(TAG, "�¶�����:strTemper=" +strTemper);
						//��ȡ�������ݺ���ļ����ֽ���Ϊ�豸��id
						byte[] byteDeviceNo=new byte[8];
						byteDeviceNo[0]=byteSrc[i+3];
						byteDeviceNo[1]=byteSrc[i+4];
						byteDeviceNo[2]=byteSrc[i+5];
						byteDeviceNo[3]=byteSrc[i+6];
						byteDeviceNo[4]=byteSrc[i+7];
						byteDeviceNo[5]=byteSrc[i+8];
						byteDeviceNo[6]=byteSrc[i+9];
						byteDeviceNo[7]=byteSrc[i+10];
						String strDeviceNo=StringUtils.bytesToHexString(byteDeviceNo, byteDeviceNo.length);
						
						//֪ͨ�¶ȸı�
						//notifyObservers(strTemper);
						Log.i(TAG, "�豸strDeviceNo=" + strDeviceNo);
						Log.i(TAG, "***********************����һ��**************************");
					}
					
					
				}
				
				
			
			
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
