     function handleBotReply(reply) {
		   if (!(reply == null)) {
   		    if (reply.hasOwnProperty('error')) {
     			    if (reply.error == "session invalid") {
     			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
     			    }
     			    else {
   		    	displayMessage(reply.error, 'Bot');           		    	
   		        } 
     		    } else { 
     		    	if ((reply.hasOwnProperty('assistantreply')) && (reply.assistantreply.hasOwnProperty('output')) && (reply.assistantreply.output.generic[0])) {
   			     var latest = reply.assistantreply.output.generic.length - 1;
   			     if (reply.assistantreply.output.generic[latest].response_type == "text") {
   			     displayMessage(reply.assistantreply.output.generic[latest].text, 'Bot');
   			     }
   			     if (reply.assistantreply.output.generic[latest].response_type == "option") {
   			      if (reply.assistantreply.hasOwnProperty('context')) {
   			      displayOptions(reply.assistantreply.context,
   			    		reply.assistantreply.output.generic[latest],
   			    		  function(reply){handleBotReply(reply)});
   			      }
   			     }
   		        } else {
   		      	 displayMessage("Error: no reply", 'Bot');
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