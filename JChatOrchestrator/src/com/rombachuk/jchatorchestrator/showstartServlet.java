package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class showstartServlet
 */

public class showstartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public showstartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try {

			    response.setContentType("text/html");  
				PrintWriter out = response.getWriter();  
				HttpSession session = request.getSession(true); // new session if not exist

				String target = request.getParameter("name");
				if ((target != null) && (target.length()>0)) {
		          String jsp = request.getParameter("name")+".jsp";
		          request.setAttribute("name", request.getParameter("name"));
		          RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(jsp);
		          dispatcher.forward(request, response);
			      out.close();
				}
			 }
		     catch( IOException e) {
		    	 System.out.println(e.getMessage());
		     }
		     catch( Exception e) {
		      	System.out.println(e.getMessage());
		     }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
