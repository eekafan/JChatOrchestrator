package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.ListEntitiesOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeEntity;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeIntent;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
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
		// TODO Auto-generated method stub
	    PrintWriter out = response.getWriter();
	    JsonObject botError = new JsonObject();
		 try {
			    //ChatFilter will only call servlet if :
			    // chatrequest.input present, workspaceid not null, watsonconnection ok
			    String chatname = (String) request.getAttribute("chatname"); //added by filter
			 	JsonObject chatRequest = (JsonObject) request.getAttribute("chatrequest"); //added by filter
			 	String workspaceid = (String) request.getAttribute("workspaceid"); //added by filter
			 	WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
					
			    InputData input = new InputData.Builder(chatRequest.get("input").getAsString()).build();
					  
			    String chatuuid_lastreply = request.getParameter("uuid")+"lastreply";
			    MessageResponse lastReply = (MessageResponse) request.getSession().getAttribute(chatuuid_lastreply);
						  
				MessageOptions options = new MessageOptions.Builder(workspaceid).build();
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
					   
				MessageResponse botReply = watsonconnection.synchronousRequest(options);
			    request.getSession().setAttribute(chatuuid_lastreply, botReply);
					
	
				if (!botReply.toString().isEmpty()) { 
				    	out.write(botReply.toString());
				} 
				else {
					    	botError.addProperty("error","response problem");  
					    	out.write(botError.toString());
				}

				out.close(); 
			 }
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.getResponse());
		    	 botError.addProperty("error","input problem");  
			      out.write(botError.toString());
			      out.close(); 
		     }
	         catch (Exception e) {
	        	 System.out.println(e.getMessage());
	        	 botError.addProperty("error","input problem");  
			      out.write(botError.toString());
			      out.close(); 
		     }

	}

}
