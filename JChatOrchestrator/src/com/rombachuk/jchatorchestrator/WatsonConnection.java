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
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;



public class WatsonConnection {


	private Assistant service = null;
	
	private Map<String,String> sessions = new HashMap<String,String>();
	
	private String CreateSession (String assistantid) {
		CreateSessionOptions sessionoptions = new CreateSessionOptions.Builder(assistantid).build();
		SessionResponse response = service.createSession(sessionoptions).execute().getResult();
		return response.getSessionId();
	}

	public WatsonConnection (JcoProps props, JcoWorkspaces workspaces) {
		
		IamOptions iamoptions = new IamOptions.Builder()
			    .apiKey(props.getWatsonassistantapikey())
			    .build();
		
		    this.service = new Assistant(props.getWatsonassistantversion(), iamoptions);
		    

		service.setEndPoint(props.getWatsonassistanturl());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Watson-Learning-Opt-Out", "true");

		service.setDefaultHeaders(headers);
		for (Workspace w : workspaces.getList()) {
			try {
			String sessionid = CreateSession(w.getId());
		    sessions.put(w.getId(), sessionid);
			}catch (Exception e) {
				sessions.put(w.getId(), null);
    	    }
		}
	}
	
	
	
	public Map<String, String> getSessions() {
		return sessions;
	}



	public void setSessions(Map<String, String> sessions) {
		this.sessions = sessions;
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
	
	public String renewSession(String assistantid) {
		   System.out.println("RENEW "+assistantid);
		        String sessionid = null;
				try {
					
				    sessionid = CreateSession(assistantid);
				    sessions.put(assistantid, sessionid);
					}catch (Exception e) {
						sessions.put(assistantid, null);
		    	    }
				System.out.println("RENEW "+assistantid+" Sessionid "+sessionid);
		return sessionid;
	}
	
	public Assistant getAssistant() {
		return service;
	}

	public void setService(Assistant service) {
		this.service = service;
	}
}
