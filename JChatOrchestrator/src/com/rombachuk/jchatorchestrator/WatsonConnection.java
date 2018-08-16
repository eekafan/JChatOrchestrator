package com.rombachuk.jchatorchestrator;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;

public class WatsonConnection {


	private Assistant assistant = null;
	
	public WatsonConnection (JcoProps props) {
		    this.assistant = new Assistant(props.getWatsonassistantversion(),
		    props.getWatsonassistantusername(),
		    props.getWatsonassistantpassword());

		assistant.setEndPoint(props.getWatsonassistanturl());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Watson-Learning-Opt-Out", "true");

		assistant.setDefaultHeaders(headers);
	}
	
	public MessageResponse synchronousRequest(MessageOptions options) {
		MessageResponse response = null;
		  try {
			  response = this.assistant.message(options).execute();
              } catch (NotFoundException e) {
     	       response.put("exception", e);
     	      } catch (RequestTooLargeException e) {
     	      } catch (ServiceResponseException e) {
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
