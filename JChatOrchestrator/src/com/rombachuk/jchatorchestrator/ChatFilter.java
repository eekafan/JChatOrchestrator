package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ibm.cloud.sdk.core.service.exception.UnauthorizedException;
import com.ibm.watson.assistant.v2.model.MessageContext;
import com.ibm.watson.assistant.v2.model.MessageContextGlobal;
import com.ibm.watson.assistant.v2.model.MessageContextSkills;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageInputOptions;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;

/**
 * Servlet Filter implementation class ChatFilter 
 */


public class ChatFilter implements Filter {
	
	   static Logger logger = Logger.getLogger(ChatFilter.class);
	
	   private static String formatLogChatDialogue(String level, JsonObject input, MessageResponse botReply) {
	    	String logEntry = "empty";
	    	String cvid = "empty";String turn = "empty"; String in = "empty"; String out = "empty";
	
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
	    	
	    	try {
	    		   MessageContext context = botReply.getContext();
	    		   if (context != null) {
			        MessageContextGlobal global = context.getGlobal();
			        Long tc = global.getSystem().getTurnCount();
	    	        if (tc >= 0) {
	    	    	   turn = Long.toString(tc);
	    	        }
	    	       }
	    	} catch (Exception e) {	
		    }
	    	
	    	if (level.equals("DEBUG")) {
	    		logEntry = "turn={"+turn+"},"+
	    				   "in={"+input.get("input").getAsString()+"},"+
	    				   "out0={"+out+"}"+
	    				   "]";
	    	}
	    	return logEntry;
	    }
	   
	   private static String formatLogChatOperation(String level, Map<String,Object> userdefined) {
	    	String logEntry = "empty";
	    	String ac = "empty";String op = "empty"; String data = "empty"; String stat = "empty";

	    	
	    	if (level.equals("DEBUG")) {
	    		logEntry = "ac={"+userdefined.get("activity").toString()+"},"+
	    				   "op=[op="+userdefined.get("operation").toString()+"},"+
	    				   "data={"+userdefined.get("operationdata").toString()+"},"+
	    				   "status={"+userdefined.get("operationstatus").toString()+"}"+
	    				   "]";
	    	}
	    	return logEntry;
	    }
	   
