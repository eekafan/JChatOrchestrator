package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.derby.client.am.Clob;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;


public class HistoricEventsDAO {
	 static Logger logger = Logger.getLogger(ChatFilter.class);

	public static JsonArray fetchMatchingEvents(Integer maxrows, String sqlfilter, ServletContext context) {
		
     JsonArray events = new JsonArray();	
     try { 
         HistoricEventsConnection historyconn = (HistoricEventsConnection) context.getAttribute("eventbothistoryconnection");
  
		 if (!historyconn.status) {
		   // retry		
		   EventbotProps eventbotprops = (EventbotProps) context.getAttribute("eventbotprops");
           historyconn = new HistoricEventsConnection (eventbotprops);
           context.setAttribute("eventbothistoryconnection", historyconn);     
		 } 

		// recheck status and validity 
		 if ((historyconn.status) && 
				 historyconn.connection.isValid(200)) {
		  String fetchQuery = "select * " + "from REPORTER_STATUS" +
		" where " + sqlfilter +
					" fetch first "+ maxrows.toString() + " rows only";
		  logger.debug("HistoricEventsDAO : " + fetchQuery);


		
         Statement statement = historyconn.connection.createStatement();  
         ResultSet rs = statement.executeQuery(fetchQuery);
         while(rs.next()){
        	JsonObject event = new JsonObject();
        	for (JsonElement field:  historyconn.fields) {
        	 event.addProperty("rowindex",Integer.toString(rs.getRow()));
             String name = field.getAsJsonObject().get("name").getAsString();
             String type = field.getAsJsonObject().get("type").getAsString();
        	 switch (type) {
        	 case "CHARACTER VARYING" :
        		 if (rs.getString(name) == null) {
        			 event.addProperty(name,""); 
        		 } else {
        		 event.addProperty(name, rs.getString(name));
        		 }
        		 break;
           	 case "INTEGER" :
           		event.addProperty(name, Integer.toString(rs.getInt(name)));
        		 break;
           	 case "TIMESTAMP" :
           		if (rs.getTimestamp(name) == null) {
           			event.addProperty(name,"");
           		} else {
            		event.addProperty(name, rs.getTimestamp(name).toString());
           		}
 
        		 break;
        	 default:
        	 }
        	}
        	events.add(event);   
         }
	   }         
	   return events;
     }
     catch (SQLException e) {
    	 System.out.println("HistoricEventsDAO fetchMatchingEvents: JDBC SQL Error: "+e.getMessage());
    	 events = new JsonArray();
    	 return events;	 
     }
     catch (Exception e) {
       	 System.out.println("HistoricEventsDAO fetchMatchingEvents: Other Error: "+e.getCause());
       	 System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
       	 events = new JsonArray();
    	 return events;	 
     }
	}
	

}
