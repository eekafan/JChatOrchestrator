package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        	
         JcoProps ldapconfig = new JcoProps( request.getServletContext().getRealPath("/")+
        		            request.getServletContext().getInitParameter("jcoProperties"));    
         User       user = new User(ldapconfig,request.getParameter("username"));

         if (user.getDn().equals("notfound")) {
        		destination = "relogin.html";
         } 
         else {
        	 	Boolean testCredentials = User.authenticate(ldapconfig, user.getDn(), request.getParameter("userpass"));
                if (testCredentials == true) {
                	destination = "success.html";
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
