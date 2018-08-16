       
     $(document).ready(function() {
            var data = new Object();
            data.lastReply = new Object();
            $('form#emit').submit(function(event) {
            	event.preventDefault();
            	var $chaturl = window.location.origin + "/JChatOrchestrator/chat" + window.location.search;
            	data.input = $('#emit_data').val();
            	$.ajax({
            		type: "POST",
            		url: $chaturl,
            		contentType:'application/json',
            		timeout: 5000,
            		data: JSON.stringify(data),
            		beforeSend: function() {
            			displayMessage($('#emit_data').val(), 'NotWatsonBot');
                        $("#emit")[0].reset();       			
            		},
            		complete: function() {          			
            		},
            		success: function(reply){
            		   if (!(reply == null)) {
            		    data.lastReply = JSON.parse(JSON.stringify(reply));
            		    if (reply.hasOwnProperty('error')) {
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
            		   }
            		},
            		fail: function(data) {   
            		}     		
            	});
                return false;
            });
         });