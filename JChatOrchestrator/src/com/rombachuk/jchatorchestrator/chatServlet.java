package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;


/**
 * Servlet implementation class chatServlet
 */
@WebServlet("/chat")
public class chatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public chatServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String getRequestBody (final HttpServletRequest request) 

    	     {
    	             
    	    HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
    	    StringBuilder stringBuilder = new StringBuilder();
    	    BufferedReader bufferedReader = null;
    	    try {
    	        InputStream inputStream = requestWrapper.getInputStream();
    	        if (inputStream != null) {
    	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    	            char[] charBuffer = new char[128];
    	            int bytesRead = -1;
    	            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
    	                stringBuilder.append(charBuffer, 0, bytesRead);
    	            }
    	        }
    	    } catch (IOException ex) {
    	        
    	    } finally {
    	        if (bufferedReader != null) {
    	            try {
    	                bufferedReader.close();
    	            } catch (IOException iox) {
    	                // ignore
    	            }
    	        }
    	    }
    	 
    	    return stringBuilder.toString();
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
		 try {

			    PrintWriter out = response.getWriter();			    
				HttpSession session = request.getSession(true);
				response.setContentType("application/json");  
				JsonObject botError = new JsonObject();
				WatsonConnection watsonconnection = null;
		        
			    if (session.isNew()) {
			    	botError.addProperty("error","session invalid");  
			    	out.write(botError.toString());
				    out.close(); 
			    }
				else {	
			          //J7 Servlet3 fix for read request once problem - do this first
			          InputData input = new InputData.Builder(getRequestBody(request)).build();
			          // J8 -> InputData input = new InputData.Builder(IOUtils.toString(req.getReader())).build();
					 
				      InputStream propsfile = request.getServletContext().getResourceAsStream(
				        		 request.getServletContext().getInitParameter("jcoProperties"));
				      JcoProps jcoprops = new JcoProps(propsfile);   
				      propsfile.close();
				      
				      InputStream workspacesfile = request.getServletContext().getResourceAsStream(
				          		 request.getServletContext().getInitParameter("jcoWorkspaces"));
				      JcoWorkspaces jcoworkspaces = new JcoWorkspaces(workspacesfile); 
				      workspacesfile.close(); 
				     
				      String workspaceid = jcoworkspaces.findId(request.getParameter("name"));
					
					
					  if (request.getSession().getAttribute("watsonconnection") != null) {
						 watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
					  } else {	
					     watsonconnection = new WatsonConnection(jcoprops);
					     request.getSession().setAttribute("watsonconnection", watsonconnection);
					  }


					  MessageOptions options = new MessageOptions.Builder(workspaceid)
					    .input(input)
					    .build();

					  MessageResponse botReply = watsonconnection.getAssistant().message(options).execute();

					
				      if (!botReply.toString().isEmpty()) { 
				    	out.write(botReply.toString());
				      } 
				      else {
					    	botError.addProperty("error","response problem");  
					    	out.write(botError.toString());
				      }
				     out.close(); 
					}
			 }
		     catch( IOException e) {
		     	
		     }
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.getResponse());
		     }
	}

}
