define(["jco/read/DatetimeParameter","jco/read/SimplefilterParameter"],
    function (readDatetimeParameter,readSimplefilterParameter) {

var ParametersForm = function (name,parameters)  {
	var searchparameters = new Array();
	
    for (var index in parameters) {
    	if (parameters[index].type == 'datetime') {  
    		searchparameters.push(readDatetimeParameter(name,index,parameters[index].name));
    	} 	
       	if (parameters[index].type == 'simplefilter') { 
       		searchparameters.push(readSimplefilterParameter(name,index,parameters[index].name));
    	} 	
    } 
   return searchparameters;
}
return ParametersForm;

});
