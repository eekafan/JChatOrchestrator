     function uuid() {
       var uuid = "", i, random;
        for (i = 0; i < 32; i++) {
        random = Math.random() * 16 | 0;

        if (i == 8 || i == 12 || i == 16 || i == 20) {
        uuid += "-"
        }
        uuid += (i == 12 ? 4 : (i == 16 ? (random & 3 | 8) : random)).toString(16);
        }
        return uuid;
     }
     
     function handleReply(reply,chat_windows,chat_name) {
		   if (!(reply == null)) {
 		    if (reply.hasOwnProperty('error')) {
   			    if (reply.error == "session invalid") {
   			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";
   			    }
   			    else {
   			     // also invalidate if any other webserver errors indicated
   			     window.location.href = "../JChatOrchestrator/chatsessioninvalid.html";          		    	
 		        } 
   		    } else { 
                if (chat_windows.length < 3) {
             	   var url = new URL(window.location.origin + "/JChatOrchestrator/chatstart");
             	   url.searchParams.append("name",chat_name);
             	   url.searchParams.append("uuid",uuid());
             	   var thischat = window.open(url,'_blank','location=no,scrollbars=yes,left=500,height=800,width=450');          	
 				   chat_windows.push(thischat);
 				   var thischatClosed = setInterval(function () {
 					    if (thischat.closed) {
 					        clearInterval(thischatClosed);
 					        chat_windows.pop(thischat);
 					    }
 					  }, 1000);
                }
   		    }  
 		   }
 		}

       $(document).ready(function() {
        	var chat_windows = new Array();
        	
        	var $listButtons = $('.launcher-button-input');
        	$listButtons.each(function(){ 
                var $this = $(this);
                var chat_name = $this.attr('id');
                $this.click(function(e){
            	e.preventDefault();
            	// test if session has expired
            	var chatpath = window.location.pathname;
            	var $chaturl = window.location.origin + "/JChatOrchestrator/launchsessioncheck";
            	$.ajax({
            		type: "GET",
            		url: $chaturl,
            		contentType:'application/json',
            		timeout: 30000,
            		beforeSend: function() {      			
            		},
            		complete: function() {          			
            		},
            		success: function(reply) { handleReply(reply,chat_windows,chat_name); },
            		fail: function(reply) {   
            		}     		
            	});
            	return false;
              });
        	});
        	
        	$('#logout').click(function(e){
            	e.preventDefault();
           		for (var w in chat_windows) {
            		chat_windows[w].close();
            	}
           	    window.location.href = window.location.origin + "/JChatOrchestrator/logout";	
           	 return false;
        	});
        });