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
			System.out.println(e.toString());
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
		
     JsonArray groups = new JsonArray();	
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
			fetchQuery = "select groupname,type,instances,totalevents,uniqueevents," +
		"lastfired,timesfired,timesfiredmonthly from RELATEDEVENTS.RE_GROUPS" +
		" where lastfired > '"+ minLastFired+"' or lastfired is null"+
		" order by totalevents desc " +
					"fetch first "+ topn.toString() + "rows only";
		 }		
		 if (impactconn.type.equals("db2")) {	
		 }
		
         Statement statement = impactconn.connection.createStatement();  
         ResultSet rs = statement.executeQuery(fetchQuery);
         while(rs.next()){
        	JsonObject group = new JsonObject();
        	group.add("groupname", new JsonPrimitive(rs.getString("groupname")));
        	group.add("type", new JsonPrimitive(rs.getString("type")));
        	group.add("instances", new JsonPrimitive(Integer.toString(rs.getInt("instances"))));
        	group.add("total_events", new JsonPrimitive(Integer.toString(rs.getInt("totalevents"))));
        	group.add("unique_events", new JsonPrimitive(Integer.toString(rs.getInt("uniqueevents"))));
        	if (rs.getTimestamp("lastfired") == null) {
        		group.add("lastfired", new JsonPrimitive("new"));
        	} else {
          	group.add("lastfired", new JsonPrimitive(rs.getTimestamp("lastfired").toString()));
        	}
           	group.add("timesfired", new JsonPrimitive(Integer.toString(rs.getInt("timesfired"))));
          	group.add("timesfiredmonthly", new JsonPrimitive(Integer.toString(rs.getInt("timesfiredmonthly"))));

        	groups.add(group);   
		 }         
		}
		 return groups;
     }
     catch (SQLException e) {
    	 System.out.println("RelatedEventsDAO fetchGroupsTopN: JDBC SQL Error: "+e.getMessage());
    	 groups = new JsonArray();
    	 return groups;	 
     }
     catch (Exception e) {
       	 System.out.println("RelatedEventsDAO fetchGroupsTopN: Other Error: "+e.getCause());
       	 System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
       	 groups = new JsonArray();
    	 return groups;	 
     }
	}
	
	public static JsonArray fetchGroupMembers(String groupname, ServletContext context) {
		
		 JsonArray groupMembers = new JsonArray();
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
				fetchQuery = "select groupasc from RELATEDEVENTS.RE_GROUPASC" +
			                 " where groupname = '"+ groupname+"'";
			
	            Statement statement = impactconn.connection.createStatement();  
	            ResultSet rs = statement.executeQuery(fetchQuery);
	            while(rs.next()){
	        	Clob clob = (Clob) rs.getClob(1);
	            JsonObject groupasc = clobToJson(clob);
	            if (groupasc.has("group")) {
	            	JsonObject group = groupasc.getAsJsonObject("group");
	            	if (group.has("groupMembers")) {
	            	return group.getAsJsonArray("groupMembers");
	                }
			    }
			    }
		      }
	        
			  if (impactconn.type.equals("db2")) {	
			  }
			}
			return groupMembers;
	     }
	     catch (SQLException e) {
	    	 System.out.println("RelatedEventsDAO fetchGroupMembers: JDBC SQL Error: "+e.toString()); 
	    	 groupMembers = new JsonArray();
	    	 return groupMembers;	 
	     }
	     catch (Exception e) {
	       	 System.out.println("RelatedEventsDAO fetchGroupMembers: Other Error: "+e.getMessage());
	         System.out.println(e.getStackTrace());
	       	 groupMembers = new JsonArray();
	    	 return groupMembers; 
	     }
		}

	public static JsonArray fetchInstanceEvents(String groupname, String index, ServletContext context) {
		
		 JsonArray events = new JsonArray();
		 Integer oindex = Integer.parseInt(index);
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
				fetchQuery = "select groupasc from RELATEDEVENTS.RE_GROUPASC" +
			                 " where groupname = '"+ groupname+"'";
			
	          Statement statement = impactconn.connection.createStatement();  
	          ResultSet rs = statement.executeQuery(fetchQuery);
	          while(rs.next()){
	        	Clob clob = (Clob) rs.getClob(1);
	            JsonObject groupasc = clobToJson(clob);
	            if (groupasc.has("group")) {
	            	JsonObject group = groupasc.getAsJsonObject("group");
	              if (group.has("groupMembers")) { 
	            	JsonArray members =  group.getAsJsonArray("groupMembers");
	            	for (JsonElement member : members) {
	            		JsonObject thismember = member.getAsJsonObject();
	            		JsonObject event = new JsonObject();
	            		Set<String> identifierset = thismember.getAsJsonObject("id").getAsJsonObject("attributes").keySet();
	            		JsonPrimitive identifier = new JsonPrimitive("");
	            		for (String key : identifierset) {
	            			identifier =  thismember.getAsJsonObject("id").getAsJsonObject("attributes").getAsJsonPrimitive(key);
	            		}
	            	    event.add("identifier",identifier);
	            		event.add("eventepoch",thismember.getAsJsonArray("observations").get(oindex));
	            		events.add(event);
	            	}
	              }
	            }
			  }
			}
	        
			  if (impactconn.type.equals("db2")) {	
			  }
			}
			return events;
	     }
	     catch (SQLException e) {
	    	 System.out.println("RelatedEventsDAO fetchInstanceEvents: JDBC SQL Error: "+e.toString());   	
	    	 events = new JsonArray();
	    	 return events;	 
	     }
	     catch (Exception e) {
	       	 System.out.println("RelatedEventsDAO fetchInstanceEvents: Other Error: "+e.getMessage());
	       	System.out.println(e.getStackTrace());
	       	events = new JsonArray();
	    	 return events; 
	     }
		}
}
