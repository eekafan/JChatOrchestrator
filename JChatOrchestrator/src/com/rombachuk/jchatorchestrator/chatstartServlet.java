package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cloudant.client.api.Database;


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

    private String getWorkspaceId(List<Workspace> list, String name) {
    	int index = 0;
    	String id = null;
    	Boolean found = false;
    	while (!found && index < list.size()) {
    		if (list.get(index).getName().equals(name)) {
    			found = true;
    			id = list.get(index).getId();
    		}
    		else
    		{
    			index=index+1;
    		}
    	}
    	return id;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 try {

	    response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		HttpSession session = request.getSession(true); // new session if not exist
		
		session.setAttribute("name", request.getParameter("name"));
        JcoWorkspaces jcoworkspaces = new JcoWorkspaces( request.getServletContext().getRealPath("/")+
	            request.getServletContext().getInitParameter("jcoWorkspaces")); 
        session.setAttribute("workspaceid",getWorkspaceId(jcoworkspaces.getList(),request.getParameter("name")));
        
        WatsonConnection watsonconnection = new WatsonConnection(
        		new JcoProps(request.getServletContext().getRealPath("/")+
				request.getServletContext().getInitParameter("jcoProperties")));
	    session.setAttribute("watsonconnection", watsonconnection);

		response.sendRedirect("chat.html");
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
