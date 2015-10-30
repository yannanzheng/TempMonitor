package com.ormlitedemo.wifi;

public interface TemperatureSubject {
  public void registerObserver(TemperatureObserver tempObj);
  public void removeObserver(TemperatureObserver tempObj);
  public void notifyObservers(String temp);
  
}
