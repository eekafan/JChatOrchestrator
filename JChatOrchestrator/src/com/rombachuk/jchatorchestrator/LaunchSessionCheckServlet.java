package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;


/**
 * Servlet implementation class LaunchSessionCheckServlet
 */

public class LaunchSessionCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LaunchSessionCheckServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	     PrintWriter out = response.getWriter();
	     JsonObject sessioncheck = new JsonObject();

		 try {
			 if (request.getRequestedSessionId() == null) {		    	
				 sessioncheck.addProperty("error","no session");
		       }
			 else {  
				if (request.getRequestedSessionId() != null
				        && !request.isRequestedSessionIdValid()) {
				    // Session is expired so flag error as reply to enable client to respond
			    	sessioncheck.addProperty("error","session invalid");  
			    }
				else {			    	
					sessioncheck.addProperty("success","session valid");  
				}		 	  
	         }
		     out.write(sessioncheck.toString());
			 out.close(); 
            }
         catch (Exception e) {
        	 System.out.println(e.getMessage());
        	 sessioncheck.addProperty("error","session check problem");  
		      out.write(sessioncheck.toString());
		      out.close(); 
	     }
	
    }
	
}
