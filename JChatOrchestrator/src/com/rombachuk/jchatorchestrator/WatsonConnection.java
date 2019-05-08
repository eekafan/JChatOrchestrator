package com.rombachuk.jchatorchestrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;



public class WatsonConnection {


	private Assistant service = null;
	
	private Map<String,String> sessions = new HashMap<String,String>();

	public WatsonConnection (JcoProps props, List<String> dialogueassistants) {
		
		IamOptions iamoptions = new IamOptions.Builder()
			    .apiKey(props.getWatsonassistantapikey())
			    .build();
		
		    this.service = new Assistant(props.getWatsonassistantversion(), iamoptions);
		    

		service.setEndPoint(props.getWatsonassistanturl());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Watson-Learning-Opt-Out", "true");

		service.setDefaultHeaders(headers);
		for (String da:dialogueassistants) {
			try {
			CreateSessionOptions sessionoptions = new CreateSessionOptions.Builder(da).build();
			SessionResponse response = service.createSession(sessionoptions).execute().getResult();
		    sessions.put(da, response.getSessionId());
			}catch (Exception e) {
				sessions.put(da, null);
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
     	    	 response.put("exception", e);
     	      } catch (ServiceResponseException e) {
     	    	 response.put("exception", e);
     	      } catch (Exception e) {
      	    	 response.put("exception", e);
     	      }
		return response;
	}
	
	public Assistant getAssistant() {
		return assistant;
	}

	public void setAssistant(Assistant assistant) {
		this.assistant = assistant;
	}
}
