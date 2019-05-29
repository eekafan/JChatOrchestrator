package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Servlet implementation class EventAnalyticsShowResultsServlet
 */

public class HistoricEventsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoricEventsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 JsonObject showException = new JsonObject(); 
		 JsonArray resultRows = new JsonArray();
		 try {

                String groupname = request.getParameter("groupname");
                String index = request.getParameter("observationindex");
  
                if ((groupname != null) && (index == null)){
   	             resultRows = RelatedEventsDAO.fetchGroupMembers(groupname, request.getServletContext());
  
                }
                
                if ((groupname != null) && (index != null)){
       	         resultRows = RelatedEventsDAO.fetchInstanceEvents(groupname, index, request.getServletContext());
                }
                
	           if (resultRows.size() == 0) {
	   	         ImpactConnection impactconn = (ImpactConnection) request.getServletContext().getAttribute("eventbotimpactconnection");
	   	         if (!impactconn.status) {
	   	            		showException = (JsonObject) new JsonParser().parse("{error:\"Data Source Connection Error\"}");
	   	            		request.setAttribute("showexception", showException);
	   	         }
	   	       }
			   JsonObject appData = new JsonObject();
			   appData.add("result_rows",resultRows);
			   request.setAttribute("appdata", appData);
		  
			 }
		     catch( Exception e) {
		      	System.out.println(e.getMessage());
		      	showException = (JsonObject) new JsonParser().parse("{error:\"Other Error\"}");
           		request.setAttribute("showexception", showException);
		     }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 JsonObject showException = new JsonObject(); 
		try {

			 	JsonObject showclientAppInput = (JsonObject) request.getAttribute("showclientappinput"); //added by filter

			 	JsonElement resultType = new JsonParser().parse("historic-events-matchingfilter");
				
			    String sqlfilter = "Severity < 0"; // default in case parameter not present
			    if (showclientAppInput.has("parameters")){  
                  for (JsonElement parameter : showclientAppInput.getAsJsonArray("parameters")) {
               	   if (parameter.getAsJsonObject().has("searchfilter")) {
               		 for (JsonElement component : parameter.getAsJsonObject().getAsJsonArray("searchfilter")) {
               	     if (component.getAsJsonObject().get("logic").getAsString().equals("none")) {
               		 sqlfilter = component.getAsJsonObject().get("filter").getAsJsonObject().get("field").getAsString() + " "  +
               				component.getAsJsonObject().get("filter").getAsJsonObject().get("operator").getAsString() + " " +
               				component.getAsJsonObject().get("filter").getAsJsonObject().get("value").getAsString();
               				 
               	     } else {
               	    	 sqlfilter = sqlfilter + " " + component.getAsJsonObject().get("logic").getAsString() + " " +
               	    			component.getAsJsonObject().get("filter").getAsJsonObject().get("field").getAsString() + " "  +
                   				component.getAsJsonObject().get("filter").getAsJsonObject().get("operator").getAsString() + " " +
                   				component.getAsJsonObject().get("filter").getAsJsonObject().get("value").getAsString();
               	     }
                   }
                  }
			     }
			    }
			    JsonArray resultRows = HistoricEventsDAO.fetchMatchingEvents(100, sqlfilter, request.getServletContext());

		        if (resultRows.size() == 0) {
		        	HistoricEventsConnection historyconn = (HistoricEventsConnection) request.getServletContext().getAttribute("eventbothistoryconnection");
			   	    if (!historyconn.status) {
			   	    	showException = (JsonObject) new JsonParser().parse("{error:\"Data Source Connection Error\"}");
			   	    	request.setAttribute("showexception", showException);
			   	   }
			   	}
			    JsonObject appData = new JsonObject();
			    appData.add("result_type",resultType);
				appData.add("result_rows",resultRows);
				request.setAttribute("appdata", appData);
		  
			 }
		     catch( Exception e) {
		      	System.out.println(e.getMessage());
		      	showException = (JsonObject) new JsonParser().parse("{error:\"Other Error\"}");
           		request.setAttribute("showexception", showException);
		     }
	}

}
