define(["jco/display/DatetimeParameter","jco/display/SimplefilterParameter"],
    function (displayDatetimeParameter,displaySimplefilterParameter) {

var ParametersForm = function (chat,name,activity,parameters,appdata)  {
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
return ParametersForm;

});
