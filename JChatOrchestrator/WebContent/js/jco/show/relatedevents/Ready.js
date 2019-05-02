define (["jco/show/relatedevents/groupsPane"],function (groupsPane) {
    var Ready = function () {  		
    	 var urlParams = new URLSearchParams(window.location.search);
    	 if (urlParams.has('dataform')) {		 
    	   var launchdataform = window.opener.document.getElementById(urlParams.get('dataform'));
    	   var appdata = JSON.parse(launchdataform.getAttribute('appdata'));
    	   var detectedafter = document.getElementById('DetectedAfterDatetime');
    	   detectedafter.innerHTML = appdata.parameters[0]['startdate']['sql'];
    	   var showname = 'relatedevents'; var chatid='unknown'; var showid = 'unknown';
    	   if (urlParams.has('name')) {showname = urlParams.get('name');}
	       if (urlParams.has('chatid')) {chatid = urlParams.get('chatid');}
	       if (urlParams.has('showid')) {showid = urlParams.get('showid');}
 
			  // important to resend the location.search as the uuid is used to decode lastreply by server
           
    	   
			   var $showurl = window.location.origin + "/JChatOrchestrator/show/"+showname+
	      	     "?chatid="+chatid+"&showid="+showid;
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
			  		 success: function(reply) {groupsHandler(reply);},
			  		 fail: function(data) {}     		
			  	     }); 	   
    	 } 
     }	
    
 
   function groupsHandler (reply) {
		if (reply != null) {
   		 if (reply.hasOwnProperty('error')) {
     			     window.location.href = "../JChatOrchestrator/connectioninvalid.html";
     	 } else if (reply.hasOwnProperty('appdata')){
     		  var appdata = reply.appdata;
     		  if (appdata.hasOwnProperty('result_rows')) {
     			 groupsPane(appdata.result_rows);
              }
 
         }
       }
    }
    
	return Ready;
});