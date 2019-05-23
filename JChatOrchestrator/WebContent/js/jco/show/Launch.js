define(["jco/display/botMessage","jco/display/userMessage","jco/utils/uuid"],  
		  function (displayBotMessage,displayUserMessage,uuid) {
	
var Launch = function (name,assistantdata,appdata,handler) {
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantdata.context.skills['main skill'].user_defined.activity;
	  var operationdata = assistantdata.context.skills['main skill'].user_defined.operationdata;
	  var output = assistantdata.output.generic;
	  var formname = "show" + uuid();
	  
      var urlParams = new URLSearchParams(window.location.search);
      var chatid='unknown';
      if (urlParams.has('chatid')) {chatid = urlParams.get('chatid');}

	  
	  for (var index in output) {
		   if (output[index].response_type == 'text') {
			   displayBotMessage(output[index].text)
		   }
	  }
	  
	  displayForm(chat,formname,appdata);
	  
	     $('form#'+formname).on('submit',{'chatid':chatid,'dataform':formname},function(event) {
	    	 event.preventDefault();
	    	 
	    	 // launch popup for user to view the data
       	     var url = new URL(window.location.origin + "/JChatOrchestrator/showstart");  
       	     url.searchParams.append("name",name);
       	     url.searchParams.append("chatid",event.data['chatid']);
     	     url.searchParams.append("showid",uuid());
     	     url.searchParams.append("dataform",event.data['dataform']);
	    	 var popup = window.open(url,'_blank','location=no,scrollbars=yes,left=200,height=1200,width=1200');        
			   var popupClosed = setInterval(function () {
				    if (popup.closed) {
				        clearInterval(popupClosed);
				    }
				  }, 1000);
			   
			 // send reply to assistant to carry on a new conversation
			   var data = new Object();

			  // important to resend the location.search as the uuid is used to decode lastreply by server
			   var chatpath = window.location.pathname;
			   var $chaturl = window.location.origin + "/JChatOrchestrator/chat/" + document.title + window.location.search;

			  	  data.assistantdata = new Object();
			  	  data.assistantdata.input = 'What can i do';
			  	  data.assistantdata.contextinput = {activity  : '',operation:'',operationdata:'',operationstatus:''};
			  	  data.appdata = new Object();

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
			      });

}

function displayForm(chat,name,appdata)  {
    var form = document.createElement('form');
    form.id = name; 
    chat.appendChild(form);
    form.setAttribute('appdata',JSON.stringify(appdata));
    
    var send =  document.createElement('button');
    send.innerHTML = 'View';
    form.appendChild(send);
  
    
}

return Launch;

});