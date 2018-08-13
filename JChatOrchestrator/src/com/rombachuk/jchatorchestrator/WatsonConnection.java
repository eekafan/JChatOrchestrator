package com.rombachuk.jchatorchestrator;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;

public class WatsonConnection {
	public Assistant getAssistant() {
		return assistant;
	}

	public void setAssistant(Assistant assistant) {
		this.assistant = assistant;
	}

	Assistant assistant;
	
	public WatsonConnection (JcoProps props) {
		    this.assistant = new Assistant(props.getWatsonassistantversion(),
		    props.getWatsonassistantusername(),
		    props.getWatsonassistantpassword());

		assistant.setEndPoint(props.getWatsonassistanturl());
	}
}
