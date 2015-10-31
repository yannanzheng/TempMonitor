package com.ormlitedemo.utils;

public class StringUtils {
	
	/**
	 * 将温度数据转换成十六进制数据字符串形式
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		System.out.println("---------------------");
		for(byte b : src)
		{
			System.out.print(b);
		}
		System.out.println("-------------------------");
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xff;
			String hv = Integer.toHexString(v);
			System.out.println("hv---->"+hv);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();

	}
}
