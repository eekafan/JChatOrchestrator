package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

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

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.assistant.v2.model.MessageResponse;

/**
 * Servlet Filter implementation class ChatFilter 
 */


public class ChatFilter implements Filter {
	
	   static Logger logger = Logger.getLogger(ChatFilter.class);
	
	   private static String formatLogChatDialogue(String level, MessageResponse botReply) {
	    	String logEntry = "empty";
	    	String cvid = "empty";String turn = "empty"; String in = "empty"; String out = "empty";
	    	if (!botReply.getContext().getConversationId().isEmpty()) {
	    		cvid = botReply.getContext().getConversationId();
	    	}
	    	if (!botReply.getContext().getSystem().get("dialog_turn_counter").toString().isEmpty()) {
	    		turn = botReply.getContext().getSystem().get("dialog_turn_counter").toString();
	    	}
	    	if (!botReply.getInput().getText().isEmpty()) {
	    		in = botReply.getInput().getText();
	    	}
	    	if (!botReply.getOutput().getGeneric().isEmpty()) {
	    		String response_type = botReply.getOutput().getGeneric().get(0).getResponseType();
	    		if (response_type.equals("text")) {
	    			out = response_type+":"+botReply.getOutput().getGeneric().get(0).getText();
	    		} else {
	    		if (response_type.equals("option")) {
	    			out = response_type+":"+botReply.getOutput().getGeneric().get(0).getDescription();
	    		} else {
	    			out = response_type+":unprocessed by logger";
	    		}
	    		}
	    	}
	    	
	    	if (level.equals("DEBUG")) {
	    		logEntry = "cv=[id={"+cvid+"},"+
	    				   "turn={"+turn+"},"+
	    				   "in={"+in+"},"+
	    				   "out0={"+out+"}"+
	    				   "]";
	    	}
	    	return logEntry;
	    }
	   
