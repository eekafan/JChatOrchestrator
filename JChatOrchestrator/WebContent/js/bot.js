/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 * This file contains all of the web and hybrid functions for interacting with 
 * the basic chat bot output.
 * Leveraged from: https://github.com/sharpstef/watson-bot-starter
 */

"use strict";

var watson = 'Bot';


/**
 * @summary Display Chat Bubble.
 *
 * Formats the chat bubble element based on if the message is from the user or from Bot.
 *
 * @function displayMessage
 * @param {String} text - Text to be displayed in chat box.
 * @param {String} user - Denotes if the message is from Bot or the user. 
 * @return null
 */

function displayBotMessage(chat,text) {
    var bubble = document.createElement('div');
    bubble.className = 'bot_message';  // Bot text formatting
    bubble.innerHTML = "<div class='bot'>" + text + "</div>";
    chat.appendChild(bubble);	
}

function displayRadio(chat,name,options,defaultoption) {
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

function displayParametersForm(chat,name,parameters)  {
    var dpform = document.createElement('form');
    dpform.id = name;
    chat.appendChild(dpform);
    
    var dpsend =  document.createElement('button');
    dpsend.innerHTML = 'Send';
    dpform.appendChild(dpsend);
}

function displayMessage(text, user) {

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



function displayOptions(context,output,handleBotReply) {
	// The bot will prompt for options and maybe extract a context defaultoption
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');

	  for (var index in output) {
	   if (output[index].response_type == 'text') {
		   displayBotMessage(chat,output[index].text)
	   }
	   if (output[index].response_type == 'option') {
		    displayBotMessage(chat,output[index].description)
	        var radioname = "radio" + String(context.system.dialog_turn_counter);
	        if (context.hasOwnProperty("defaultoption")) {
	        	displayRadio(chat,radioname,output[index].options,context.defaultoption);
	        } else {
	        displayRadio(chat,radioname,output[index].options)
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
            	data.input = myRadio.filter(":checked").val();
            	// check if client has selected an option before click of send button
            	if (data.input != undefined) {
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
            		success: function(reply) {handleBotReply(reply);},
            		fail: function(data) {}     		
            	   });
            	 }
                });
	  
	   return null;
}

function displayCollectParameters(context,output,handleBotReply) {
	// The bot will prompt for parameters and maybe provide a context variable default
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var activity = context.activity;
	  var operationdata = context.operationdata;
	  var dpname = "dp" + String(context.system.dialog_turn_counter);
	  
	  displayParametersForm(chat,dpname,activity,operationdata);
	  chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	  
      $('form#'+dpname).submit(function(event) {
      var data = new Object();
  	  event.preventDefault();
  	  // important to resend the location.search as the uuid is used to decode lastreply by server
  	  var chatpath = window.location.pathname;
  	  var $chaturl = window.location.origin + "/JChatOrchestrator/chat/" + document.title + window.location.search;

  	  data.input = 'sendparameters';
  	  data.contextinput = {operationstatus : 'complete'};
  	  // check if client has selected an option before click of send button
  	   if (data.input != undefined) {
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
  		 success: function(reply) {handleBotReply(reply);},
  		 fail: function(data) {}     		
  	     });
  	    }
      });

return null;
}

function displayGrid(context,output,handleBotReply) {
	// The bot will prompt for parameters and maybe provide a context variable default
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var activity = context.activity;
	  var operationdata = context.operationdata;

	  console.log(operationdata);
	  
	   return null;
}

