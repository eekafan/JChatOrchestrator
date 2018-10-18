define ([],function () {
    var Ready = function () {  
    	$(document).ready(function() {
    		
    		displayForm('test');
    		
    	     $('form#'+'test').on('submit',function(event) {
    	    	 event.preventDefault();
    	    		var urlParams = new URLSearchParams(window.location.search);
    	    		if (urlParams.has('dataform')) {
    	    			var formid = urlParams.get('dataform');
    	        			console.log(urlParams.get('dataform'));
    	        			var launchdataform = window.opener.document.getElementById(urlParams.get('dataform'));
    	        			var appdata = JSON.parse(launchdataform.getAttribute('appdata'));
    	        			console.log(JSON.stringify(appdata));
    	    		} 
   
    	     });
       });
     }
    
    function displayForm(name)  {
        var form = document.createElement('form');
        form.id = name; 
        var chat = document.getElementById('summaryDisplay');
        chat.appendChild(form);
        
        var send =  document.createElement('button');
        send.innerHTML = 'View';
        form.appendChild(send);
        //store appdata in form for child
        
    }
	
	return Ready;
});