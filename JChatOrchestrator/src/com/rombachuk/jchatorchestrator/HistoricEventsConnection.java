package com.rombachuk.jchatorchestrator;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class HistoricEventsConnection {
	
	   private static boolean methodExists(Class clazz, String methodName) {
		    boolean result = false;
		    for (Method method : clazz.getDeclaredMethods()) {
		        if (method.getName().equals(methodName)) {
		            result = true;
		            break;
		        }
		    }
		    return result;
		}
	   
	  private JsonArray fetchFields(Connection connection) {
		  JsonArray fields = new JsonArray();
		  try {
		   if (connection != null) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select column_name,data_type,character_maximum_length from sysibm.columns where table_name='REPORTER_STATUS'");
            while(rs.next()){
          	JsonObject field_def = new JsonObject();
  		      field_def.add("id", new JsonPrimitive(rs.getString("column_name")));
  		      field_def.add("name", new JsonPrimitive(rs.getString("column_name")));
  		      field_def.add("type", new JsonPrimitive(rs.getString("data_type")));
  		      field_def.add("length", new JsonPrimitive(Integer.toString(rs.getInt("character_maximum_length"))));
  		      fields.add(field_def);
            }
		   }
		  } catch (SQLException e) {
			  System.out.println(e.toString());
		  } catch (Exception e) {
			  System.out.println(e.toString());
		  }
           return fields;  
	   }
	  

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	Connection connection = null;
	JsonArray fields = new JsonArray();
	Boolean status = false;
	Boolean isValidSupported = false;
	
	public HistoricEventsConnection (EventbotProps eventbotprops)   {
		this.connection =null;
		this.status = false;
		this.fields = new JsonArray();
		this.isValidSupported = false;
	   	   
		try {
		   Class.forName("com.ibm.db2.jcc.DB2Driver");

           this.connection =  DriverManager.getConnection (eventbotprops.getHistoryurl(), 
           		eventbotprops.getHistoryuser(), eventbotprops.getHistorypassword());
           if (methodExists(this.connection.getClass(), "isValid")) {
        	   this.isValidSupported = true;
           }
           if (this.isValidSupported) {
        	   if (this.connection.isValid(200)) {
        	   this.fields = fetchFields(this.connection);
              }
		   } else {
			   //cant check if valid owing to class-support but try anyway
	           this.fields = fetchFields(this.connection);
		   }
    	   if (this.fields.size() > 0) { this.status = true; }
    	   else { this.status = false;}
		}
		catch (SQLException e ) {
			System.out.println(e.toString());
		}
        catch (ClassNotFoundException e ) {
        	System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		catch (Exception e ) {
			System.out.println("Exception caught");
			System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		
	}
}
