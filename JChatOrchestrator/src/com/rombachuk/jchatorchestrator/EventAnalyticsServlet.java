package com.rombachuk.jchatorchestrator;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;

import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;



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
	    		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 JsonObject botException = new JsonObject();
		 try {
			    // ChatFilter provides validated set of attributes for use by chat servlet
			    // chatuuid_lastreply stores last reply from assistant for this chatclient
			    String chatname = (String) request.getAttribute("chatname"); //added by filter
			 	JsonObject chatclientInput = (JsonObject) request.getAttribute("chatclientinput"); //added by filter
			 	String workspaceid = (String) request.getAttribute("workspaceid"); //added by filter
			 	WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
			    String chatuuid_lastreply = request.getParameter("uuid")+"lastreply";
			    MessageResponse lastReply = (MessageResponse) request.getSession().getAttribute(chatuuid_lastreply);
			    Context latestContext = (Context) request.getAttribute("latestcontext");
			    MessageOptions options = new MessageOptions.Builder(workspaceid).build();
			    
			    // prepare options to send
			    InputData input = new InputData.Builder(chatclientInput.get("input").getAsString()).build();
				if ((lastReply == null) || (lastReply.entrySet().size() == 0)) {
						   options= new MessageOptions.Builder(workspaceid)
								    .input(input)
								    .build();
				} else {
				  	    options = new MessageOptions.Builder(workspaceid)
					    .input(input)
					    //.intents(lastReply.getIntents())
					    //.entities(lastReply.getEntities())
					    .context(latestContext)
					    //.output(lastReply.getOutput())
					    .build();
			    }
					
				// send request to watson assistant
				MessageResponse botReply = watsonconnection.synchronousRequest(options);

				// process reply from watson assistant
				Context context = botReply.getContext();
				JsonObject appData = new JsonObject();


				if ((context.containsKey("activity") == true ) && (context.containsKey("operation") == true )){
					
					// collectparameter operations
					if (context.get("operation").toString().equals("collectparameters") && 
							!context.get("operationstatus").toString().equals("complete")) {
						
						if (context.get("activity").toString().equals("searchseasonalevents") ||
						 context.get("activity").toString().equals("searchrelatedevents") ||
						 context.get("activity").toString().equals("searchhistoricevents")) {
						 JsonArray historyfields = (JsonArray) request.getSession().getServletContext().getAttribute("eventbothistoryfields");
						 appData.add("filter_fields",historyfields);
					    }

					    if (context.get("activity").toString().equals("searchcurrentevents")) {
						 JsonArray currentfields = (JsonArray) request.getSession().getServletContext().getAttribute("eventbotobjectserverfields");
						 appData.add("filter_fields",currentfields);
					    }
					}
					
					// showresults (run report) operations
					if (context.get("operation").toString().equals("showresults") && 
							!context.get("operationstatus").toString().equals("complete")) {
						
					    if (context.get("activity").toString().equals("searchseasonalevents")) {
						    JsonElement resultType = new JsonParser().parse("seasonal");
						    JsonArray resultRows = new JsonArray();
						    resultRows.add(new JsonParser().parse("{\"name\":\"aname\",\"field1\":\"345\"}"));
							appData.add("result_type",resultType);
							appData.add("result_rows",resultRows);
							context.put("operationstatus", "complete");	
							botReply.put("context", context);
						}
					    
					    if (context.get("activity").toString().equals("searchrelatedevents")) {
						    JsonElement resultType = new JsonParser().parse("related");
						    JsonArray resultRows = new JsonArray();
						    resultRows.add(new JsonParser().parse("{\"name\":\"bname\",\"field2\":\"567\"}"));
							appData.add("result_type",resultType);
							appData.add("result_rows",resultRows);
							context.put("operationstatus", "complete");
							botReply.put("context", context);
							
						}
				    }
				}
				
				// send app specific response to chatclient via ChatFilter
				request.setAttribute("appdata", appData);
				// send bot response to chatclient via ChatFilter
				request.setAttribute("botreply", botReply);
			 }
		 
		     catch( UnauthorizedException e) {
		    	 botException.addProperty("error","Assistant Access Authorisation problem"); 
		    	 request.setAttribute("botexception",botException);
		     }
	         catch (Exception e) {
		    	 botException.addProperty("error","Assistant Request error"); 
		    	 request.setAttribute("botexception",botException);
		     }

	}

}
