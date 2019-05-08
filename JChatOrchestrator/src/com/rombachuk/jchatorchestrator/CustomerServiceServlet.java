package com.rombachuk.jchatorchestrator;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.service.exception.UnauthorizedException;
import com.ibm.watson.assistant.v2.model.MessageContext;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;




/**
 * Servlet implementation class chatServlet
 */

public class CustomerServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerServiceServlet() {
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
			 	JsonObject chatclientAssistantInput = (JsonObject) request.getAttribute("chatclientassistantinput"); //added by filter
			 	JsonObject chatclientAppInput = (JsonObject) request.getAttribute("chatclientappinput"); //added by filter
			    MessageContext latestContext = (MessageContext) request.getAttribute("latestcontext");	
			 	WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
			    String chatuuid_lastreply = request.getParameter("chatid")+"lastreply";
			    MessageResponse lastReply = (MessageResponse) request.getSession().getAttribute(chatuuid_lastreply);

			    String dialogueassistantid = request.getParameter("name");
			     Map<String,String> dialoguesessions = watsonconnection.getSessions();
			     String dialoguesessionid = dialoguesessions.get(dialogueassistantid);
			    
			    //send message to watson assistant
			    MessageOptions options = new MessageOptions.Builder(dialogueassistantid,dialoguesessionid).build();
			    MessageInput input = new MessageInput.Builder()
			    		  .messageType("text")
			    		  .text(chatclientAssistantInput.get("input").getAsString())
			    		  .build();
			    
	
			    
				if ((lastReply == null) ) {
						   options= new MessageOptions.Builder(dialogueassistantid,dialoguesessionid)
								    .input(input)
								    .build();
				} else {
				  	    options = new MessageOptions.Builder(dialogueassistantid,dialoguesessionid)
					    .input(input)
					    //.intents(lastReply.getIntents())
					    //.entities(lastReply.getEntities())
					    //.context(latestContext)
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
