define (["jco/show/seasonalevents/entriesPane"],function (entriesPane) {
    var Ready = function () {  		
    	 var urlParams = new URLSearchParams(window.location.search);
    	 if (urlParams.has('dataform')) {		 
    	   var launchdataform = window.opener.document.getElementById(urlParams.get('dataform'));
    	   var appdata = JSON.parse(launchdataform.getAttribute('appdata'));
    	   var firedafter = document.getElementById('FiredAfterDatetime');
    	   firedafter.innerHTML = appdata.parameters[0]['startdate']['sql'];
 
			  // important to resend the location.search as the uuid is used to decode lastreply by server
           
    	   
			   var $showurl = window.location.origin + "/JChatOrchestrator/show/seasonalevents";
			   var data = new Object();
			  	  data.appdata = appdata;
			  	  
            
			  	    $.ajax({
			  		 type: "POST",
			  	 	 url: $showurl,
			  		 contentType:'application/json',
			  		 timeout: 30000,
			  		 data: JSON.stringify(data),
			  		 beforeSend: function() {},
			  		 complete: function() {},
			  		 success: function(reply) {entriesHandler(reply);},
			  		 fail: function(data) {}     		
			  	     }); 	   
    	 } 
     }	
    
 
   function entriesHandler (reply) {
		if (reply != null) {
   		 if (reply.hasOwnProperty('error')) {
     			     window.location.href = "../JChatOrchestrator/connectioninvalid.html";
     	 } else if (reply.hasOwnProperty('appdata')){
     		  var appdata = reply.appdata;
     		  if (appdata.hasOwnProperty('result_rows')) {
     			 entriesPane(appdata.result_rows);
              }
 
         }
       }
    }
    
	return Ready;
});