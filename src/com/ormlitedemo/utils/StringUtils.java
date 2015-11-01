package com.ormlitedemo.utils;

public class StringUtils {
	
	/**
	 * ���¶�����ת����ʮ�����������ַ�����ʽ
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src, int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xff;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			
//			if (i==length-1) {
//				stringBuilder.append(hv);
//			}else{
//				stringBuilder.append(hv+":");
//			}
			
			stringBuilder.append(hv);
			
		}
		System.out.println("������������ݣ�"+stringBuilder.toString());
		
		return stringBuilder.toString();

	}
	
	public static String parseTemperature(String strData){
		String strTemp=strData.substring(0,4);
		return strTemp;
		
	}
	public static String parseDeviceId(String strData){
		String strDeviceId=strData.substring(4);
		return strDeviceId;
		
	}
	
}
