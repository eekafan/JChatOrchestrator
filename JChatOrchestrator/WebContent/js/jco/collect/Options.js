define (["jco/display/botMessage","jco/display/Radio"],function (displayBotMessage,displayRadio) {


var Options = function (assistantdata,handler) {
	// The bot will prompt for options and maybe extract a context defaultoption
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var context = assistantdata.context;
	  var output = assistantdata.output.generic;

	  for (var index in output) {
	   if (output[index].response_type == 'text') {
		   displayBotMessage(output[index].text)
	   }
	   if (output[index].response_type == 'option') {
		    displayBotMessage(output[index].description)
	        var radioname = "radio" + String(context.global.system.turn_count);
	        if (context.hasOwnProperty('skills')) {
	        	displayRadio(radioname,output[index].options,
	        			context.skills['main skill'].user_defined.defaultoption);
	        } else {
	        displayRadio(radioname,output[index].options)
	        }
	   }
	  }
	  chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	  
                $('form#'+radioname).submit(function(event) {
                var data = new Object();
            	event.preventDefault();
            	// important to resend the location.search as the uuid is used to decode lastreply by server
            	var chatpath = window.location.pathname;
            	var $chaturl = window.location.origin + "/JChatOrchestrator/chat/" + document.title + window.location.search;
            	
            	var myRadio = $("input[name="+radioname+"]");
            	data.assistantdata = new Object();
            	data.assistantdata.input = myRadio.filter(":checked").val();
            	// check if client has selected an option before click of send button
            	if (data.assistantdata.input != undefined) {
            	  $.ajax({
            		type: "POST",
            		url: $chaturl,
            		contentType:'application/json',
            		timeout: 30000,
            		data: JSON.stringify(data),
            		beforeSend: function() {
                        $("#emit")[0].reset();       			
            		},
            		complete: function() {},
            		success: function(reply) {handler(reply);},
            		fail: function(data) {}     		
            	   });
            	 }
                });
	  
}

 return Options;

});