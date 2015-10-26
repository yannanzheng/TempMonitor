package com.ormlitedemo.bean;

import java.io.Serializable;  

import com.j256.ormlite.field.DatabaseField; 
import com.ormlitedemo.ormlite.MainActivity;
import com.ormlitedemo.wifi.MyWifiActivity;

public class Student implements Serializable{
	private static final long serialVersionUID = -5683263669918171030L;  
    
    @DatabaseField(id=true)  
    private String stuNO;  
    @DatabaseField  
    private String name;  
    @DatabaseField  
    private int age;  
    @DatabaseField  
    private String sex;  
    @DatabaseField  
   // private double score;  
    private String score;
    @DatabaseField  
    private String address;  
      
    public String getStuNO() {  
        return stuNO;  
    }  
    public void setStuNO(String stuNO) {  
        this.stuNO = stuNO;  
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
      
    public String getScore() {  
        return score;  
    }  
    public void setScore() {  
        this.score = MyWifiActivity.strTemp.toString();  
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
		return "Student [stuNO=" + getStuNO() + ", name=" + getName() + ", age=" + getAge()
				+ ", sex=" + getSex() + ", score=" + getScore() + ", address=" + getAddress()
				+ "]";
	}  


}
