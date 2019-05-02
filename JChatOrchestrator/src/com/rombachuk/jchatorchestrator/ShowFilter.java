package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



/**
 * Servlet Filter implementation class ShowFilter
 */

public class ShowFilter implements Filter {
	
	  static Logger logger = Logger.getLogger(ShowFilter.class);
	  
	  private JsonObject getUrlParameters (HttpServletRequest httprequest) {
	   JsonObject urlparameters = new JsonObject();
       Enumeration<String> parameterNames = httprequest.getParameterNames();
       while (parameterNames.hasMoreElements()) {				 
          String paramName = parameterNames.nextElement();
          String paramValues[] = httprequest.getParameterValues(paramName);
          urlparameters.addProperty(paramName, paramValues[0]);
       }
       return urlparameters;
	  }
	  
	  
	  private String formatLogShow(String level, String opname, JsonObject urlParams, JsonObject showclientAppInput, JsonObject appdata) {
	      String logShow = "";
		  String chatid = "unknown"; String showid = "unknown";
	  if (urlParams.has("chatid")) {
		  chatid = urlParams.get("chatid").getAsString();
	  }
	  if (urlParams.has("showid")) {
		  showid = urlParams.get("showid").getAsString();
	  }
	  
	  String otherparameters="";	  
	  for (Map.Entry<String,JsonElement> entry : urlParams.entrySet()) {
		  if (!(entry.getKey().equals("chatid")) && !(entry.getKey().equals("showid"))) {
			  otherparameters = otherparameters + "&" + entry.getKey() + "=" + entry.getValue().getAsString();
	   }
	  }
	 
	  if (level.equals("DEBUG")) {
       if (!(showclientAppInput.size() == 0)) {
		  logShow = "showop chatid={"+chatid+"} op={"+opname+"} showid={"+showid+"} POST otherparams= {"+otherparameters+"} body={"+showclientAppInput.toString()+"} replydata={"+appdata.toString()+"}";
	   } else {
		  logShow = "showop chatid={"+chatid+"} op={"+opname+"} showid={"+showid+"} GET  otherparams= {"+otherparameters+"} replydata={"+appdata.toString()+"}";
	   }
	  }
	  return logShow;
	  }
	
	  private String getRequestBody (final HttpServletRequest request) 

	     {
	             
	    HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    try {
	        InputStream inputStream = requestWrapper.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        }
	    } catch (IOException ex) {
	        
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException iox) {
	                // ignore
	            }
	        }
	    }	 
	    return stringBuilder.toString();
	}

    /**
     * Default constructor. 
     */
    public ShowFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	     PrintWriter out = response.getWriter();
	     response.setContentType("application/json");  
	     JsonObject showException = new JsonObject();
		 try {
			    HttpServletRequest httprequest = (HttpServletRequest) request;
				if (httprequest.getRequestedSessionId() != null
				        && !httprequest.isRequestedSessionIdValid()) {
				    // Session is expired so flag error as reply to enable client to respond
			    	showException.addProperty("error","session invalid");  
			    	out.write(showException.toString());
				    out.close(); 
			    }
				else {
					 // create new session if none exists, else collect current valid session
					 HttpSession session = httprequest.getSession(true);
					 
			          //J7 Servlet3 fix for read request once problem - do this first
			          // J8 -> x = IOUtils.toString(req.getReader()));
					 // body is empty for GETs but populated for POSTs
					  JsonObject urlParams = getUrlParameters(httprequest);
					  String bodyString = getRequestBody(httprequest);
					  JsonObject showclientAppInput = new JsonObject();
					  if (!bodyString.isEmpty()) { 
				       JsonObject showclientInput = new JsonParser().parse(bodyString).getAsJsonObject();
				       if (showclientInput.has("appdata")) {
				    	  showclientAppInput  = showclientInput.getAsJsonObject("appdata");
				       }
					  }
					  
				       String servletpath[] = httprequest.getServletPath().split("/");
				       String chatname = servletpath[servletpath.length-1];
				       // logger.debug("servlet:"+httprequest.getServletPath()+'?'+httprequest.getQueryString());

					   request.setAttribute("showclientappinput", showclientAppInput);
					   request.setAttribute("showexception", showException);
 				   
					   // pre-servlet processing complete - send to show-data servlet
					   chain.doFilter(request, response); 
					   // post-servlet processing starts - process reply from chatapp
					   // expects errors in request.showexception
					   // expects show-data specific data in request.appdata
					   // for non-error combine botreply and appdata
					
					   showException = (JsonObject)  request.getAttribute("showexception");
 				       JsonObject appdata = (JsonObject)  request.getAttribute("appdata");
					   if (showException.entrySet().isEmpty()) {
						  JsonObject reply = new JsonObject();
						  reply.add("appdata", appdata);
						  out.write(reply.toString());
						  logger.debug(formatLogShow("DEBUG",chatname,urlParams,showclientAppInput,appdata));
			
					   } 
					   else {
						   out.write(showException.toString());
						   logger.debug("showop error={"+showException.toString()+"}");
					   }
					   out.close(); 
				  }
			 }
		     catch( IOException e) {
		    	  showException.addProperty("error","input problem");  
			      out.write(showException.toString());
			      out.close(); 
		     }
	         catch (Exception e) {
	        	 System.out.println(e.getMessage());
	        	 showException.addProperty("error","input problem");  
			      out.write(showException.toString());
			      out.close(); 
		     }
	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
