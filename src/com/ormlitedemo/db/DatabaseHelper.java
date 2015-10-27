package com.ormlitedemo.db;

import java.sql.SQLException;  

import android.content.Context;  
import android.database.sqlite.SQLiteDatabase;  
import android.util.Log;  
  
import com.ormlitedemo.bean.Student;  
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;  
import com.j256.ormlite.dao.Dao;  
import com.j256.ormlite.support.ConnectionSource;  
import com.j256.ormlite.table.TableUtils; 

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	 private static final String DATABASE_NAME = "ormlite.db";  
	    private static final int DATABASE_VERSION = 1;  
	      
	    //private Dao<Student,String> stuDao = null;
	      
	    public DatabaseHelper(Context context){  
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	    }  
	  
	    /**  
	     * 创建SQLite数据库  
	     */  
	    @Override  
	    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {  
	        try {  
	            TableUtils.createTable(connectionSource, Student.class);  
	        } catch (SQLException e) {  
	            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);  
	        }  
	    }  
	  
	    /**  
	     * 更新SQLite数据库  
	     */  
	    @Override  
	    public void onUpgrade(  
	            SQLiteDatabase sqliteDatabase,   
	            ConnectionSource connectionSource,   
	            int oldVer,  
	            int newVer) {  
	        try {  
	            TableUtils.dropTable(connectionSource, Student.class, true);  
	            onCreate(sqliteDatabase, connectionSource);  
	        } catch (SQLException e) {  
	            Log.e(DatabaseHelper.class.getName(),   
	                    "Unable to upgrade database from version " + oldVer + " to new "  
	                    + newVer, e);  
	        }  
	    }  
	      
//	    public Dao<Student,String> getStudentDao() throws SQLException{  
//	        if(stuDao == null){  
//	            stuDao = getDao(Student.class);  
//	        }  
//	        return stuDao;  
//	    }  
	  
	    
	    public static  DatabaseHelper instance;
	    /**
	     * 单例获取DatabaseHelper
	     * @param context
	     * @return
	     */
	    public static synchronized DatabaseHelper  getHelper(Context context){
	    	context=context.getApplicationContext();
	    	if (instance==null) {
				synchronized (DatabaseHelper.class) {
					if (instance==null) {
						instance=new DatabaseHelper(context);
					}
				}
			}
	    	
	    	return instance;
	    	
	    }
	    
	    @Override
	    public void close() {
	    	// TODO Auto-generated method stub
	    	super.close();
	    }

}
