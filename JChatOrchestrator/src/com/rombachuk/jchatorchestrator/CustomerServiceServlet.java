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
import com.ibm.watson.assistant.v2.model.MessageInputOptions;
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
	
				MessageResponse botReply = (MessageResponse) request.getAttribute("botreply");

				// appData is private to the app and the client, assistant does not see it.
				
				JsonObject appData = new JsonObject();
				JsonObject chatclientAppInput = (JsonObject) request.getAttribute("chatclientappinput");
			    if (chatclientAppInput.has("parameters")) {
			    	appData.add("parameters", chatclientAppInput.getAsJsonArray("parameters"));
			    }

				request.setAttribute("appdata", appData);

			 }
		 
		     catch( UnauthorizedException e) {
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