	   private static String formatLogChatOperation(String level, MessageResponse botReply) {
	    	String logEntry = "empty";
	    	String ac = "empty";String op = "empty"; String data = "empty"; String stat = "empty";
	    	if (botReply.getContext().containsKey("activity")){
	    		ac = botReply.getContext().get("activity").toString();
	    	}
	    	if (botReply.getContext().containsKey("operation")){
	    		op = botReply.getContext().get("operation").toString();
	    	}
	    	if (botReply.getContext().containsKey("operationdata")){
	    		data = botReply.getContext().get("operationdata").toString();
	    	}
	    	if (botReply.getContext().containsKey("operationstatus")){
	    		stat = botReply.getContext().get("operationstatus").toString();
	    	}
	    	
	    	if (level.equals("DEBUG")) {
	    		logEntry = "ac={"+ac+"},"+
	    				   "op=[op="+op+"},"+
	    				   "data={"+data+"},"+
	    				   "status={"+stat+"}"+
	    				   "]";
	    	}
	    	return logEntry;
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
    public ChatFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */

	public void init(FilterConfig fConfig) throws ServletException {

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
	     response.setContentType("application/json");  
	     JsonObject botException = new JsonObject();
	     MessageResponse botReply = new MessageResponse();
	     
		 try {
			   
			    HttpServletRequest httprequest = (HttpServletRequest) request;
				if (httprequest.getRequestedSessionId() != null
				        && !httprequest.isRequestedSessionIdValid()) {
				    // Session is expired so flag error as reply to enable client to respond
			    	botException.addProperty("error","session invalid");  
			    	out.write(botException.toString());
				    out.close(); 
			    }
				else {
					 // create new session if none exists, else collect current valid session
					 HttpSession session = httprequest.getSession(true);
					 
			          //J7 Servlet3 fix for read request once problem - do this first
			          // J8 -> InputData input = new InputData.Builder(IOUtils.toString(req.getReader())).build();
					  String bodyString = getRequestBody(httprequest);
				      JsonObject chatclientInput = new JsonParser().parse(bodyString).getAsJsonObject();
				      JsonObject chatclientAssistantInput = new JsonObject();
				      JsonObject chatclientAppInput = new JsonObject();
				  
				      if (chatclientInput.has("assistantdata")) {
				    	  chatclientAssistantInput  = chatclientInput.getAsJsonObject("assistantdata");
				      }
				      if (chatclientInput.has("appdata")) {
				    	  chatclientAppInput  = chatclientInput.getAsJsonObject("appdata");
				      }
				        
				      JcoDialogueAssistants jcodialogueassistants = (JcoDialogueAssistants) session.getServletContext().getAttribute("jcodialogueassistants");
				        
				 
					  JcoProps jcoprops = (JcoProps) session.getServletContext().getAttribute("jcoprops");   
	
				      String servletpath[] = httprequest.getServletPath().split("/");
				      // logger.debug("servlet:"+httprequest.getServletPath()+'?'+httprequest.getQueryString());
				      String chatname = servletpath[servletpath.length-1];
					
					  WatsonConnection watsonconnection = (WatsonConnection) request.getAttribute("watsonconnection");
					  if (watsonconnection == null) {
						     watsonconnection = new WatsonConnection(jcoprops,jcodialogueassistants.getList());
						     session.setAttribute("watsonconnection", watsonconnection);
					  }

					   request.setAttribute("chatname", chatname);
					   request.setAttribute("chatclientassistantinput", chatclientAssistantInput);
					   request.setAttribute("chatclientappinput", chatclientAppInput);
					   request.setAttribute("botreply", botReply);
					   request.setAttribute("botexception", botException);

					    String chatuuid = request.getParameter("chatid");
					    String chatuuid_lastreply = chatuuid+"lastreply";
					    MessageResponse lastReply = (MessageResponse) session.getAttribute(chatuuid_lastreply);
					    Context latestContext = lastReply.getContext();
					    if (chatclientAssistantInput.has("contextinput")) {
					     JsonObject chatclientContextinput = chatclientAssistantInput.getAsJsonObject("contextinput");
					     Set<String> keyset = chatclientContextinput.keySet();
					     for (String key : keyset) {
					    		latestContext.put(key, chatclientContextinput.get(key));
					     }
					    }
                 	    request.setAttribute("latestcontext", latestContext);	
  				   
					   // pre-servlet processing complete - send to chatapp servlet
					   chain.doFilter(request, response); 
					   // post-servlet processing starts - process reply from chatapp
					   // expects good answer from assistant in request.botreply
					   // expects errors in request.botexception
					   // expects chatapp specific data in request.appdata
					   // for non-error combine botreply and appdata
					
					   botException = (JsonObject)  request.getAttribute("botexception");
					   botReply = (MessageResponse) request.getAttribute("botreply");

					   JsonObject assistantreply = (JsonObject) new JsonParser().parse(botReply.toString());
					   JsonObject appdata = (JsonObject)  request.getAttribute("appdata");
					   if (botException.entrySet().isEmpty()) {
						  session.setAttribute(chatuuid_lastreply, botReply);
						  JsonObject reply = new JsonObject();
						  reply.add("assistantdata",assistantreply);
						  reply.add("appdata", appdata);
						  out.write(reply.toString());
						  logger.debug("chatxchg chatid={"+ chatuuid+ "} ws={"+chatname+"} "+formatLogChatDialogue("DEBUG",botReply));
						  if (botReply.getContext().containsKey("activity")) {
							  if (!botReply.getContext().get("activity").equals(""))
					          logger.debug("chatop chatid={"+ chatuuid+ "} ws={"+chatname+"} "+formatLogChatOperation("DEBUG",botReply));
						  }
						  if (!(appdata.size() == 0)) {
							  logger.debug("appop chatid={"+ chatuuid+ "} ws={"+chatname+"} "+appdata.toString());
						  }
					   } 
					   else {
						   out.write(botException.toString());
						   logger.debug("chatxchg chatid={"+ chatuuid+ "} ws={"+chatname+"} error={"+botException.toString()+"}");
					   }
					   out.close(); 
				      }
					  else {
						  botException.addProperty("error","input problem");  
					      out.write(botException.toString());
						   logger.debug("chatxchg chatid={unidentified} ws={unidentified} error={"+botException.toString()+"}");
					      out.close(); 
					  }			      

			 }
		     catch( IOException e) {
		    	  botException.addProperty("error","input problem");  
			      out.write(botException.toString());
				   logger.debug("chatxchg chatid={unidentified} ws={unidentified} error={"+botException.toString()+"}");
			      out.close(); 
		     }
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.getResponse());
		    	 botException.addProperty("error","input problem");  
			      out.write(botException.toString());
				   logger.debug("chatxchg chatid={unidentified} ws={unidentified} error={"+botException.toString()+"}");
			      out.close(); 
		     }
	         catch (Exception e) {
	        	 System.out.println(e.getMessage());
	        	 botException.addProperty("error","input problem");  
			      out.write(botException.toString());
				   logger.debug("chatxchg chatid={unidentified} ws={unidentified} error={"+botException.toString()+"}");
			      out.close(); 
		     }
	}
}
