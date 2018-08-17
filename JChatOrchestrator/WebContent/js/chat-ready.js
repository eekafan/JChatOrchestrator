       
     $(document).ready(function() {
            var data = new Object();
            $('form#emit').submit(function(event) {
            	event.preventDefault();
            	// important to resend the location.search as the uuid is used to decode lastreply by server
            	var $chaturl = window.location.origin + "/JChatOrchestrator/chat" + window.location.search;
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
            		success: function(reply){
            		   if (!(reply == null)) {
            		    if (reply.hasOwnProperty('error')) {
              			    if (reply.error == "session invalid") {
              			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
              			    }
              			    else {
            		    	displayMessage(reply.error, 'Bot');           		    	
            		        } 
              		    } else { 
              		    	if ((reply.hasOwnProperty('output')) &&
            			       (reply.output.text[0])) {
            			     var latest = reply.output.text.length - 1;
            			     displayMessage(reply.output.text[latest], 'Bot');
            		        } else {
            		      	 displayMessage("Error: no reply", 'Bot');
            		        } 
              		    }  
            		   }
            		},
            		fail: function(data) {   
            		}     		
            	});
                return false;
            });
         });