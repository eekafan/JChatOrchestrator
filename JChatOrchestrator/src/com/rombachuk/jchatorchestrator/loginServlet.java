package com.rombachuk.jchatorchestrator;

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
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;


/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public loginServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");  
	    PrintWriter out = response.getWriter();  
	          
        String destination = "relogin.html"; 
        
        try {
        	
         JcoProps Jcoprops = new JcoProps( request.getServletContext().getRealPath("/")+
        		            request.getServletContext().getInitParameter("jcoProperties"));    
         User       user = new User(Jcoprops,request.getParameter("username"));

         if (user.getDn().equals("notfound")) {
        		destination = "relogin.html";
         } 
         else {
        	 	Boolean testCredentials = User.authenticate(Jcoprops, user.getDn(), request.getParameter("userpass"));
                if (testCredentials == true) {
                	CloudantConnection cloudantconn  = new CloudantConnection(Jcoprops);
                	Database jco_log = cloudantconn.client.database("jco_log", false);
                	InputStream test = jco_log.find("Test");
                	destination = "chat.html";
                }
                else {
                	destination = "relogin.html";
                }
         }
        }
        catch (LDAPSearchException e) {
        	
        }
        catch (LDAPException e) {
        	
        }
        
 
     	if (destination.equals("relogin.html")){ 
       			HttpSession session = request.getSession();
       			session.invalidate();
       	}
       	response.sendRedirect(destination);
	    out.close();  
    }
}
