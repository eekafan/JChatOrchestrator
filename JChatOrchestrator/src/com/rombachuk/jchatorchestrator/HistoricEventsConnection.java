package com.rombachuk.jchatorchestrator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class HistoricEventsConnection {

	Connection connection = null;
	JsonArray fields = new JsonArray();
	Boolean status = false;
	
	public HistoricEventsConnection (EventbotProps eventbotprops)   {
		this.status = false;
		this.fields = new JsonArray();
	   	   
		try {
		   Class.forName("com.ibm.db2.jcc.DB2Driver");

           this.connection =  DriverManager.getConnection (eventbotprops.getHistoryurl(), 
           		eventbotprops.getHistoryuser(), eventbotprops.getHistorypassword());
           if (this.connection.isValid(200)) {
        	   this.status = true;
               Statement statement = this.connection.createStatement();
               ResultSet rs = statement.executeQuery("select column_name,data_type,character_maximum_length from sysibm.columns where table_name='REPORTER_STATUS'");
               while(rs.next()){
               	JsonObject field_def = new JsonObject();
       		      field_def.add("id", new JsonPrimitive(rs.getString("column_name")));
       		      field_def.add("name", new JsonPrimitive(rs.getString("column_name")));
       		      field_def.add("type", new JsonPrimitive(rs.getString("data_type")));
       		      field_def.add("length", new JsonPrimitive(Integer.toString(rs.getInt("character_maximum_length"))));
       		      this.fields.add(field_def);
                }
           }
		}
		catch (SQLException e ) {
			System.out.println(e.toString());
		}
        catch (ClassNotFoundException e ) {
			
		}
	}
}
