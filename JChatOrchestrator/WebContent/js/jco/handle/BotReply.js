define(["jco/display/botMessage","jco/display/Image",
	    "jco/collect/Parameters","jco/collect/Options",
	    "jco/show/Launch"], 
		function (displayBotMessage,displayImage,collectParameters,collectOptions,showLaunch) {

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
     			  
     			  
   			     if (reply.assistantdata.context.hasOwnProperty('skills')) {
   			     var userdefined = reply.assistantdata.context.skills['main skill']['user_defined'];
  			     if (userdefined.hasOwnProperty('activity')) {
    			     activity = userdefined.activity;
    			 }
  			     
   			     if (userdefined.hasOwnProperty('operation')) {
   			    	 operation = userdefined.operation;
   			     }
   			     
   			     if (userdefined.hasOwnProperty('operationdata')) {
   			    	 operationdata = userdefined.operationdata;
   			     }
   			     
 			     if (userdefined.hasOwnProperty('operationstatus')) {
    			     operationstatus = userdefined.operationstatus;
    			 } 
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
   			    	 //showresults is set complete by the server - if report is ok to fetch
   			    	 else if ((operation == 'showresults') && 
   	   			    	    (operationdata != undefined) && (operationstatus == 'complete')){
   			    	  switch(activity) {
   			    	   case 'searchrelatedevents':
   			    		showLaunch('relatedevents',reply.assistantdata,appdata,
			    					function(reply){BotReply(reply)});
   			    	    break;
   			    	   case 'searchseasonalevents':
   			    		showLaunch('seasonalevents',reply.assistantdata,appdata,
			    					function(reply){BotReply(reply)});
   			    	    break;
   			    	   case 'searchhistoricevents':
   			    		showLaunch('historicevents',reply.assistantdata,appdata,
			    					function(reply){BotReply(reply)});
    			    	break;
   			    	   default:
   			    		displayMessage('Configuration Error in Chatbot', 'Bot');
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
 