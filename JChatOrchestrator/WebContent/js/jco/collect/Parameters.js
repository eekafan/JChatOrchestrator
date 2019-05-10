define(["jco/display/botMessage","jco/display/ParametersForm","jco/read/ParametersForm","jco/utils/uuid"],
    function (displayBotMessage,displayParametersForm,readParametersForm,uuid) {
	
var Parameters = function (assistantdata,appdata,handler) {
	// The bot will prompt for parameters and maybe provide a context variable default
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantdata.context.skills['main skill'].user_defined.activity;
	  var operationdata = assistantdata.context.skills['main skill'].user_defined.operationdata;
	  var output = assistantdata.output.generic;
	  var dpname = "dp" + uuid();
	  
	  for (var index in output) {
		   if (output[index].response_type == 'text') {
			   displayBotMessage(output[index].text)
		   }
	  }
	  displayParametersForm(chat,dpname,activity,operationdata,appdata);
	  chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
  	  
      $('form#'+dpname).on('submit',{'dpname':dpname,'operationdata':operationdata},function(event) {
      var data = new Object();
  	  event.preventDefault();
  	  // important to resend the location.search as the uuid is used to decode lastreply by server
  	  var chatpath = window.location.pathname;
  	  var $chaturl = window.location.origin + "/JChatOrchestrator/chat/" + document.title + window.location.search;

  	  data.assistantdata = new Object();
  	  data.assistantdata.input = 'send parameters';
  	  data.assistantdata.contextinput = {operationstatus : 'complete'};
  	  data.appdata = new Object();

  	  data.appdata['parameters'] = readParametersForm(event.data['dpname'],event.data['operationdata']);
  	  // check if client has selected an option before click of send button
  	   if (data.appdata.parameters != undefined) {
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

return Parameters;

});