	   private static void logReply(String level, MessageResponse botReply, JsonObject reply, JsonObject chatclientAssistantInput) {

		    if (level.equals("DEBUG")) {
		    try {
		    logger.debug("chatxchg1 chatid={"+ reply.getAsJsonObject("assistantdata").get("chatid").getAsString()+
				  "} ws={"+reply.getAsJsonObject("assistantdata").get("chatname").getAsString()+
				  "} wsid={"+reply.getAsJsonObject("assistantdata").get("chatassistantid").getAsString()+
				  "} sess={"+ reply.getAsJsonObject("assistantdata").get("chatsessionid").getAsString() +"}"+
				  formatLogChatDialogue("DEBUG",chatclientAssistantInput,botReply));
		    }catch (Exception e) {	
	        	  System.out.println(e.getMessage());
			}
		    try {
		    MessageContextSkills contextskills = botReply.getContext().getSkills();
		    if ((contextskills != null) && (contextskills.containsKey("main skill") == true)){
		    Map<String,Object> mainskill = (Map<String, Object>) contextskills.get("main skill");
		     if (mainskill.containsKey("user_defined") == true) {
	 	      Map<String,Object> userdefined = (Map<String, Object>) mainskill.get("user_defined");
		      if (userdefined.containsKey("activity")) {
		 	   if (!userdefined.get("activity").toString().equals("")) {
				  logger.debug("chatop chatid={"+ reply.getAsJsonObject("assistantdata").get("chatid").getAsString()+
						  "} ws={"+reply.getAsJsonObject("assistantdata").get("chatname").getAsString()+
						  "} wsid={"+reply.getAsJsonObject("assistantdata").get("chatassistantid").getAsString()+
						  "} sess={"+ reply.getAsJsonObject("assistantdata").get("chatsessionid").getAsString() +"}"+
			   formatLogChatOperation("DEBUG",userdefined));
			   }
		      }
		     }
	        }
		    if (!(reply.getAsJsonObject("appdata").isJsonNull())) {
			  logger.debug("chatop-appd chatid={"+ reply.getAsJsonObject("assistantdata").get("chatid").getAsString()+
					  "} ws={"+reply.getAsJsonObject("assistantdata").get("chatname").getAsString()+
			   "} "+reply.getAsJsonObject("appdata").toString());
		    }
		    } catch (Exception e) {	
		    }
		    }
	   }

	   
	   private static MessageContext generateContext (MessageResponse mresponse, JsonObject chatclientInput) {
		    MessageContext context = new MessageContext();		   
		     try {	

		       context = mresponse.getContext();
		       MessageContextGlobal global = context.getGlobal();
		       Long tc = global.getSystem().getTurnCount();
		       tc = tc + 1;
		       global.getSystem().setTurnCount(tc);
		       
		       if (chatclientInput.has("contextinput")) {
			     MessageContextSkills contextskills = context.getSkills();
		         if (contextskills.containsKey("main skill") == true) {
			     Map<String,Object> mainskill = (Map<String, Object>) contextskills.get("main skill");
			      if (mainskill.containsKey("user_defined") == true) {
			      Map<String,Object> userdefined = (Map<String, Object>) mainskill.get("user_defined");
		          JsonObject chatclientContextinput = chatclientInput.getAsJsonObject("contextinput");
		          Set<String> keyset = chatclientContextinput.keySet();
		          for (String key : keyset) {

		        	  if (chatclientContextinput.get(key).isJsonObject()) {
		        	   String input = chatclientContextinput.get(key).getAsJsonObject().toString();
		        	  
		        	  Map<String, Object> inputAsMap = new Gson().fromJson(
		        			  input, 
		        			  new TypeToken<HashMap<String, Object>>() {}.getType()
		        			);
		    	 	  userdefined.put(key, inputAsMap);
		        	  }
		        	  if (chatclientContextinput.get(key).isJsonPrimitive()) {
		        		  String input = chatclientContextinput.get(key).getAsString();
		        		  userdefined.put(key, input);
		        	  }
		          }
		         }
		        }
		       }
		     }catch (Exception e) {	
			}
		    return context;
	   }
	   
		private static MessageResponse  submitSynchronousWatsonAssistantRequest (
				WatsonConnection watsonconnection,  
				String chatassistantid, 
				String chatid, 
				MessageResponse lreply, 
				JsonObject chatclientinput)
		{
			String chatsessionid = watsonconnection.getSessions().get(chatid);
	        MessageContext latestContext = generateContext(lreply, chatclientinput);
		    //send message to watson assistant
		    MessageInputOptions inputoptions = new MessageInputOptions();
	        inputoptions.setReturnContext(true);
	        
		    MessageInput input = new MessageInput.Builder()
		    		  .messageType("text")
		    		  .options(inputoptions)
		    		  .text(chatclientinput.get("input").getAsString())
		    		  .build();
		    
		    MessageOptions options = new MessageOptions.Builder(chatassistantid,chatsessionid).build();
			if ((lreply == null) ) {
					   options= new MessageOptions.Builder(chatassistantid,chatsessionid)
							    .input(input)
							    .build();
			} else {
			  	    options = new MessageOptions.Builder(chatassistantid,chatsessionid)
				    .input(input)
				    //.intents(lastReply.getIntents())
				    //.entities(lastReply.getEntities())
				    .context(latestContext)
				    //.output(lastReply.getOutput())
				    .build();
		    }
			
			MessageResponse reply = watsonconnection.synchronousRequest(options);
			// process reply from watson assistant - 
			if (reply == null) {
				// maybe timeout - try to build new session and resubmit
				Boolean deleteresult = watsonconnection.deleteSession(chatassistantid, chatid);
				chatsessionid = watsonconnection.addSession(chatassistantid, chatid);
				if (chatsessionid != null) {
				    options = new MessageOptions.Builder(chatassistantid,chatsessionid).build();
					if ((lreply == null) ) {
							   options= new MessageOptions.Builder(chatassistantid,chatsessionid)
									    .input(input)
									    .build();
					} else {
					  	    options = new MessageOptions.Builder(chatassistantid,chatsessionid)
						    .input(input)
						    //.intents(lastReply.getIntents())
						    //.entities(lastReply.getEntities())
						    .context(latestContext)
						    //.output(lastReply.getOutput())
						    .build();
				    }
					reply = watsonconnection.synchronousRequest(options);

			    }
			}
			return reply;		
		}
		
