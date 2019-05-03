define(["jco/utils/isodate"],
    function (getISO) {

var DatetimeParameter = function (domname,domindex,parname) {
    var localdate = new Date(dijit.byId(domname+String(domindex)+'-pickerdateinput').get('value'));
	var localtime = new Date(dijit.byId(domname+String(domindex)+'-pickertimeinput').get('value'));
	var localdatetime_epoch = Math.floor((localdate.getTime() + 
			 localtime.getTime() - (localdate.getTimezoneOffset()*60*1000))/1000);
	var localdatetime = new Date(localdatetime_epoch*1000);  
	var parameter = new Object();
    parameter[parname] = {iso:getISO(localdatetime),
				utc:localdatetime.toJSON(),epoch:localdatetime_epoch,
	    		sql: localdatetime.toISOString().split('T')[0]+' '+localdatetime.toTimeString().split(' ')[0]};
    return parameter;
}

return DatetimeParameter;

});
