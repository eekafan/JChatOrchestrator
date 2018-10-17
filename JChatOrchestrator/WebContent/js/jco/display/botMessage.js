define ([], function () {
	
var botMessage = function (text) {
  if (text && text != "") {
	var chat = document.getElementById('chatBox');
    var bubble = document.createElement('div');
    bubble.className = 'bot_message';  // Bot text formatting
    bubble.innerHTML = "<div class='bot'>" + text + "</div>";
    chat.appendChild(bubble);	
  }
}

	return botMessage;

});