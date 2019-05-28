define(["jco/utils/isodate"],
    function (getISO) {

	var SimplefilterParameter = function(domname) {
		 var field = dijit.byId(domname+'fieldselect').item;
		 var operator = dijit.byId(domname+'operatorselect').item;
		 var value = undefined;
		 if (field.type == "TIMESTAMP"){
		    var localdate = new Date(dijit.byId(domname+'valuediv-pickerdateinput').get('value'));
			    var localtime = new Date(dijit.byId(domname+'valuediv-pickertimeinput').get('value'));
			    var localdatetime_epoch = Math.floor((localdate.getTime() + 
					 localtime.getTime() - (localdate.getTimezoneOffset()*60*1000))/1000);
			    var localdatetime = new Date(localdatetime_epoch*1000);
			    
			    value = {iso:getISO(localdatetime),utc:localdatetime.toJSON(),epoch:localdatetime_epoch,
			    		sql: localdatetime.toISOString().split('T')[0]+' '+localdatetime.toTimeString().split(' ')[0]};
		 }
		 if (field.type == "CHARACTER VARYING") {
			value = "'"+dijit.byId(domname+'valuediv-textinput').get('value')+"'";
		 }
		 if (field.type == "INTEGER") {
			value = dijit.byId(domname+'valuediv-textinput').get('value');
		 }

		 return {field:field.name,operator:operator.name,value:value};
}
	


return SimplefilterParameter;

});