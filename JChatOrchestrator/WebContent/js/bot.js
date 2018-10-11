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

function tableCreate(parent,name,rows,columns){
    var tbl  = document.createElement('table');
    for(var i = 0; i < rows; i++){
        var tr = tbl.insertRow();
        for(var j = 0; j < columns; j++){
                var td = tr.insertCell();
        }
    }
    tbl.id = name;
    parent.appendChild(tbl);
    return tbl;
}

function displayDatetimeParameter(parent,parameter) {
 	var paramtable = tableCreate(parent,parent.id +'table',1,3);
  	paramtable.rows[0].cells[0].innerHTML = parameter.name;
  	var datediv = document.createElement('div');
  	datediv.id = parent.id + 'datecontainer';
  	var dateinput = document.createElement('input');
 	paramtable.rows[0].cells[1].appendChild(datediv);
 	dojo.place('<input id="'+parent.id+'dateinput"></input>',document.getElementById(datediv.id));
  	var timediv = document.createElement('div');
 	timediv.id = parent.id + 'timecontainer';
  	var timeinput = document.createElement('input');
  	paramtable.rows[0].cells[2].appendChild(timediv); 
  	dojo.place('<input id="'+parent.id+'timeinput"></input>',document.getElementById(timediv.id));
  	var now = new Date();
  	var start = new Date(now.getTime()-(now.getMilliseconds()/1000));
  	var dateBox = new dijit.form.DateTextBox({name: parent.id+'dateinput', value: start,
         constraints: {
         datePattern: 'dd-MM-yyyy',
         }
       }, parent.id+'dateinput');
    dateBox.startup();
    var timeBox = new dijit.form.TimeTextBox({name: parent.id+'timeinput', value: start,
         constraints: {
         timePattern: 'HH:mm',
         clickableIncrement: 'T01:00:00',
         visibleIncrement: 'T01:00:00',
         visibleRange: 'T01:00:00'
         }
    }, parent.id+'timeinput');
    timeBox.startup();
}

  function displaySimplefilterParameter(parent,parameter,filter_fields) {
 	var paramtable = tableCreate(parent,parent.id +'table',1,4);
  	paramtable.rows[0].cells[0].innerHTML = parameter.name;
  	var fieldsdiv = document.createElement('div');
  	fieldsdiv.id = parent.id + 'fieldscontainer';
  	var fieldselect = document.createElement('input');
  	fieldselect.id = parent.id + 'fieldselect';
  	fieldsdiv.appendChild(fieldselect);
 	paramtable.rows[0].cells[1].appendChild(fieldsdiv);
  	var operatordiv = document.createElement('div');
  	operatordiv.id = parent.id + 'operatorcontainer';
  	var operatorselect = document.createElement('input');
  	operatorselect.id = parent.id + 'operatorselect';
  	operatordiv.appendChild(operatorselect);
 	paramtable.rows[0].cells[2].appendChild(operatordiv);
  	var valuediv = document.createElement('div');
  	valuediv.id = parent.id + 'valuecontainer';
  	var valueinput = document.createElement('input');
  	valueinput.id = parent.id + 'valueinput';
  	valuediv.appendChild(valueinput);
 	paramtable.rows[0].cells[3].appendChild(valuediv);
 	
 	fieldStore.setData(filter_fields);
 	var fieldFilterSelect = new dijit.form.FilteringSelect({
        name: fieldselect.id,
        placeHolder: "Select a Field",
        store: fieldStore,
        searchAttr: "name"
        }, fieldselect.id);
        fieldFilterSelect.startup();
}



function displayParametersForm(chat,name,activity,parameters,appdata)  {
    var dpform = document.createElement('form');
    dpform.id = name;
    chat.appendChild(dpform);
    
    for (var index in parameters) {
    	var paramdiv = document.createElement('div');
    	paramdiv.id = name + String(index);   	
    	dpform.appendChild(paramdiv);
    	if (parameters[index].type == 'datetime') {        	
    		displayDatetimeParameter(paramdiv,parameters[index]);
    	} 	
    	if ((parameters[index].type == 'simplefilter') && (appdata.hasOwnProperty('filter_fields'))){        	
    		displaySimplefilterParameter(paramdiv,parameters[index],appdata.filter_fields);
    	} 
    } 
    var dpsend =  document.createElement('button');
    dpsend.innerHTML = 'Send';
    dpform.appendChild(dpsend);
}

function readParametersForm(name,parameters)  {
	var searchparameters = new Array();
	
    for (var index in parameters) {
    	if (parameters[index].type == 'datetime') {  
    	    var localdate = new Date(dijit.byId(name+String(index)+'dateinput').get('value'));
       	    var localtime = new Date(dijit.byId(name+String(index)+'timeinput').get('value'));
       	    var localdatetime = Math.floor((localdate.getTime() + 
       			 localtime.getTime() - (localdate.getTimezoneOffset()*60*1000))/1000);
       	    var searchparameter = new Object();
    		searchparameter[parameters[index].name] = localdatetime;
    		searchparameters.push(searchparameter);
    	} 	
       	if (parameters[index].type == 'simplefilter') { 
    	    var field = dijit.byId(name+String(index)+'fieldselect').item.value;
       	    var searchparameter = new Object();
    		searchparameter[parameters[index].name] = field+"=''";
    		searchparameters.push(searchparameter);
    	} 	
    } 
   return searchparameters;
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



function displayOptions(assistantreply,handler) {
	// The bot will prompt for options and maybe extract a context defaultoption
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var context = assistantreply.context;
	  var output = assistantreply.output.generic;

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
            		success: function(reply) {handler(reply);},
            		fail: function(data) {}     		
            	   });
            	 }
                });
	  
	   return null;
}

function displayCollectParameters(assistantreply,appdata,handleBotReply) {
	// The bot will prompt for parameters and maybe provide a context variable default
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantreply.context.activity;
	  var operationdata = assistantreply.context.operationdata;
	  var output = assistantreply.output.generic;
	  var dpname = "dp" + String(assistantreply.context.system.dialog_turn_counter);
	  
	  for (var index in output) {
		   if (output[index].response_type == 'text') {
			   displayBotMessage(chat,output[index].text)
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

  	  data.input = 'send parameters';
  	  data.contextinput = {operationstatus : 'complete'};
  	  data.appdata = new Object();

  	  data.appdata['parameters'] = readParametersForm(event.data['dpname'],event.data['operationdata']);
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

function displayResults(assistantreply,handleBotReply) {
	// The bot will prompt for parameters and maybe provide a context variable default
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantreply.context.activity;
	  var operationdata = assistantreply.context.operationdata;
	  var output = assistantreply.output.generic;
	  var drname = "dr" + String(assistantreply.context.system.dialog_turn_counter);
	  for (var index in output) {
		   if (output[index].response_type == 'text') {
			   displayBotMessage(chat,output[index].text)
		   }
	  }

	  
	   return null;
}