	  private static String getRequestBody (final HttpServletRequest request) 

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
					 logger.debug("servlet url: "+httprequest.getServletPath()+'?'+httprequest.getQueryString());
					 
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
				    	  request.setAttribute("chatclientappinput", chatclientAppInput);
				      }
				      
				        
				      JcoWorkspaces jcoworkspaces = (JcoWorkspaces) session.getServletContext().getAttribute("jcoworkspaces");
	
				 
	
					
					  WatsonConnection watsonconnection = (WatsonConnection) httprequest.getSession().getAttribute("watsonconnection");
					  if (watsonconnection == null) {
						     watsonconnection = new WatsonConnection((JcoProps) session.getServletContext().getAttribute("jcoprops"));
						     
					  }
					  if (watsonconnection.getSessions().get(request.getParameter("chatid")) == null) {
					    	 watsonconnection.addSession(jcoworkspaces.findId(request.getParameter("name")),
					    			 request.getParameter("chatid"));
					  }
					  session.setAttribute("watsonconnection", watsonconnection);

					   
				
               		 try {	
                	       botReply = submitSynchronousWatsonAssistantRequest(watsonconnection,
                	    		   jcoworkspaces.findId(request.getParameter("name")),
                	    		   request.getParameter("chatid"),    		   
                	    		   (MessageResponse) session.getAttribute(request.getParameter("chatid")+"lastreply"), 
                	    		   chatclientAssistantInput
                	    		   );
                	       request.setAttribute("botreply", botReply);
                	       session.setAttribute("watsonconnection", watsonconnection);
    					   session.setAttribute(request.getParameter("chatid")+"lastreply",botReply);  
               		 }	 
        		     catch( UnauthorizedException e) {
        		    	 botException.addProperty("error","Assistant Access Authorisation problem"); 
        		     }
        	         catch (Exception e) {
        	        	 System.out.println(e.getMessage());
        		    	 botException.addProperty("error","Assistant Request error"); 
        		     }
               		 if (botException.entrySet().isEmpty()) {
					   // pre-servlet processing complete - send reply to specific servlet
  					   // servlet will process reply from bot and interface via appdata
					   chain.doFilter(request, response); 
					   // post-servlet processing starts - process reply from chatapp
					   //

					   JsonObject assistantreply = (JsonObject) new JsonParser().parse(botReply.toString());
					   assistantreply.addProperty("chatname",request.getParameter("name"));
					   assistantreply.addProperty("chatassistantid", jcoworkspaces.findId(request.getParameter("name")));
					   assistantreply.addProperty("chatid", request.getParameter("chatid"));
					   assistantreply.addProperty("chatsessionid", watsonconnection.getSessions().get(request.getParameter("chatid")));
					   JsonObject appdata = (JsonObject)  request.getAttribute("appdata");					     
					   JsonObject reply = new JsonObject();
					   reply.add("assistantdata",assistantreply);
					   reply.add("appdata", appdata);
					   out.write(reply.toString());
					   out.close(); 
					   
					   logReply("DEBUG", botReply, reply, chatclientAssistantInput);					    		
						  
					   } 
					  else {
						   //bot error
						   out.write(botException.toString());
						   out.close(); 
						   logger.debug("chatxchg chatid={" + request.getParameter("chatid")+ 
								   "{ ws={" + request.getParameter("name") +
								   "} error={"+botException.toString()+"}");
					   }
					   
			 }
		 }
		 catch( IOException e) {
		    	  botException.addProperty("error","input problem");  
			      out.write(botException.toString());
				   logger.debug("chatxchg chatid={unidentified} ws={unidentified} error={"+botException.toString()+"}");
			      out.close(); 
		 }
		 catch( UnauthorizedException e) {
		    	 System.out.println(e.toString());
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
