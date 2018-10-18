define(["jco/display/botMessage","jco/display/Image",
	    "jco/collect/Parameters","jco/collect/Options",
	    "jco/show/relatedevents/Launch"], 
		function (displayBotMessage,displayImage,collectParameters,collectOptions,launchRelatedEvents) {

var BotReply = function (reply) {
		if (reply != null) {
   		 if (reply.hasOwnProperty('error')) {
     			    if (reply.error == "session invalid") {
     			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
     			    }
     			    else {
   		    	     displayBotMessage(reply.error);           		    	
   		            } 
     	 } else { 
     		  var activity = undefined;
     		  var operation = undefined;
     		  var operationdata = undefined;
     		  var operationstatus = undefined;
     		  var appdata = undefined;
       		  var responsetype = undefined;
       		  var topindex = undefined;

     		  if (reply.hasOwnProperty('assistantdata') && reply.assistantdata.hasOwnProperty('context') &&
     				 reply.assistantdata.hasOwnProperty('output')) {   	
   			     
  			     if (reply.assistantdata.context.hasOwnProperty('activity')) {
    			     activity = reply.assistantdata.context.activity;
    			 }
  			     
   			     if (reply.assistantdata.context.hasOwnProperty('operation')) {
   			    	 operation = reply.assistantdata.context.operation;
   			     }
   			     
   			     if (reply.assistantdata.context.hasOwnProperty('operationdata')) {
   			    	 operationdata = reply.assistantdata.context.operationdata;
   			     }
   			     
 			     if (reply.assistantdata.context.hasOwnProperty('operationstatus')) {
    			     operationstatus = reply.assistantdata.context.operationstatus;
    			 } 
 			     
 			    if (reply.hasOwnProperty('appdata')) {
   			         appdata = reply.appdata;
   			     } else {
   			    	 appdata = {};
   			     }
   			     			     
   			     if	(reply.assistantdata.output.generic[0]) {
		           topindex = reply.assistantdata.output.generic.length - 1;
		           responsetype = reply.assistantdata.output.generic[topindex].response_type;		    	 
   			     }
   			     
   			     // Now process bot activity-operation requests
   			     // If no activity-operation, then handle as plain bot dialogues
   			     
   			    if ((activity == undefined) && (operation == undefined)) {
   			    	if (responsetype == "text") {
  		   			     displayBotMessage(reply.assistantdata.output.generic[topindex].text);
  		   			}
   			    	else if ((responsetype == "option") && 
	   		   		   reply.assistantdata.hasOwnProperty('context')) {
	   		   			     collectOptions(reply.assistantdata,
	   		   			    		  function(reply){BotReply(reply)});
	   		   	    }
   			    }
   			    
   			     // If  activity-operation, then process them 
   			     
   			     if ((activity != undefined) && (operation != undefined)) {
   			    	 // collectparameters is set complete by the client ie by collectParameters
   			    	 if ((operation == 'collectparameters') && 
   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    			 collectParameters(reply.assistantdata,appdata,
   			    					function(reply){BotReply(reply)});  			    					 
   			    	 }
   			    	 //showresults is set complete by the server - if report is ok to display
   			    	 else if ((operation == 'showresults') && 
   	   			    	    (operationdata != undefined) && (operationstatus == 'complete')){
   			    		 if (activity == 'searchrelatedevents') {
   			    			 launchRelatedEvents(reply.assistantdata,appdata,
   			    					function(reply){BotReply(reply)});
   			    		 }
  	  					 
   	   			     }
   			    	 else {
   	  			    	if (responsetype == "text") {
     		   			     displayBotMessage(reply.assistantdata.output.generic[topindex].text);
     		   			}
      			    	else if (responsetype == "option") {
   	   		   			      collectOptions(reply.assistantdata,
   	   		   			    		  function(reply){BotReply(reply)});
   	   		   	        }
   			         }			     
   			      }			     
     		   } else {
     			  displayMessage(reply.error, 'Bot');      
     		   }
            }
   		}
     }
     return BotReply;
});
 