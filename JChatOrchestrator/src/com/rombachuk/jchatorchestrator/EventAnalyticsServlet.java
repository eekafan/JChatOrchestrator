package com.rombachuk.jchatorchestrator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

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
			    // get request variables - specific to this request
			    String chatname = (String) request.getAttribute("chatname"); //added by filter
			 	JsonObject chatclientAssistantInput = (JsonObject) request.getAttribute("chatclientassistantinput"); //added by filter
			 	JsonObject chatclientAppInput = (JsonObject) request.getAttribute("chatclientappinput"); //added by filter
			    Context latestContext = (Context) request.getAttribute("latestcontext");			    
			 	String workspaceid = (String) request.getAttribute("workspaceid"); //added by filter
			 	
			 	//get session variables - maintained over many requests in this session
			 	WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
			    String chatuuid_lastreply = request.getParameter("uuid")+"lastreply";
			    MessageResponse lastReply = (MessageResponse) request.getSession().getAttribute(chatuuid_lastreply);

			    
			    //send message to watson assistant
			    MessageOptions options = new MessageOptions.Builder(workspaceid).build();
			    
			    InputData input = new InputData.Builder(chatclientAssistantInput.get("input").getAsString()).build();
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
				MessageResponse botReply = watsonconnection.synchronousRequest(options);

				// process reply from watson assistant - 
				
				// appData is private to the app and the client, assistant does not see it.
				
				JsonObject appData = new JsonObject();
			    if (chatclientAppInput.has("parameters")) {
			    	appData.add("parameters", chatclientAppInput.getAsJsonArray("parameters"));
			    }

			    // context is shared with assistant. 
			    // context holds activity,operation,operationstatus which are set by client+bot
			    // app responds to the operation+status and uses its private appdata 

			    Context context = botReply.getContext();
				if ((context.containsKey("activity") == true ) && (context.containsKey("operation") == true )){
					
					// collectparameter operations 
					if (context.get("operation").toString().equals("collectparameters") && 
							!context.get("operationstatus").toString().equals("complete")) {
						
						if (context.get("activity").toString().equals("searchseasonalevents") ||
						 context.get("activity").toString().equals("searchrelatedevents") ||
						 context.get("activity").toString().equals("searchhistoricevents")) {
					     // appdata activity
						 JsonArray historyfields = (JsonArray) request.getSession().getServletContext().getAttribute("eventbothistoryfields");
						 appData.add("filter_fields",historyfields);
					    }

					    if (context.get("activity").toString().equals("searchcurrentevents")) {
					     // appdata activity
						 JsonArray currentfields = (JsonArray) request.getSession().getServletContext().getAttribute("eventbotobjectserverfields");
						 appData.add("filter_fields",currentfields);
					    }
					}
					
					// showresults (run report) operations
					if (context.get("operation").toString().equals("showresults") && 
							!context.get("operationstatus").toString().equals("complete")) {
						
					    if (context.get("activity").toString().equals("searchseasonalevents")) {
					    	// appdata activity
						    JsonElement resultType = new JsonParser().parse("seasonal");
						    JsonArray resultRows = new JsonArray();
						    resultRows.add(new JsonParser().parse("{\"name\":\"aname\",\"field1\":\"345\"}"));
							appData.add("result_type",resultType);
							appData.add("result_rows",resultRows);
							// assistantdata activity
							context.put("operationstatus", "complete");	
							botReply.put("context", context);
						}
					    
					    if (context.get("activity").toString().equals("searchrelatedevents")) {
					    	// appdata activity
						    JsonElement resultType = new JsonParser().parse("related-groups-topn-bysize");
						    
						    String minLastFired = "2000-01-01 00:00:00"; // default in case parameter not present
						    if (appData.has("parameters")){  
                               for (JsonElement parameter : appData.getAsJsonArray("parameters")) {
                            	 if (parameter.getAsJsonObject().has("startdate")) {
                            		 minLastFired = parameter.getAsJsonObject().get("startdate").getAsJsonObject().get("sql").getAsString();
                            	 }
                               }
						    }
						    JsonArray resultRows = RelatedEventsDAO.fetchGroupsTopN(10, minLastFired, request.getServletContext());
							appData.add("result_type",resultType);
							appData.add("result_rows",resultRows);
							// assistantdata activity
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
		    	 System.out.println(e.getMessage());
		    	 botException.addProperty("error","Assistant Access Authorisation problem"); 
		    	 request.setAttribute("botexception",botException);
		     }
	         catch (Exception e) {
	        	 System.out.println(e.getMessage());
		    	 botException.addProperty("error","Assistant Request error"); 
		    	 
		    	 request.setAttribute("botexception",botException);
		     }

	}

}
