define (["jco/handle/BotReply","jco/display/userMessage"],function (handleBotReply,userMessage) {
    var chatReady = function () {  
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
        			userMessage($('#emit_data').val());
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
     }
	
	return chatReady;
});