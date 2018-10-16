define (["jco/handleBotReply"],function (handleBotReply) {
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
     }
    
	function displayMessage(text, user) {
		
		var watson = 'Bot';

	    if (text && text != "") {
	        var chat = document.getElementById('chatBox');
	        var bubble = document.createElement('div');

	        // Set chat bubble color and position based on the user parameter
	        if (user === watson) {
	            bubble.className = 'bot_message';  // Bot text formatting
	            bubble.innerHTML = "<div class='bot'>" + text + "</div>";
	        } else {
	            bubble.className = 'user_message';  // User text formatting
	            bubble.innerHTML = "<div class='user'>" + text + "</div>";
	        }

	        chat.appendChild(bubble);
	        chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	    }

	}
	
	return chatReady;
});