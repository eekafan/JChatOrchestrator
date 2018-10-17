define(["jco/display/botMessage","dojo/dom-construct",
	"dijit/form/TextBox","dijit/form/DateTextBox","dijit/form/TimeTextBox",
    "dijit/form/FilteringSelect","dojo/store/Memory"],
    function (displayBotMessage,domconstruct,textbox,datetextbox,timetextbox,filteringselect,memory) {
	
var relatedEvents = function (assistantdata,appdata,handler) {
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantdata.context.activity;
	  var operationdata = assistantdata.context.operationdata;
	  var output = assistantdata.output.generic;
	  var relname = "showre" + String(assistantdata.context.system.dialog_turn_counter);
	  
	  for (var index in output) {
		   if (output[index].response_type == 'text') {
			   displayBotMessage(output[index].text)
		   }
	  }
	  
	  displayForm(chat,relname,activity,operationdata,appdata);
	  
	     $('form#'+rename).on('submit',{'name':relname,'appdata':appdata},function(event) {
	    	 event.preventDefault();
	    	 var thischat = window.open(url,'_blank','location=no,scrollbars=yes,left=500,height=800,width=650');        
			   var thischatClosed = setInterval(function () {
				    if (thischat.closed) {
				        clearInterval(thischatClosed);
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

return relatedEvents;

});