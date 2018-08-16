package com.rombachuk.jchatorchestrator;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.DialogNodeVisitedDetails;
import com.ibm.watson.developer_cloud.assistant.v1.model.DialogRuntimeResponseGeneric;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.LogMessage;
import com.ibm.watson.developer_cloud.assistant.v1.model.OutputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeEntity;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeIntent;
import com.ibm.watson.developer_cloud.assistant.v1.model.SystemResponse;

public class ChatRequestOptions {
	 
	 public String getWorkspaceid() {
		return workspaceid;
	}


	public void setWorkspaceid(String workspaceid) {
		this.workspaceid = workspaceid;
	}


	public InputData getInput() {
		return input;
	}


	public void setInput(InputData input) {
		this.input = input;
	}


	public Boolean getAlternateIntents() {
		return alternateIntents;
	}


	public void setAlternateIntents(Boolean alternateIntents) {
		this.alternateIntents = alternateIntents;
	}


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public List<RuntimeEntity> getEntities() {
		return entities;
	}


	public void setEntities(List<RuntimeEntity> entities) {
		this.entities = entities;
	}


	public List<RuntimeIntent> getIntents() {
		return intents;
	}


	public void setIntents(List<RuntimeIntent> intents) {
		this.intents = intents;
	}


	public OutputData getOutput() {
		return output;
	}


	public void setOutput(OutputData output) {
		this.output = output;
	}


	public Boolean getNodesVisitedDetails() {
		return nodesVisitedDetails;
	}


	public void setNodesVisitedDetails(Boolean nodesVisitedDetails) {
		this.nodesVisitedDetails = nodesVisitedDetails;
	}


	 private String workspaceid = new String();
	 private InputData input = new InputData.Builder("").build();
	 private Boolean alternateIntents = false;
	 private Context context = new Context();	 
	 private List<RuntimeEntity> entities = new ArrayList<RuntimeEntity>();
	 private List<RuntimeIntent> intents = new ArrayList<RuntimeIntent>();;
	 private OutputData output = new OutputData();
	 private Boolean nodesVisitedDetails = true;

	
	public ChatRequestOptions (JsonObject requestOptions) {
		
		SystemResponse system = new SystemResponse();
		List<DialogRuntimeResponseGeneric> listGeneric = new ArrayList<DialogRuntimeResponseGeneric>();
		List<LogMessage> listLog = new ArrayList<LogMessage>();
		List<String> nodesVisited = new ArrayList<String>();
		List<DialogNodeVisitedDetails> listDialogNodeDetails = new ArrayList<DialogNodeVisitedDetails>();;
		List<String> listText = new ArrayList<String>();
		
		
		
		if (requestOptions.has("input")) {
		JsonObject jsonInput = requestOptions.get("input").getAsJsonObject();
		this.input = new InputData.Builder(jsonInput.get("text").getAsString()).build();
	    } 
		
		
		if (requestOptions.has("context")) {
		JsonObject jsonContext = requestOptions.get("context").getAsJsonObject();
		this.context.setConversationId(jsonContext.get("conversation_id").getAsString());
	    this.context.setSystem(system);
		}

		if (requestOptions.has("intents")) {
		JsonArray jsonIntents = requestOptions.get("intents").getAsJsonArray();
		  for (JsonElement je : jsonIntents) {
			   RuntimeIntent element = new RuntimeIntent();
		      element.setIntent(je.getAsJsonObject().get("intent").getAsString());
			   element.setConfidence(je.getAsJsonObject().get("confidence").getAsDouble());
			   this.intents.add(element);
		  }	
		}
		
		if (requestOptions.has("output")) {
		JsonObject jsonOutput = requestOptions.get("output").getAsJsonObject();
		if (jsonOutput.has("generic")) {
		JsonArray jsonGenerics = jsonOutput.get("generic").getAsJsonArray();
		   for (JsonElement je : jsonGenerics) {
			   DialogRuntimeResponseGeneric element = new DialogRuntimeResponseGeneric();
			   if (je.getAsJsonObject().has("response_type")) {
			    element.setResponseType(je.getAsJsonObject().get("response_type").getAsString());
			   }
			   if (je.getAsJsonObject().has("text")) {
			    element.setText(je.getAsJsonObject().get("text").getAsString());
			   }
			   listGeneric.add(element);
		   }
		}
		
		if (jsonOutput.has("log_messages")) {
		JsonArray jsonLogs = jsonOutput.get("log_messages").getAsJsonArray();
		   for (JsonElement je : jsonLogs) {
			   LogMessage element = new LogMessage();
			   if (je.getAsJsonObject().has("level")) {
			    element.setLevel(je.getAsJsonObject().get("level").getAsString());
			   }
			   if (je.getAsJsonObject().has("msg")) {
			    element.setMsg(je.getAsJsonObject().get("msg").getAsString());
		       }
			   listLog.add(element);
		   }
		}
		
		if (jsonOutput.has("nodes_visited")) {
	    JsonArray jsonNodes = jsonOutput.get("nodes_visited").getAsJsonArray();
		    for (JsonElement je : jsonNodes) {
				   nodesVisited.add(je.getAsString());
			}
		}
		
		if (jsonOutput.has("nodes_details")) {
	    JsonArray jsonNodeDetails = jsonOutput.get("nodes_details").getAsJsonArray();
			for (JsonElement je : jsonNodeDetails) {
				DialogNodeVisitedDetails element = new DialogNodeVisitedDetails();
				if (je.getAsJsonObject().has("conditions")) {
			     element.setConditions(je.getAsJsonObject().get("conditions").getAsString());
			    }
			    if (je.getAsJsonObject().has("dialognode")) {
			     element.setDialogNode(je.getAsJsonObject().get("dialognode").getAsString());
				}
			    if (je.getAsJsonObject().has("title")) {
				 element.setTitle(je.getAsJsonObject().get("title").getAsString());
				}			    
			    listDialogNodeDetails.add(element);
			}
		}
		
		if (jsonOutput.has("text")) {
	    JsonArray jsonTexts = jsonOutput.get("text").getAsJsonArray();
			    for (JsonElement je : jsonTexts) {
					   listText.add(je.getAsString());
				}	 
		}
		
	    this.output.setGeneric(listGeneric);
	    this.output.setLogMessages(listLog);
	    this.output.setNodesVisited(nodesVisited);
	    this.output.setNodesVisitedDetails(listDialogNodeDetails);
	    this.output.setText(listText);
		}
		
	}

}
