package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;

/**
 * Servlet Filter implementation class ChatFilter
 */


public class ChatFilter implements Filter {
	private ServletContext context;
	
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
    public ChatFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */

	public void init(FilterConfig fConfig) throws ServletException {

		this.context = fConfig.getServletContext();
	}
	/**
	 * @see Filter#destroy()
	 */
    
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
    
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    PrintWriter out = response.getWriter();
	    JsonObject botError = new JsonObject();
		 try {
			    HttpServletRequest httprequest = (HttpServletRequest) request;
				HttpSession session = httprequest.getSession(true);
				response.setContentType("application/json");  
		        
			    if (session.isNew()) {
			    	botError.addProperty("error","session invalid");  
			    	out.write(botError.toString());
				    out.close(); 
			    }
				else {	
			          //J7 Servlet3 fix for read request once problem - do this first
			          // J8 -> InputData input = new InputData.Builder(IOUtils.toString(req.getReader())).build();
					  String bodyString = getRequestBody(httprequest);
				      JsonObject chatRequest = new JsonParser().parse(bodyString).getAsJsonObject();
				 
				      InputStream propsfile = this.context.getResourceAsStream(
				    		  this.context.getInitParameter("jcoProperties"));
				      JcoProps jcoprops = new JcoProps(propsfile);   
				      propsfile.close();
				      
				      InputStream workspacesfile = this.context.getResourceAsStream(
				    		  this.context.getInitParameter("jcoWorkspaces"));
				      JcoWorkspaces jcoworkspaces = new JcoWorkspaces(workspacesfile); 
				      workspacesfile.close(); 
				     
	
				      String servletpath[] = httprequest.getServletPath().split("/");
				      String chatname = servletpath[servletpath.length-1];
				      String workspaceid = jcoworkspaces.findId(chatname);
					
					
					  WatsonConnection watsonconnection = (WatsonConnection) request.getAttribute("watsonconnection");
					  if (watsonconnection == null) {
						     watsonconnection = new WatsonConnection(jcoprops);
						     session.setAttribute("watsonconnection", watsonconnection);
					  }

					  if ((chatRequest.has("input")) && (workspaceid != null)) {
					   request.setAttribute("chatname", chatname);
					   request.setAttribute("workspaceid", workspaceid);
					   request.setAttribute("chatrequest", chatRequest);
					   chain.doFilter(request, response); //send to servlet routed via /chat/name 
				      }
					  else {
						  botError.addProperty("error","input problem");  
					      out.write(botError.toString());
					      out.close(); 
					  }			      
					}
			 }
		     catch( IOException e) {
		    	  botError.addProperty("error","input problem");  
			      out.write(botError.toString());
			      out.close(); 
		     }
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.getResponse());
		    	 botError.addProperty("error","input problem");  
			      out.write(botError.toString());
			      out.close(); 
		     }
	         catch (Exception e) {
	        	 System.out.println(e.getMessage());
	        	 botError.addProperty("error","input problem");  
			      out.write(botError.toString());
			      out.close(); 
		     }
	}
}
