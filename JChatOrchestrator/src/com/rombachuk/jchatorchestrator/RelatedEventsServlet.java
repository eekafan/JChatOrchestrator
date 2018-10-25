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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

/**
 * Servlet implementation class EventAnalyticsShowResultsServlet
 */

public class RelatedEventsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RelatedEventsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 JsonObject showException = new JsonObject(); 
		 try {

                String groupid = request.getParameter("groupid");
                String instanceid = request.getParameter("instanceid");
                String startdate = request.getParameter("startdate");
                
                if (startdate == null) {
                	startdate = "2000-01-01 00:00:00";
                }
                
                if (groupid != null) {
   	            JsonArray resultRows = RelatedEventsDAO.fetchInstances(groupid, startdate, request.getServletContext());
			    JsonObject appData = new JsonObject();
				appData.add("result_rows",resultRows);
				request.setAttribute("appdata", appData);
                }
		  
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
		 JsonObject showException = new JsonObject(); 
		try {

			 	JsonObject showclientAppInput = (JsonObject) request.getAttribute("showclientappinput"); //added by filter

			 	JsonElement resultType = new JsonParser().parse("related-groups-topn-bysize");
				
			    String minLastFired = "2000-01-01 00:00:00"; // default in case parameter not present
			    if (showclientAppInput.has("parameters")){  
                  for (JsonElement parameter : showclientAppInput.getAsJsonArray("parameters")) {
               	 if (parameter.getAsJsonObject().has("startdate")) {
               		 minLastFired = parameter.getAsJsonObject().get("startdate").getAsJsonObject().get("sql").getAsString();
               	 }
                  }
			    }
			    JsonArray resultRows = RelatedEventsDAO.fetchGroupsTopN(10, minLastFired, request.getServletContext());

			    JsonObject appData = new JsonObject();
			    appData.add("result_type",resultType);
				appData.add("result_rows",resultRows);
				request.setAttribute("appdata", appData);
		  
			 }
		     catch( Exception e) {
		      	System.out.println(e.getMessage());
		     }
	}

}
