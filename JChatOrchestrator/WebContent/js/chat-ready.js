  

function handleBotReply(reply) {
		if (reply != null) {
   		 if (reply.hasOwnProperty('error')) {
     			    if (reply.error == "session invalid") {
     			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
     			    }
     			    else {
   		    	     displayMessage(reply.error, 'Bot');           		    	
   		            } 
     	 } else { 
     		  var activity = undefined;
     		  var operation = undefined;
     		  var operationdata = undefined;
     		  var operationstatus = undefined;
     		  var appdata = {};
       		  var responsetype = undefined;
       		  var topindex = undefined;

     		  if (reply.hasOwnProperty('assistantreply') && reply.assistantreply.hasOwnProperty('context') &&
     				 reply.assistantreply.hasOwnProperty('output')) {   	
   			     
  			     if (reply.assistantreply.context.hasOwnProperty('activity')) {
    			     activity = reply.assistantreply.context.activity;
    			 }
  			     
   			     if (reply.assistantreply.context.hasOwnProperty('operation')) {
   			    	 operation = reply.assistantreply.context.operation;
   			     }
   			     
   			     if (reply.assistantreply.context.hasOwnProperty('operationdata')) {
   			    	 operationdata = reply.assistantreply.context.operationdata;
   			     }
   			     
 			     if (reply.assistantreply.context.hasOwnProperty('operationstatus')) {
    			     operationstatus = reply.assistantreply.context.operationstatus;
    			 } 
 			     
 			    if (reply.hasOwnProperty('appdata')) {
   			         appdata = reply.appdata;
   			     } 
   			     			     
   			     if	(reply.assistantreply.output.generic[0]) {
		           topindex = reply.assistantreply.output.generic.length - 1;
		           responsetype = reply.assistantreply.output.generic[topindex].response_type;		    	 
   			     }
   			     
   			     // Now process bot activity-operation requests
   			     // If no activity-operation, then handle as plain bot dialogues
   			     
   			    if ((activity == undefined) && (operation == undefined)) {
   			    	if (responsetype == "text") {
  		   			     displayMessage(reply.assistantreply.output.generic[topindex].text, 'Bot');
  		   			}
   			    	else if ((responsetype == "option") && 
	   		   		   reply.assistantreply.hasOwnProperty('context')) {
	   		   			      displayOptions(reply.assistantreply,
	   		   			    		  function(reply){handleBotReply(reply)});
	   		   	    }
   			    }
   			    
   			     // If incomplete activity-operation, then process them 
   			     // If complete then handle as plain bot dialogues
   			     
   			     if ((activity != undefined) && (operation != undefined)) {
   			    	 if ((operation == 'collectparameters') && 
   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    			 displayCollectParameters(reply.assistantreply,appdata,
   			    					function(reply){handleBotReply(reply)});  			    					 
   			    	 }
   			    	 else if ((operation == 'showresults') && 
   	   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    		     displayResults(reply.assistantreply,
			    					function(reply){handleBotReply(reply)});  	  					 
   	   			     }
   			    	 else {
   	  			    	if (responsetype == "text") {
     		   			     displayMessage(reply.assistantreply.output.generic[topindex].text, 'Bot');
     		   			}
      			    	else if (responsetype == "option") {
   	   		   			      displayOptions(reply.assistantreply,
   	   		   			    		  function(reply){handleBotReply(reply)});
   	   		   	        }
   			         }			     
   			      }			     
     		   } else {
     			  displayMessage(reply.error, 'Bot');      
     		   }
            }
   		}
     }
 
     $(document).ready(function() {
            var data = new Object();
            $('form#emit').submit(function(event) {
            	event.preventDefault();
            	// important to resend the location.search as the uuid is used to decode lastreply by server
            	var chatpath = window.location.pathname;
            	var $chaturl = window.location.origin + "/JChatOrchestrator/chat/" + document.title + window.location.search;
            	data.action = 'sendtext';
            	data.input = $('#emit_data').val();
            	$.ajax({
            		type: "POST",
            		url: $chaturl,
            		contentType:'application/json',
            		timeout: 30000,
            		data: JSON.stringify(data),
            		beforeSend: function() {
            			displayMessage($('#emit_data').val(), 'NotWatsonBot');
                        $("#emit")[0].reset();       			
            		},
            		complete: function() {          			
            		},
            		success: function(reply) { handleBotReply(reply); },
            		fail: function(data) {   
            		}     		
            	});
                return false;
            });
         });