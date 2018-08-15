       
     $(document).ready(function() {
            // Send message to the WOS bot
            $('form#emit').submit(function(event) {
            	event.preventDefault();
            	var $chaturl = window.location.origin + "/JChatOrchestrator/chat" + window.location.search;
            	$.ajax({
            		type: "POST",
            		url: $chaturl,
            		timeout: 5000,
            		data: $('#emit_data').val(),
            		beforeSend: function() {
            			displayMessage($('#emit_data').val(), 'NotWatsonBot');
                        $("#emit")[0].reset();       			
            		},
            		complete: function() {          			
            		},
            		success: function(reply){
            		   if (reply.error) {
              			    if (reply.error == "session invalid") {
              			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
              			    }
              			    else {
            		    	displayMessage(reply.error, 'Bot');
            		        } 
              		   } else {           		  
            			    if (reply.output.text[0]) {
            			     displayMessage(reply.output.text[0], 'Bot');
            		        } else {
            		      	 displayMessage("Error: no reply", 'Bot');
            		        } 
              		   }                      			
            		},
            		fail: function(data) {   
            		}     		
            	});
                return false;
            });
         });