define(["jco/utils/isodate","jco/read/SimplefilterParameter"],
    function (getISO,readSimplefilterParameter) {

	var ComplexfilterParameter = function(domname) {
		 
     var complexfilter = new Array();
	 var tbl = document.getElementById(domname+'-table');
	 for(var j = 0; j < tbl.rows.length; j++){ 
		var component = new Object();
		if (j == 0) {
			component.logic = 'none';
	    } else {
	    	component.logic = 'and'
	    }
		component.filter = readSimplefilterParameter(domname+'-table-r'+String(j)+'-filter');
		complexfilter.push(component);
	 }
	 return complexfilter;
}
	


return ComplexfilterParameter;

});