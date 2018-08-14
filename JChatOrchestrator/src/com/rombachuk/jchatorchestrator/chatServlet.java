package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    
    private static String BufferToString(BufferedReader body) throws IOException {
	    String line = body.readLine();
	    StringBuilder bodyString = new StringBuilder();

	    while( (line != null) && (!line.isEmpty()) ){
	        bodyString.append(line);
	        line = body.readLine();
	    }	    
	    return(bodyString.toString()); 
    }
    
 	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter out = response.getWriter();			
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
					String propsfile = request.getServletContext().getRealPath("/")+
							request.getServletContext().getInitParameter("jcoProperties");
					String workspacename = (String) request.getSession().getAttribute("name");
					String workspaceid = (String) request.getSession().getAttribute("workspaceid");
					if (workspacename != null) {
					  if (request.getSession().getAttribute("watsonconnection") != null) {
						 watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
					  } else {	
					     watsonconnection = new WatsonConnection(new JcoProps(propsfile));
					     request.getSession().setAttribute("watsonconnection", watsonconnection);
					  }
					  

					  InputData input = new InputData.Builder(BufferToString(request.getReader())).build();

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
					else { // workspace not present
				    	botError.addProperty("error","workspace not identified");  
				    	out.write(botError.toString());
					    out.close(); 						
					}
				}
			 }
		     catch( IOException e) {
		     	
		     }
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.getResponse());
		     }
	}

}
