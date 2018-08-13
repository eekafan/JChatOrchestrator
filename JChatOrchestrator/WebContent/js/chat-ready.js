       
     $(document).ready(function() {
            // Send message to the WOS bot
            $('form#emit').submit(function(event) {
            	event.preventDefault();
            	$.ajax({
            		type: "POST",
            		url: "../JChatOrchestrator/chat",
            		timeout: 5000,
            		data: $('#emit_data').val(),
            		beforeSend: function() {
            			displayMessage($('#emit_data').val(), 'NotWatsonBot');
                        $("#emit")[0].reset();       			
            		},
            		complete: function() {          			
            		},
            		success: function(reply){
            			if (reply.botreply) {
            			displayMessage(reply.botreply, 'Bot');
            		    }  
            			if (reply.error) {
            			  if (reply.error == "session invalid") {
            			  window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
            			  }
            			}                       			
            		},
            		fail: function(data) {   
            		}     		
            	});
                return false;
            });
         });