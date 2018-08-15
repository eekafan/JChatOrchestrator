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
@WebServlet("/chatstart")
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
		
       	InputStream workspacesfile = request.getServletContext().getResourceAsStream(
          		 request.getServletContext().getInitParameter("jcoWorkspaces"));
        JcoWorkspaces jcoworkspaces = new JcoWorkspaces(workspacesfile); 
        workspacesfile.close(); 
        String workspaceid = jcoworkspaces.findId(request.getParameter("name"));
        
	    InputStream propsfile = request.getServletContext().getResourceAsStream(
	        		 request.getServletContext().getInitParameter("jcoProperties"));
	    JcoProps jcoprops = new JcoProps(propsfile);   
	    propsfile.close();
        WatsonConnection watsonconnection = new WatsonConnection(jcoprops);
	    session.setAttribute("watsonconnection", watsonconnection);
	    
		  InputData input = new InputData.Builder("Hello").build();

		  MessageOptions options = new MessageOptions.Builder(workspaceid)
		    .input(input)
		    .build();

		  MessageResponse botReply = watsonconnection.getAssistant().message(options).execute();

	    request.setAttribute("workspacename", request.getParameter("name"));
	    request.setAttribute("welcome", botReply.getOutput().getText().get(0));
        RequestDispatcher dispatcher = request.getRequestDispatcher("chat.jsp");
        dispatcher.forward(request, response);
	    out.close();
  
	 }
     catch( IOException e) {
     	
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
