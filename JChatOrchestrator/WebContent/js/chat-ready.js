  

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
  		   			     displayMessage(reply.assistantdata.output.generic[topindex].text, 'Bot');
  		   			}
   			    	else if ((responsetype == "option") && 
	   		   		   reply.assistantdata.hasOwnProperty('context')) {
	   		   			      displayOptions(reply.assistantdata,
	   		   			    		  function(reply){handleBotReply(reply)});
	   		   	    }
   			    }
   			    
   			     // If incomplete activity-operation, then process them 
   			     // If complete then handle as plain bot dialogues
   			     
   			     if ((activity != undefined) && (operation != undefined)) {
   			    	 if ((operation == 'collectparameters') && 
   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    			 displayCollectParameters(reply.assistantdata,appdata,
   			    					function(reply){handleBotReply(reply)});  			    					 
   			    	 }
   			    	 else if ((operation == 'showresults') && 
   	   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    		     displayResults(reply.assistantdata,
			    					function(reply){handleBotReply(reply)});  	  					 
   	   			     }
   			    	 else {
   	  			    	if (responsetype == "text") {
     		   			     displayMessage(reply.assistantdata.output.generic[topindex].text, 'Bot');
     		   			}
      			    	else if (responsetype == "option") {
   	   		   			      displayOptions(reply.assistantdata,
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
            	data.assistantdata = new Object();
            	data.assistantdata.input = $('#emit_data').val();
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