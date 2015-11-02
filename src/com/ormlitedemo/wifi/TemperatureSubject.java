package com.ormlitedemo.wifi;

/**
 * 主题，观察者观察的对象
 * @author jfy
 *
 */
public interface TemperatureSubject {
  
	/**
	 * 注册观察者
	 * @param tempObj
	 */
	public void registerObserver(TemperatureObserver tempObj);
	/**
	 * 取消观察者
	 * @param tempObj 
	 */
  public void removeObserver(TemperatureObserver tempObj);
  /**
   * 通知，并传递设备id和与之对应的温度信息
   * @param strData
   */
  public void notifyObservers(String strData);
  
}
