define(["jco/collectParameters","jco/collectOptions"], function (collectParameters,collectOptions) {
	
	function displayMessage(text, user) {
		
		var watson = 'Bot';

	    if (text && text != "") {
	        var chat = document.getElementById('chatBox');
	        var bubble = document.createElement('div');

	        // Set chat bubble color and position based on the user parameter
	        if (user === watson) {
	            bubble.className = 'bot_message';  // Bot text formatting
	            bubble.innerHTML = "<div class='bot'>" + text + "</div>";
	        } else {
	            bubble.className = 'user_message';  // User text formatting
	            bubble.innerHTML = "<div class='user'>" + text + "</div>";
	        }

	        chat.appendChild(bubble);
	        chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	    }

	    return null;
	}

	function displayImage(url) {

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


var handleBotReply = function (reply) {
		if (reply != null) {
   		 if (reply.hasOwnProperty('error')) {
     			    if (reply.error == "session invalid") {
     			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
     			    }
     			    else {
   		    	     displayMessage(reply.error, 'Bot');           		    	
   		            } 
     	 } else { 
     		  var activity = undefined;
     		  var operation = undefined;
     		  var operationdata = undefined;
     		  var operationstatus = undefined;
     		  var appdata = undefined;
       		  var responsetype = undefined;
       		  var topindex = undefined;

     		  if (reply.hasOwnProperty('assistantdata') && reply.assistantdata.hasOwnProperty('context') &&
     				 reply.assistantdata.hasOwnProperty('output')) {   	
   			     
  			     if (reply.assistantdata.context.hasOwnProperty('activity')) {
    			     activity = reply.assistantdata.context.activity;
    			 }
  			     
   			     if (reply.assistantdata.context.hasOwnProperty('operation')) {
   			    	 operation = reply.assistantdata.context.operation;
   			     }
   			     
   			     if (reply.assistantdata.context.hasOwnProperty('operationdata')) {
   			    	 operationdata = reply.assistantdata.context.operationdata;
   			     }
   			     
 			     if (reply.assistantdata.context.hasOwnProperty('operationstatus')) {
    			     operationstatus = reply.assistantdata.context.operationstatus;
    			 } 
 			     
 			    if (reply.hasOwnProperty('appdata')) {
   			         appdata = reply.appdata;
   			     } else {
   			    	 appdata = {};
   			     }
   			     			     
   			     if	(reply.assistantdata.output.generic[0]) {
		           topindex = reply.assistantdata.output.generic.length - 1;
		           responsetype = reply.assistantdata.output.generic[topindex].response_type;		    	 
   			     }
   			     
   			     // Now process bot activity-operation requests
   			     // If no activity-operation, then handle as plain bot dialogues
   			     
   			    if ((activity == undefined) && (operation == undefined)) {
   			    	if (responsetype == "text") {
  		   			     displayMessage(reply.assistantdata.output.generic[topindex].text, 'Bot');
  		   			}
   			    	else if ((responsetype == "option") && 
	   		   		   reply.assistantdata.hasOwnProperty('context')) {
	   		   			     collectOptions(reply.assistantdata,
	   		   			    		  function(reply){handleBotReply(reply)});
	   		   	    }
   			    }
   			    
   			     // If incomplete activity-operation, then process them 
   			     // If complete then handle as plain bot dialogues
   			     
   			     if ((activity != undefined) && (operation != undefined)) {
   			    	 if ((operation == 'collectparameters') && 
   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    			 collectParameters(reply.assistantdata,appdata,
   			    					function(reply){handleBotReply(reply)});  			    					 
   			    	 }
   			    	 else if ((operation == 'showresults') && 
   	   			    	    (operationdata != undefined) && (operationstatus != 'complete')){
   			    		     displayResults(reply.assistantdata,
			    					function(reply){handleBotReply(reply)});  	  					 
   	   			     }
   			    	 else {
   	  			    	if (responsetype == "text") {
     		   			     displayMessage(reply.assistantdata.output.generic[topindex].text, 'Bot');
     		   			}
      			    	else if (responsetype == "option") {
   	   		   			      collectOptions(reply.assistantdata,
   	   		   			    		  function(reply){handleBotReply(reply)});
   	   		   	        }
   			         }			     
   			      }			     
     		   } else {
     			  displayMessage(reply.error, 'Bot');      
     		   }
            }
   		}
     }
     return handleBotReply;
});
 