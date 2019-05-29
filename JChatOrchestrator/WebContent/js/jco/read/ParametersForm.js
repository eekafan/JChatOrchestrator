define(["jco/read/DatetimeParameter","jco/read/SimplefilterParameter","jco/read/ComplexfilterParameter"],
    function (readDatetimeParameter,readSimplefilterParameter,readComplexfilterParameter) {

var ParametersForm = function (formname,parameters)  {
	var searchparameters = new Array();
	
    for (var index in parameters) {
    	var parameter = new Object();
    	if (parameters[index].type == 'datetime') {    		
            parameter[String(parameters[index].name)] = readDatetimeParameter(formname+'-p'+String(index));

    	} 	
       	if (parameters[index].type == 'simplefilter') { 
       		parameter[String(parameters[index].name)] = readSimplefilterParameter(formname+'-p'+String(index)+'-filter');
    	} 	
     	if (parameters[index].type == 'complexfilter') { 
       		parameter[String(parameters[index].name)] = readComplexfilterParameter(formname+'-p'+String(index)+'-cxfilter');
    	} 
		searchparameters.push(parameter);
    } 
   return searchparameters;
}
return ParametersForm;

});
