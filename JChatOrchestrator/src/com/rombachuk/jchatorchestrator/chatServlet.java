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
				JsonObject botResponse = new JsonObject();
				WatsonConnection watsonconnection = null;
		        
			    if (session.isNew()) {
			    	botResponse.addProperty("error","session invalid");  
			    	out.write(botResponse.toString());
				    out.close(); 
			    }
				else {	
					String propsfile = request.getServletContext().getRealPath("/")+
							request.getServletContext().getInitParameter("jcoProperties");
					if (request.getSession().getAttribute("watsonconnection") != null) {
						 watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
					} else {
						
					     watsonconnection = new WatsonConnection(new JcoProps(propsfile));
					     request.getSession().setAttribute("watsonconnection", watsonconnection);
					}
					
					botResponse.addProperty("botreply",BufferToString(request.getReader()));
					
				    if (!botResponse.toString().isEmpty()) { 
				    	out.write(botResponse.toString());
				     } 
				    out.close(); 
				}
			 }
		     catch( IOException e) {
		     	
		     }
	}

}
