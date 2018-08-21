package com.rombachuk.jchatorchestrator;


import java.io.IOException;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import com.google.gson.JsonObject;

import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;

import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;



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
			 	JsonObject chatclientInput = (JsonObject) request.getAttribute("chatclientinput"); //added by filter
			 	String workspaceid = (String) request.getAttribute("workspaceid"); //added by filter
			 	WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
			    String chatuuid_lastreply = request.getParameter("uuid")+"lastreply";
			    MessageResponse lastReply = (MessageResponse) request.getSession().getAttribute(chatuuid_lastreply);
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
					    //.intents(lastOptions.getIntents())
					    .entities(lastReply.getEntities())
					    .context(lastReply.getContext())
					    .output(lastReply.getOutput())
					    .build();
			    }
					
				// send request to watson assistant
				MessageResponse botReply = watsonconnection.synchronousRequest(options);
				
				// process reply from watson assistant
				
				// send response to chatclient via ChatFilter
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
