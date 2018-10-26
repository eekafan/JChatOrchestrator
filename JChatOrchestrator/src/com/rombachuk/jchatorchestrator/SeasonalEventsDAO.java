package com.rombachuk.jchatorchestrator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SeasonalEventsDAO {
	
	public static JsonArray fetchEntriesTopN(Integer topn, String startdate, String enddate, String filter, ServletContext context) {
		
	     JsonArray entries = new JsonArray();	
	     try { 
			 ImpactConnection impactconn = (ImpactConnection) context.getAttribute("eventbotimpactconnection");	
			 if (!impactconn.status) {
			   // retry		
			   EventbotProps eventbotprops = (EventbotProps) context.getAttribute("eventbotprops");
			   impactconn = new ImpactConnection(eventbotprops);	   
			 } 

			// recheck status and validity 
			 if ((impactconn.status) && 
		        impactconn.connection.isValid(200)) {
			  String fetchQuery = "";		
			  if (impactconn.type.equals("derby")) {
				fetchQuery = "select entry,idvalue,pvalue,p_value_day_of_month,p_value_day_of_week,p_value_minute,p_value_hour," +
			" from SEASONAL.RESULTS where resultid in (select resultid from SEASONAL.RESULTCONFIGURATION where " +
			" where resultid in select resultid from SEASONAL.RESULTCONFIGURATION where "+ 
			" result_start_time > '"+startdate+"' and result_end_time < '"+enddate+"')"+
			" order by pvalue desc " +
						"fetch first "+ topn.toString() + "rows only";
			 }		
			 if (impactconn.type.equals("db2")) {	
			 }
			
	         Statement statement = impactconn.connection.createStatement();  
	         ResultSet rs = statement.executeQuery(fetchQuery);
	         while(rs.next()){
	        	JsonObject entry = new JsonObject();
	        	entry.add("entry", new JsonPrimitive(rs.getString("entry")));
	        	entry.add("identifier", new JsonPrimitive(rs.getString("idvalue")));
	        	entry.add("prob_overall", new JsonPrimitive(Integer.toString(rs.getInt("pvalue"))));
	        	entry.add("prob_dom", new JsonPrimitive(Integer.toString(rs.getInt("p_value_day_of_month"))));
	        	entry.add("prob_dow", new JsonPrimitive(Integer.toString(rs.getInt("p_value_day_of_week"))));
	        	entry.add("prob_hour", new JsonPrimitive(rs.getTimestamp("p_value_hour").toString()));
	        	entry.add("prob_min", new JsonPrimitive(Integer.toString(rs.getInt("p_value_minute"))));

	        	entries.add(entry);   
			 }         
			}
			 return entries;
	     }
	     catch (SQLException e) {
	    	 System.out.println("SeasonalEventsDAO : JDBC SQL Error: "+e.getMessage());
	    	 entries = new JsonArray();
	    	 return entries;	 
	     }
	     catch (Exception e) {
	       	 System.out.println("SeasonalEventsDAO : Other Error: "+e.getMessage());
	       	 entries = new JsonArray();
	    	 return entries;	 
	     }
		}

}
