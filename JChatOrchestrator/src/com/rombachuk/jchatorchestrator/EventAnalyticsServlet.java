package com.rombachuk.jchatorchestrator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.cloud.sdk.core.service.exception.UnauthorizedException;
import com.ibm.watson.assistant.v2.model.MessageContext;
import com.ibm.watson.assistant.v2.model.MessageContextSkills;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageInputOptions;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;




/**
 * Servlet implementation class chatServlet
 */

public class EventAnalyticsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventAnalyticsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
  
 	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("relatedevents.jsp");
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 JsonObject botException = new JsonObject();
		 try {
	
			    MessageResponse botReply = (MessageResponse) request.getAttribute("botreply");

				// appData is private to the app and the client, assistant does not see it.
				
				JsonObject appData = new JsonObject();
				JsonObject chatclientAppInput = (JsonObject) request.getAttribute("chatclientappinput");
			    if ((chatclientAppInput != null) && (chatclientAppInput.has("parameters"))) {
			    	appData.add("parameters", chatclientAppInput.getAsJsonArray("parameters"));
			    }

			    // context is shared with assistant. 
			    // context holds activity,operation,operationstatus which are set by client+bot
			    // app responds to the operation+status and uses its private appdata 
			    try {
			    	MessageContextSkills contextskills = botReply.getContext().getSkills();
			    	if (contextskills.containsKey("main skill") == true) {
			    	 Map<String,Object> mainskill = (Map<String, Object>) contextskills.get("main skill");
			    	 if (mainskill.containsKey("user_defined") == true) {
			    		 Map<String,Object> userdefined = (Map<String, Object>) mainskill.get("user_defined");
				     if ((userdefined.containsKey("activity") == true ) && 
				    		(userdefined.containsKey("operation") == true )){
					
					// collectparameter operations 
					 if (userdefined.get("operation").toString().equals("collectparameters") && 
							!userdefined.get("operationstatus").toString().equals("complete")) {
						
						if (userdefined.get("activity").toString().equals("searchseasonalevents") ||
								userdefined.get("activity").toString().equals("searchrelatedevents")) {
					     // appdata activity
					    }
						
						if (userdefined.get("activity").toString().equals("searchhistoricevents")) {
					     // appdata activity
						 HistoricEventsConnection historyconn = (HistoricEventsConnection) request.getSession().getServletContext().getAttribute("eventbothistoryconnection");
						 appData.add("filter_fields",historyconn.fields);
					    }

					    if (userdefined.get("activity").toString().equals("searchcurrentevents")) {
					     // appdata activity
						 JsonArray currentfields = (JsonArray) request.getSession().getServletContext().getAttribute("eventbotobjectserverfields");
						 appData.add("filter_fields",currentfields);
					    }
					  }				
				     }
			    	 }
			    	}
			     } catch (Exception e) {	
			     }

				// send app specific response to chatclient via ChatFilter
				request.setAttribute("appdata", appData);
				// send bot response to chatclient via ChatFilter
				request.setAttribute("botreply", botReply);
			 }
		 
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.toString());
		    	 botException.addProperty("error","Assistant Access Authorisation problem"); 
		    	 request.setAttribute("botexception",botException);
		     }
	         catch (Exception e) {
	        	 System.out.println(e.toString());
		    	 botException.addProperty("error","Assistant Request error"); 
		    	 
		    	 request.setAttribute("botexception",botException);
		     }

	}

}
