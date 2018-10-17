define(["jco/display/botMessage","dojo/dom-construct",
	"dijit/form/TextBox","dijit/form/DateTextBox","dijit/form/TimeTextBox",
    "dijit/form/FilteringSelect","dojo/store/Memory"],
    function (displayBotMessage,domconstruct,textbox,datetextbox,timetextbox,filteringselect,memory) {
	
var Parameters = function (assistantdata,appdata,handler) {
	// The bot will prompt for parameters and maybe provide a context variable default
	// The client will return the text of the option which can be used as an entity for dialog logic
	// The turn counter is used to uniquely identify the radio form
	
	  var chat = document.getElementById('chatBox');
	  var activity = assistantdata.context.activity;
	  var operationdata = assistantdata.context.operationdata;
	  var output = assistantdata.output.generic;
	  var dpname = "dp" + String(assistantdata.context.system.dialog_turn_counter);
	  
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

function displayTextinput(parent) {
	var textdiv = document.createElement('div');
  	textdiv.id = parent.id + '-textcontainer';
  	parent.appendChild(textdiv);
 	domconstruct.place('<input id="'+parent.id+'-textinput"></input>',document.getElementById(textdiv.id));
 	var myTextBox = new dijit.form.TextBox({
        name: "firstname",
        value: "",
        placeHolder: "Enter your value here"
    }, parent.id+"-textinput");
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

function displayDatetimePicker(parent) {
	if (document.getElementById(parent.id + 'datecontainer')) {
		var datecontainer = document.getElementById(parent.id + 'datecontainer');
		parent.removeChild(datecontainer);
	}
	if (document.getElementById(parent.id + 'timecontainer')) {
		var timecontainer = document.getElementById(parent.id + 'timecontainer');
		parent.removeChild(timecontainer);
	}
  	var datediv = document.createElement('div');
  	datediv.id = parent.id + 'datecontainer';
  	parent.appendChild(datediv);
 	domconstruct.place('<input id="'+parent.id+'dateinput"></input>',document.getElementById(datediv.id));
 	var timediv = document.createElement('div');
 	timediv.id = parent.id + 'timecontainer';
  	parent.appendChild(timediv);
  	domconstruct.place('<input id="'+parent.id+'timeinput"></input>',document.getElementById(timediv.id));
  	var now = new Date();
  	var start = new Date(now.getTime()-(now.getMilliseconds()/1000));
  	var dateBox = new datetextbox({name: parent.id+'dateinput', value: start,
         constraints: {
         datePattern: 'dd-MM-yyyy',
         }
       }, parent.id+'dateinput');
    dateBox.startup();
    var timeBox = new timetextbox({name: parent.id+'timeinput', value: start,
         constraints: {
         timePattern: 'HH:mm',
         clickableIncrement: 'T01:00:00',
         visibleIncrement: 'T01:00:00',
         visibleRange: 'T06:00:00'
         }
    }, parent.id+'timeinput');
    timeBox.startup();
}

function displayDatetimeParameter(parent,name) {
 	var paramtable = tableCreate(parent,parent.id +'table',1,2);
  	paramtable.rows[0].cells[0].innerHTML = name;
  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
  	var pickerdiv = document.createElement('div');
  	pickerdiv.id = parent.id +'-picker';
 	paramtable.rows[0].cells[1].appendChild(pickerdiv);
 	displayDatetimePicker(pickerdiv);
}

function displaySimplefilterParameter(parent,name,filter_fields) {
 	var paramtable = tableCreate(parent,parent.id +'table',1,4);
  	paramtable.rows[0].cells[0].innerHTML = name;
  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
  	var fieldselect = document.createElement('div');
  	fieldselect.id = parent.id + 'fieldselect';
 	paramtable.rows[0].cells[1].appendChild(fieldselect);
  	var operatorselect = document.createElement('div');
  	operatorselect.id = parent.id + 'operatorselect';
  	operatorselect.style.width = '15px';
 	paramtable.rows[0].cells[2].appendChild(operatorselect);
  	var valuediv = document.createElement('div');
  	valuediv.id = parent.id + 'valuediv';
 	paramtable.rows[0].cells[3].appendChild(valuediv);
 	
    var opsA = ([{name:"=",id:"="},{name:"<>",id:"<>"},
    	{name:">=",id:">="},
        {name:">",id:">"},{name:"<=",id:"<="},
        {name:"<",id:"<"}]);

    var opsB = ([{name:"like",id:"like"},{name:"not like",id:"not like"},
    	{name:"=",id:"="},{name:"<>",id:"<>"},
    	{name:">=",id:">="},
        {name:">",id:">"},{name:"<=",id:"<="},
        {name:"<",id:"<"}]);
 	
	var fieldStore = new memory({data:[],idproperty:"name"});
	var operatorStore = new memory({data:[],idproperty:"name"});
 	fieldStore.setData(filter_fields);
 	var fieldFilterSelect = new filteringselect({
 		id: fieldselect.id,
        name: fieldselect.id,
        placeHolder: "Please select a field",
        store: fieldStore,
        searchAttr: "name",
        onChange:function(value) {
        	
        	if (dijit.byId(valuediv.id + '-pickerdateinput')) {
        		dijit.byId(valuediv.id + '-pickerdateinput').destroyRecursive();
        	}
        	if (dijit.byId(valuediv.id + '-pickertimeinput')) {
        		dijit.byId(valuediv.id + '-pickertimeinput').destroyRecursive();
        	}
        	if (dijit.byId(valuediv.id + '-textinput')) {
        		dijit.byId(valuediv.id + '-textinput').destroyRecursive();
        	}
        	
       	    if (document.getElementById(valuediv.id + '-picker')) {
      		  var currentpickerdiv = document.getElementById(valuediv.id + '-picker');
      		  valuediv.removeChild(currentpickerdiv);
      	    }
       	    
     	    if (document.getElementById(valuediv.id + '-textcontainer')) {
        		  var textdiv = document.getElementById(valuediv.id + '-textcontainer');
        		  valuediv.removeChild(textdiv);
        	}
        	    
        	if (this.item.type == "CHARACTER VARYING") {
        	  operatorStore.setData(opsB);
        	  dijit.byId(operatorselect.id).set('value','like');
        	  displayTextinput(valuediv);
        	}
        	if (this.item.type == "INTEGER"){
          	  operatorStore.setData(opsA);
          	  dijit.byId(operatorselect.id).set('value','=');
          	  displayTextinput(valuediv);
          	}
        	if (this.item.type == "TIMESTAMP"){
              operatorStore.setData(opsA);
              dijit.byId(operatorselect.id).set('value','=');  
     		  var pickerdiv = document.createElement('div');
          	  pickerdiv.id = valuediv.id +'-picker';
          	  valuediv.appendChild(pickerdiv);
              displayDatetimePicker(pickerdiv);
            }
        }
        }, fieldselect.id);
        fieldFilterSelect.startup();
        
        operatorStore.setData(opsA);
    var operatorFilterSelect =  new filteringselect({
 		id: operatorselect.id,
        name: operatorselect.id,
        store: operatorStore,
        style: "width:90px",
        value: "=",
        searchAttr: "name"
        }, operatorselect.id);
        operatorFilterSelect.startup();
}



function displayParametersForm(chat,name,activity,parameters,appdata)  {
    var dpform = document.createElement('form');
    dpform.id = name; dpform.className = "dpform";
    chat.appendChild(dpform);
    
    for (var index in parameters) {
    	var paramdiv = document.createElement('div');
    	paramdiv.id = name + String(index);   	
    	dpform.appendChild(paramdiv);
    	if (parameters[index].type == 'datetime') {        	
    		displayDatetimeParameter(paramdiv,parameters[index].name);
    	} 	
    	if ((parameters[index].type == 'simplefilter') && (appdata.hasOwnProperty('filter_fields'))){        	
    		displaySimplefilterParameter(paramdiv,parameters[index].name,appdata.filter_fields);
    	} 
    } 
    var dpsend =  document.createElement('button');
    dpsend.innerHTML = 'Send';
    dpform.appendChild(dpsend);
}

function getISO(localdatetime) {
	var timezone_offset_min = localdatetime.getTimezoneOffset();
	var offset_hrs = parseInt(Math.abs(timezone_offset_min/60));
	var offset_min = Math.abs(timezone_offset_min%60);
	var timezone_standard = undefined;

	if(offset_hrs < 10)
	offset_hrs = '0' + offset_hrs;

	if(offset_min < 10)
	offset_min = '0' + offset_min;

	//Add an opposite sign to the offset
	//If offset is 0, it means timezone is UTC
	if(timezone_offset_min < 0)
	timezone_standard = '+' + offset_hrs + ':' + offset_min;
	else if(timezone_offset_min > 0)
	timezone_standard = '-' + offset_hrs + ':' + offset_min;
	else if(timezone_offset_min == 0)
	timezone_standard = 'Z';
	
	var utcstring = localdatetime.toJSON();
	return utcstring.replace('Z',timezone_standard);
}



function readParametersForm(name,parameters)  {
	var searchparameters = new Array();
	
    for (var index in parameters) {
    	if (parameters[index].type == 'datetime') {  
    	    var localdate = new Date(dijit.byId(name+String(index)+'-pickerdateinput').get('value'));
       	    var localtime = new Date(dijit.byId(name+String(index)+'-pickertimeinput').get('value'));
       	    var localdatetime_epoch = Math.floor((localdate.getTime() + 
       			 localtime.getTime() - (localdate.getTimezoneOffset()*60*1000))/1000);
       	    var localdatetime = new Date(localdatetime_epoch*1000);  
       	    var searchparameter = new Object();
       		searchparameter[parameters[index].name] = {iso:getISO(localdatetime),
       				utc:localdatetime.toJSON(),epoch:localdatetime_epoch,
       	    		sql: localdatetime.toISOString().split('T')[0]+' '+localdatetime.toTimeString().split(' ')[0]};
    		searchparameters.push(searchparameter);
    	} 	
       	if (parameters[index].type == 'simplefilter') { 
    	    var field = dijit.byId(name+String(index)+'fieldselect').item;
    	    var operator = dijit.byId(name+String(index)+'operatorselect').item;
    	    var value = undefined;
        	if (field.type == "TIMESTAMP"){
        	    var localdate = new Date(dijit.byId(name+String(index)+'valuediv-pickerdateinput').get('value'));
           	    var localtime = new Date(dijit.byId(name+String(index)+'valuediv-pickertimeinput').get('value'));
           	    var localdatetime_epoch = Math.floor((localdate.getTime() + 
           			 localtime.getTime() - (localdate.getTimezoneOffset()*60*1000))/1000);
           	    var localdatetime = new Date(localdatetime_epoch*1000);
           	    
           	    value = {iso:getISO(localdatetime),utc:localdatetime.toJSON(),epoch:localdatetime_epoch,
           	    		sql: localdatetime.toISOString().split('T')[0]+' '+localdatetime.toTimeString().split(' ')[0]};
        	}
        	if (field.type == "CHARACTER VARYING") {
        		value = "'"+dijit.byId(name+String(index)+'valuediv-textinput').get('value')+"'";
        	}
           	if (field.type == "INTEGER") {
        		value = dijit.byId(name+String(index)+'valuediv-textinput').get('value');
        	}

       	    var searchparameter = new Object();
       		searchparameter[parameters[index].name] = {field:field.name,operator:operator.name,value:value};
    		searchparameters.push(searchparameter);
    	} 	
    } 
   return searchparameters;
}

return Parameters;

});