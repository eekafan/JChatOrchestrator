define(["jco/display/botMessage","jco/utils/uuid"],  function (displayBotMessage,uuid) {
	
var Launch = function (assistantdata,appdata) {
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantdata.context.activity;
	  var operationdata = assistantdata.context.operationdata;
	  var output = assistantdata.output.generic;
	  var relname = "showrel" + String(assistantdata.context.system.dialog_turn_counter);
	  
	  for (var index in output) {
		   if (output[index].response_type == 'text') {
			   displayBotMessage(output[index].text)
		   }
	  }
	  
	  displayForm(chat,relname,activity,operationdata,appdata);
	  
	     $('form#'+relname).on('submit',{'name':relname,'appdata':appdata},function(event) {
	    	 event.preventDefault();
       	   var url = new URL(window.location.origin + "/JChatOrchestrator/showresults/eventanalytics");  
     	     url.searchParams.append("type","relatedevents");
     	     url.searchParams.append("uuid",uuid());
	    	 var show = window.open(url,'_blank','location=no,scrollbars=yes,left=500,height=800,width=650');        
			   var showClosed = setInterval(function () {
				    if (show.closed) {
				        clearInterval(showClosed);
				    }
				  }, 1000);
	     });

}

function displayForm(chat,name,activity,parameters,appdata)  {
    var form = document.createElement('form');
    form.id = name; form.className = "showreform";
    chat.appendChild(form);
    
    var send =  document.createElement('button');
    send.innerHTML = 'View';
    form.appendChild(send);
}

return Launch;

});