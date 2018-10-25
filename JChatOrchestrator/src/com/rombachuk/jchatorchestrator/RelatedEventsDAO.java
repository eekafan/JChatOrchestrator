package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.derby.client.am.Clob;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;


public class RelatedEventsDAO {
	
	  private static JsonObject clobToJson (Clob clob) 

	  {
		JsonObject object = new JsonObject();
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    InputStream inputStream;
		try {
			inputStream = clob.getAsciiStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	            object = (JsonObject) new JsonParser().parse(stringBuilder.toString());
	        }
	    } 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (IOException ex) {
	        
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException iox) {
	                // ignore
	            }
	        }
	    }	 
	    return object;
	}
	
	public static JsonArray fetchGroupsTopN(Integer topn, String minLastFired, ServletContext context) {
		
     try {
		Connection impactconn = (Connection) context.getAttribute("eventbotimpactconnection");
		String impactconntype = (String) context.getAttribute("eventbotimpactconntype");
		JsonArray groups = new JsonArray();
		
		String fetchQuery = "";
		
		if (impactconntype.equals("derby")) {
			fetchQuery = "select groupname,type,instances,totalevents,uniqueevents," +
		"lastfired,timesfired,timesfiredmonthly,"+
		"instances*uniqueevents as size from RELATEDEVENTS.RE_GROUPS" +
		" where lastfired > '"+ minLastFired+"'"+
		" order by totalevents desc " +
					"fetch first "+ topn.toString() + "rows only";
		}
		
		if (impactconntype.equals("db2")) {	
		}
		
        Statement statement = impactconn.createStatement();  
        ResultSet rs = statement.executeQuery(fetchQuery);
        while(rs.next()){
        	JsonObject group = new JsonObject();
        	group.add("groupname", new JsonPrimitive(rs.getString("groupname")));
        	group.add("type", new JsonPrimitive(rs.getString("type")));
        	group.add("instances", new JsonPrimitive(Integer.toString(rs.getInt("instances"))));
        	group.add("total_events", new JsonPrimitive(Integer.toString(rs.getInt("totalevents"))));
        	group.add("unique_events", new JsonPrimitive(Integer.toString(rs.getInt("uniqueevents"))));
          	group.add("lastfired", new JsonPrimitive(rs.getTimestamp("lastfired").toString()));
           	group.add("timesfired", new JsonPrimitive(Integer.toString(rs.getInt("timesfired"))));
          	group.add("timesfiredmonthly", new JsonPrimitive(Integer.toString(rs.getInt("timesfiredmonthly"))));
          	group.add("size", new JsonPrimitive(rs.getString("size")));

        	groups.add(group);   
		}
		
        return groups;
     }
     catch (SQLException e) {
    	 System.out.println("RelatedEventsDAO : JDBC SQL Error: "+e.getMessage());
    	 JsonArray groups = new JsonArray();
    	 return groups;	 
     }
     catch (Exception e) {
       	 System.out.println("RelatedEventsDAO : Other Error: "+e.getMessage());
    	 JsonArray groups = new JsonArray();
    	 return groups;	 
     }
	}
	
	public static JsonArray fetchGroupMembers(String groupname, ServletContext context) {
		
		 JsonArray groupMembers = new JsonArray();
	     try {
			Connection impactconn = (Connection) context.getAttribute("eventbotimpactconnection");
			String impactconntype = (String) context.getAttribute("eventbotimpactconntype");
			
			String fetchQuery = "";
			
			if (impactconntype.equals("derby")) {
				fetchQuery = "select groupasc from RELATEDEVENTS.RE_GROUPS" +
			                 " where groupname = '"+ groupname+"'";
			
	         Statement statement = impactconn.createStatement();  
	         ResultSet rs = statement.executeQuery(fetchQuery);
	         while(rs.next()){
	        	Clob clob = (Clob) rs.getClob(1);
	            JsonObject groupasc = clobToJson(clob);
	            if (groupasc.has("groupMembers")) {
	            	return groupasc.getAsJsonArray("groupMembers");
	            }
			 }
			}
	        
			if (impactconntype.equals("db2")) {	
			}
			return groupMembers;
	     }
	     catch (SQLException e) {
	    	 System.out.println("RelatedEventsDAO : JDBC SQL Error: "+e.getMessage());   	 
	    	 return groupMembers;	 
	     }
	     catch (Exception e) {
	       	 System.out.println("RelatedEventsDAO : Other Error: "+e.getMessage());
	    	 return groupMembers; 
	     }
		}

	public static JsonArray fetchInstanceEvents(String groupname, String index, ServletContext context) {
		
		 JsonArray events = new JsonArray();
		 Integer oindex = Integer.parseInt(index);
	     try {
			Connection impactconn = (Connection) context.getAttribute("eventbotimpactconnection");
			String impactconntype = (String) context.getAttribute("eventbotimpactconntype");
			
			String fetchQuery = "";
			
			if (impactconntype.equals("derby")) {
				fetchQuery = "select groupasc from RELATEDEVENTS.RE_GROUPS" +
			                 " where groupname = '"+ groupname+"'";
			
	         Statement statement = impactconn.createStatement();  
	         ResultSet rs = statement.executeQuery(fetchQuery);
	         while(rs.next()){
	        	Clob clob = (Clob) rs.getClob(1);
	            JsonObject groupasc = clobToJson(clob);
	            if (groupasc.has("groupMembers")) {
	            	JsonArray members =  groupasc.getAsJsonArray("groupMembers");
	            	for (JsonElement member : members) {
	            		JsonObject thismember = member.getAsJsonObject();
	            		JsonObject event = new JsonObject();
	            		event.add("identifier", thismember.getAsJsonObject("id").getAsJsonObject("attributes").get("IDENTIFIER"));
	            		event.add("eventepoch",thismember.getAsJsonArray("observations").get(oindex));
	            		events.add(event);
	            	}
	            }
			 }
			}
	        
			if (impactconntype.equals("db2")) {	
			}
			return events;
	     }
	     catch (SQLException e) {
	    	 System.out.println("RelatedEventsDAO : JDBC SQL Error: "+e.getMessage());   	 
	    	 return events;	 
	     }
	     catch (Exception e) {
	       	 System.out.println("RelatedEventsDAO : Other Error: "+e.getMessage());
	    	 return events; 
	     }
		}
}
