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

function displayOptions(context,latest,handleBotReply) {
	
	  //naming convention - if the output.generic[latest].title matches a context variable
      // then the client will select a value for that option
	  // if the context property value of that variable is not empty then use it as default
	  // selected item.
	  var contextvariable = undefined;
	  var defaultselected = undefined;
	  if (context.hasOwnProperty(latest.title)) {
	      contextvariable = latest.title;
	      if (context[contextvariable] != "") {
	    	  defaultselected = context[contextvariable];
	      }
	  }
	
	  if (latest.options) {
	        var chat = document.getElementById('chatBox');
	        var bubble = document.createElement('div');
	            bubble.className = 'bot_message';  // Bot text formatting
	            bubble.innerHTML = "<div class='bot'>" + latest.description + "</div>";
	            chat.appendChild(bubble);

	        var radioform = document.createElement('form');
	        var radioname = "radio" + String(context.system.dialog_turn_counter);
	            radioform.id = radioname;
	            chat.appendChild(radioform);
            for (var index in latest.options) {
            	var input = document.createElement('input');
            	input.type = 'radio';
            	input.name = radioname;
	            input.value = latest.options[index].value.input.text;
	            if ((defaultselected != undefined) && (input.value == defaultselected)){
	            	input.checked = true;
	            }
	            
	            var label = document.createElement('label');
	            label.innerHTML = latest.options[index].label;
	            label.className = 'bot_message'; 
	            radioform.appendChild(input);
	            radioform.appendChild(label);
	            radioform.appendChild(document.createElement('br'));
	        }
            var radiosend =  document.createElement('button');
                radiosend.innerHTML = 'Send';
                radioform.appendChild(radiosend);
                
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
            	  data.action = 'selectoption';
            	  if (context.hasOwnProperty(latest.title)) {
            	      data.contextvariable = latest.title;
            	  }
            	  $.ajax({
            		type: "POST",
            		url: $chaturl,
            		contentType:'application/json',
            		timeout: 30000,
            		data: JSON.stringify(data),
            		beforeSend: function() {
                        $("#emit")[0].reset();       			
            		},
            		complete: function() {          			
            		},
            		success: function(reply) {handleBotReply(reply);},
            		fail: function(data) {   
            		}     		
            	   });
            	}
                return false;
            });
	        chat.scrollTop = chat.scrollHeight;  // Move chat down to the last message displayed
	    }

	    return null;
}
