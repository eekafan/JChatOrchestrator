package com.rombachuk.jchatorchestrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;



public class WatsonConnection {


	private Assistant service = null;
	
	private Map<String,String> sessions = new HashMap<String,String>();
	
	public WatsonConnection (JcoProps props) {
		
		IamOptions iamoptions = new IamOptions.Builder()
			    .apiKey(props.getWatsonassistantapikey())
			    .build();
		
		    this.service = new Assistant(props.getWatsonassistantversion(), iamoptions);
		    

		service.setEndPoint(props.getWatsonassistanturl());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Watson-Learning-Opt-Out", "true");

		service.setDefaultHeaders(headers);

	}
	
	
	public MessageResponse synchronousRequest(MessageOptions options) {
		MessageResponse response = null;
		  try {
			  response = this.service.message(options).execute().getResult();
     	      } catch (RequestTooLargeException e) {
     	    	 System.out.println("exception"+ e);
     	      } catch (ServiceResponseException e) {
     	    	 System.out.println("exception"+ e);
     	      } 
		         catch (Exception e) {
     	    	 System.out.println("exception"+ e);
     	      }
		return response;
	}
	
	
	public Map<String, String> getSessions() {
		return sessions;
	}

	public String addSession(String assistantid,String chatid) {

		        String sessionid = null;
				try {
					CreateSessionOptions sessionoptions = new CreateSessionOptions.Builder(assistantid).build();
					SessionResponse response = service.createSession(sessionoptions).execute().getResult();				
				    sessionid = response.getSessionId();
				    sessions.put(chatid, sessionid);
					}catch (Exception e) {
						sessions.put(chatid, null);
		    	    }
		return sessionid;
	}
	
	public Boolean deleteSession(String assistantid,String chatid) {

		try {
	        DeleteSessionOptions options = new DeleteSessionOptions.Builder(assistantid, sessions.get(chatid)).build();
	        service.deleteSession(options).execute();
	        sessions.remove(chatid);
	        return true;
		}catch (Exception e) {
			sessions.remove(chatid);
			return false;
	    }
	}
	
	public Assistant getAssistant() {
		return service;
	}

	public void setService(Assistant service) {
		this.service = service;
	}
}
