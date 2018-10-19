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

import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

/**
 * Servlet implementation class EventAnalyticsShowResultsServlet
 */

public class EventAnalyticsShowResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventAnalyticsShowResultsServlet() {
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

			    request.setAttribute("workspacename", request.getParameter("name"));

			    
			    if (request.getParameter("type").equals("relatedevents")) {
		         RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("relatedevents.jsp");
		         dispatcher.forward(request, response);
			    }
			    
			    if (request.getParameter("type").equals("seasonalevents")) {
			         RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("seasonalevents.jsp");
			         dispatcher.forward(request, response);
				    }
		        
			    out.close();
		  
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
