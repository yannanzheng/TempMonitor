package com.ormlitedemo.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Student implements Serializable{
    
	private static final long serialVersionUID = -8551683803719520351L;

	 /**
     * 学号
     */
    @DatabaseField
    private String stuNo;  
	
	/**
	 * 佩戴设备的id,这个可以重用
	 */
    @DatabaseField(id=true) 
    private String deviceID; 
    
   
    
    /**
     * 姓名
     */
    @DatabaseField
    private String name;  
    
    /**
     * 年龄
     */
    @DatabaseField  
    private String age; 
    
    /**
     * 性别
     */
    @DatabaseField  
    private String sex;  
    
    /**
     * 体温
     */
    @DatabaseField  
    private String temper;
    
    /**
     * 家庭地址
     */
    @DatabaseField  
    private String address;  
    
    /**
     * 联系方式
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
				+ name + ", age=" + age + ", sex=" + sex + ", temper=" + 

temper
				+ ", address=" + address + ", phoneNum=" + phoneNum + 

"]";
	}  
    
    


}
