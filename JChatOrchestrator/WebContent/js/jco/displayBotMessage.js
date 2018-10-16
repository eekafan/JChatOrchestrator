define ([], function () {
	
var  displayBotMessage = function (chat,text) {
    var bubble = document.createElement('div');
    bubble.className = 'bot_message';  // Bot text formatting
    bubble.innerHTML = "<div class='bot'>" + text + "</div>";
    chat.appendChild(bubble);	
}

return displayBotMessage;

})