package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cloudant.client.api.Database;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;


/**
 * Servlet implementation class chatServlet
 */

public class chatstartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public chatstartServlet() {
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

        JcoWorkspaces jcoworkspaces = (JcoWorkspaces) session.getServletContext().getAttribute("jcoworkspaces");
        String workspaceid = jcoworkspaces.findId(request.getParameter("name"));
 
	    JcoProps jcoprops = (JcoProps) session.getServletContext().getAttribute("jcoprops");   
	    
	    WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
	    if (watsonconnection == null) {
		     watsonconnection = new WatsonConnection(jcoprops);
		     session.setAttribute("watsonconnection", watsonconnection);
	    }

         // use hello as default input to kick start a reply
		 MessageOptions options = new MessageOptions.Builder(workspaceid)
		    .input(new InputData.Builder("Hello").build())
		    .build();

		  MessageResponse botReply = watsonconnection.synchronousRequest(options);
		  
		  //session is shared by many chats, so use this uuid to store a specific session variable
		  //for recovery by this chat dialogue
		  String chatuuid_lastreply = request.getParameter("uuid")+"lastreply";
		  session.setAttribute(chatuuid_lastreply, botReply);

	    request.setAttribute("workspacename", request.getParameter("name"));
	    request.setAttribute("welcome", botReply.getOutput().getText().get(0));

        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("chat.jsp");
        dispatcher.forward(request, response);
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
