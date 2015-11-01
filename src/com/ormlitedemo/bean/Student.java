package com.ormlitedemo.bean;

import java.io.Serializable;  

import com.j256.ormlite.field.DatabaseField; 
import com.ormlitedemo.TemperData;
import com.ormlitedemo.activity.AddStudentActivity;
import com.ormlitedemo.wifi.MyWifiActivity;

public class Student implements Serializable{
    
	private static final long serialVersionUID = -8551683803719520351L;

	 /**
     * ѧ��
     */
    @DatabaseField(id=true)
    private String stuNo;  
	
	/**
	 * ����豸��id,�����������
	 */
    @DatabaseField 
    private String deviceID; 
    
   
    
    /**
     * ����
     */
    @DatabaseField
    private String name;  
    
    /**
     * ����
     */
    @DatabaseField  
    private String age; 
    
    /**
     * �Ա�
     */
    @DatabaseField  
    private String sex;  
    
    /**
     * ����
     */
    @DatabaseField  
    private String temper;
    
    /**
     * ��ͥ��ַ
     */
    @DatabaseField  
    private String address;  
    
    /**
     * ��ϵ��ʽ
     */
    @DatabaseField
    private String phoneNum;  
      
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
      
    
      
    public String getAge() {
		return age;
	}
	public void setAge(String age) {
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
	public String getStuNo() {
		return stuNo;
	}
	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	@Override
	public String toString() {
		return "Student [deviceID=" + deviceID + ", stuNo=" + stuNo + ", name="
				+ name + ", age=" + age + ", sex=" + sex + ", temper=" + temper
				+ ", address=" + address + ", phoneNum=" + phoneNum + "]";
	}  
    
    


}
