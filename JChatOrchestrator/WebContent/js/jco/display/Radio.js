define ([], function () {
	
 var Radio = function (name,options,defaultoption) {
	var chat = document.getElementById('chatBox');
    var radioform = document.createElement('form');
        radioform.id = name;
        chat.appendChild(radioform);
    for (var index in options) {
    	var input = document.createElement('input');
    	input.type = 'radio';
    	input.name = name;
        input.value = options[index].value.input.text;
        if ((defaultoption != undefined) && (input.value == defaultoption)){
        	input.checked = true;
        }	            
        var label = document.createElement('label');
        label.innerHTML = options[index].label;
        label.className = 'bot_message'; 
        radioform.appendChild(input);
        radioform.appendChild(label);
        radioform.appendChild(document.createElement('br'));
    }
    var radiosend =  document.createElement('button');
        radiosend.innerHTML = 'Send';
        radioform.appendChild(radiosend);
    }

 return Radio;
 
});