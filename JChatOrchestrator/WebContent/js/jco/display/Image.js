define ([], function () {
	
	var Image = function (url) {

	    if (url) {
	        var image = document.createElement("img");
	        image.src = url;
	        image.alt = url;
	        image.className = 'thumbnail';  // Image formatting

	        document.body.appendChild(image);

	        var chat = document.getElementById('chatBox');
	        chat.appendChild(image);
	        chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	    }

	    return null;
	}
});