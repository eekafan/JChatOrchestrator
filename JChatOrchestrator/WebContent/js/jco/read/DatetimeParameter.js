define(["jco/utils/isodate","dijit/registry"],
    function (getISO,registry) {

var DatetimeParameter = function (domname) {
    var localdate = new Date(registry.byId(domname+'-pickerdateinput').get('value'));
	var localtime = new Date(registry.byId(domname+'-pickertimeinput').get('value'));
	var localdatetime_epoch = Math.floor((localdate.getTime() + 
			 localtime.getTime() - (localdate.getTimezoneOffset()*60*1000))/1000);
	var localdatetime = new Date(localdatetime_epoch*1000);  

    return {iso:getISO(localdatetime),
				utc:localdatetime.toJSON(),epoch:localdatetime_epoch,
	    		sql: localdatetime.toISOString().split('T')[0]+' '+localdatetime.toTimeString().split(' ')[0]};
}

return DatetimeParameter;

});
