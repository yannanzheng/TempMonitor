package com.ormlitedemo.wifi;

/**
 * ���⣬�۲��߹۲�Ķ���
 * @author jfy
 *
 */
public interface TemperatureSubject {
  
	/**
	 * ע��۲���
	 * @param tempObj
	 */
	public void registerObserver(TemperatureObserver tempObj);
	/**
	 * ȡ���۲���
	 * @param tempObj 
	 */
  public void removeObserver(TemperatureObserver tempObj);
  /**
   * ֪ͨ���������豸id����֮��Ӧ���¶���Ϣ
   * @param strData
   */
  public void notifyObservers(String strData);
  
}
