package com.ormlitedemo.bean;

import java.io.Serializable;  

import com.j256.ormlite.field.DatabaseField; 
import com.ormlitedemo.TemperData;
import com.ormlitedemo.activity.HomeActivity;
import com.ormlitedemo.wifi.MyWifiActivity;

public class Student implements Serializable{
	private static final long serialVersionUID = -5683263669918171030L;  
    
    @DatabaseField(id=true)  
    private String deviceID;  
    @DatabaseField  
    private String name;  
    @DatabaseField  
    private int age;  
    @DatabaseField  
    private String sex;  
    @DatabaseField  
   // private double score;  
    private String temper;
    @DatabaseField  
    private String address;  
      
    public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
	public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
      
    public int getAge() {  
        return age;  
    }  
    public void setAge(int age) {  
        this.age = age;  
    }  
      
    public String getSex() {  
        return sex;  
    }  
    public void setSex(String sex) {  
        this.sex = sex;  
    }  
      
     
     
   public String getTemper() {
		return temper;
	}
	public void setTemper(String temper) {
		this.temper = temper;
	}
	/* public String getScore() {  
        return MyWifiActivity.strTemp.toString(); 
    }  
*/
    public String getAddress() {  
        return address;  
    }  
    public void setAddress(String address) {  
        this.address = address;  
    }  
    
    @Override
	public String toString() {
		return "Student [stuNO=" + getDeviceID() + ", name=" + getName() + ", age=" + getAge()
				+ ", sex=" + getSex() + ", score=" + getTemper() + ", address=" + getAddress()
				+ "]";
	}  


}
