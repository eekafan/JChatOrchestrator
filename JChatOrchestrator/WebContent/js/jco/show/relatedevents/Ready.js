define (["jco/show/relatedevents/groupsPane"],function (groupsPane) {
    var Ready = function () {  
    	$(document).ready(function() {   		
    	 var urlParams = new URLSearchParams(window.location.search);
    	 if (urlParams.has('dataform')) {		 
    	   var launchdataform = window.opener.document.getElementById(urlParams.get('dataform'));
    	   var appdata = JSON.parse(launchdataform.getAttribute('appdata'));
    	   var firedafter = document.getElementById('FiredAfterDatetime');
    	   firedafter.innerHTML = appdata.parameters[0]['startdate']['sql'];
    	   groupsPane(appdata.result_rows);
    	 } 
    	});
     }	
    
	return Ready;
});