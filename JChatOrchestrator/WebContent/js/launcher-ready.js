       $(document).ready(function() {
        	var chat_windows = new Array();
           
            var servletMapping = '/chat';
            
            $('#launch').click(function(e) {
            	e.preventDefault();
            	if (chat_windows.length < 3) {
            	var href = window.location.href;
            	var thischat = window.open("../JChatOrchestrator/chatstart",'_blank','location=no,scrollbars=yes,left=500,height=800,width=450');          	
				chat_windows.push(thischat);
                }
            	return false;
            });
            
            $('#logout').click(function(e) {
            	e.preventDefault();
            	for (var w in chat_windows) {
            		chat_windows[w].close();
            	}
            	window.location.href = "../JChatOrchestrator/logout";
            	return false;
            });

        });