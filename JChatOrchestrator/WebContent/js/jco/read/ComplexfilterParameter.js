define(["jco/utils/isodate","jco/read/SimplefilterParameter"],
    function (getISO,readSimplefilterParameter) {

	var ComplexfilterParameter = function(domname) {
		 
     var complexfilter = new Array();
	 var tbl = document.getElementById(domname+'-table');
	 for(var j = 0; j < tbl.rows.length; j++){ 
		var rowid = domname+'-table-r'+String(j)+'-filter';
		var component = new Object()
		if (j == 0) {
			component.logic = 'none';
	    } else {
	    	var logicRadio = $("input[name="+rowid+"-logic-radio]");
	    	component.logic = logicRadio.filter(":checked").val();
	    }
		component.filter = readSimplefilterParameter(rowid);
		complexfilter.push(component);
	 }
	 return complexfilter;
}
	


return ComplexfilterParameter;

});