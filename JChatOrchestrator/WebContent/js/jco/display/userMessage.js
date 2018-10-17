define ([], function () {
	
var userMessage = function(text) {

	    if (text && text != "") {
	        var chat = document.getElementById('chatBox');
	        var bubble = document.createElement('div');

	            bubble.className = 'user_message';  // User text formatting
	            bubble.innerHTML = "<div class='user'>" + text + "</div>";

	        chat.appendChild(bubble);
	        chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	    }

	}

	return userMessage;

});