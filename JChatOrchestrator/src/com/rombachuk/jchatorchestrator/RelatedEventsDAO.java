package com.rombachuk.jchatorchestrator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class RelatedEventsDAO {
	
	public static JsonArray fetchGroupsTopN(Integer topn, ServletContext context) {
		
     try {
		Connection impactconn = (Connection) context.getAttribute("eventbotimpactconnection");
		String impactconntype = (String) context.getAttribute("eventbotimpactconntype");
		JsonArray re_groups = new JsonArray();
		
		String fetchQuery = "";
		
		if (impactconntype.equals("derby")) {
			fetchQuery = "select groupname,instances,totalevents,uniqueevents,type,instances*totalevents as size from RELATEDEVENTS.RE_GROUPS " +
		"group by groupname,instances,totalevents,uniqueevents,type,instances*totalevents order by instances*totalevents desc " +
					"fetch first "+ topn.toString() + "rows only";
		}
		
		if (impactconntype.equals("db2")) {	
		}
		
        Statement statement = impactconn.createStatement();  
        ResultSet rs = statement.executeQuery(fetchQuery);
        while(rs.next()){
        	JsonObject re_group = new JsonObject();
        	re_group.add("groupname", new JsonPrimitive(rs.getString("groupname")));
        	re_group.add("size", new JsonPrimitive(rs.getString("size")));
        	re_group.add("type", new JsonPrimitive(rs.getString("type")));
        	re_group.add("total_events", new JsonPrimitive(Integer.toString(rs.getInt("totalevents"))));
        	re_group.add("unique_events", new JsonPrimitive(Integer.toString(rs.getInt("uniqueevents"))));
        	re_group.add("instances", new JsonPrimitive(Integer.toString(rs.getInt("instances"))));
        	re_groups.add(re_group);   
		}
		
        return re_groups;
     }
     catch (SQLException e) {
    	 System.out.println("RelatedEventsDAO : JDBC SQL Error: "+e.getMessage());
    	 JsonArray re_groups = new JsonArray();
    	 return re_groups;	 
     }
     catch (Exception e) {
       	 System.out.println("RelatedEventsDAO : Other Error: "+e.getMessage());
    	 JsonArray re_groups = new JsonArray();
    	 return re_groups;	 
     }
	}

}
