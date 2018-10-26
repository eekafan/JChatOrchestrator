package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class SeasonalEventsServlet
 */

public class SeasonalEventsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SeasonalEventsServlet() {
        super();
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
		// TODO Auto-generated method stub
		 JsonObject showException = new JsonObject(); 
		try {

			 	JsonObject showclientAppInput = (JsonObject) request.getAttribute("showclientappinput"); //added by filter

			 	JsonElement resultType = new JsonParser().parse("related-groups-topn-bysize");
				
			    String startdate = "2000-01-01 00:00:00"; // default in case parameter not present
			    String enddate = "2000-01-01 00:00:00"; // default in case parameter not present
			    String filter = "Severity >= 0"; // default in case parameter not present
			    if (showclientAppInput.has("parameters")){  
                 for (JsonElement parameter : showclientAppInput.getAsJsonArray("parameters")) {
              	 if (parameter.getAsJsonObject().has("startdate")) {
              		 startdate = parameter.getAsJsonObject().get("startdate").getAsJsonObject().get("sql").getAsString();
              	 }
              	 if (parameter.getAsJsonObject().has("enddate")) {
              		 startdate = parameter.getAsJsonObject().get("enddate").getAsJsonObject().get("sql").getAsString();
              	 }
              	 if (parameter.getAsJsonObject().has("startdate")) {
              		 startdate = parameter.getAsJsonObject().get("filter").getAsJsonObject().getAsString();
              	 }
                 }
			    }
			    JsonArray resultRows = SeasonalEventsDAO.fetchEntriesTopN(20, startdate,enddate,filter, request.getServletContext());

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
