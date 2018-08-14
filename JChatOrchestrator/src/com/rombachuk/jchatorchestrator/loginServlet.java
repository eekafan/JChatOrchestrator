package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
@WebServlet("/login")
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
        
        try {
        	
         JcoProps jcoprops = new JcoProps( request.getServletContext().getRealPath("/")+
        		            request.getServletContext().getInitParameter("jcoProperties"));    
         User       user = new User(jcoprops,request.getParameter("username"));
 

         if (user.getDn().equals("notfound")) {
             	HttpSession session = request.getSession();
       			session.invalidate();
     		    response.sendRedirect("relogin.html");
    	        out.close(); 
         } 
         else {
        	 	Boolean testCredentials = User.authenticate(jcoprops, user.getDn(), request.getParameter("userpass"));
                if (testCredentials == true) {
                    JcoWorkspaces jcoworkspaces = new JcoWorkspaces( request.getServletContext().getRealPath("/")+
        		            request.getServletContext().getInitParameter("jcoWorkspaces")); 
                    List<Workspace> workspaces = jcoworkspaces.getList();
                    request.setAttribute("workspaces", workspaces);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("launcher.jsp");
                    dispatcher.forward(request, response);
                    out.close();              
                }
                else {
                 	HttpSession session = request.getSession();
           			session.invalidate();
         		    response.sendRedirect("relogin.html");
        	        out.close(); 
                }
         }
        }
        catch (LDAPSearchException e) {
           	HttpSession session = request.getSession();
       			session.invalidate();
     		    response.sendRedirect("relogin.html");
    	        out.close();
        }
        catch (LDAPException e) {
           	HttpSession session = request.getSession();
       			session.invalidate();
     		    response.sendRedirect("relogin.html");
    	        out.close();
        }
       	 
    }
}
