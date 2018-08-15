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

       $(document).ready(function() {
        	var chat_windows = new Array();
        	
        	var $listButtons = $('.launcher-button-input');
        	$listButtons.each(function(){ 
                var $this = $(this);
                $this.click(function(e){
            	e.preventDefault();
                if (chat_windows.length < 3) {
            	   var url = new URL(window.location.origin + "/JChatOrchestrator/chatstart");
            	   url.searchParams.append("name",$this.attr('id'));
            	   url.searchParams.append("uuid",uuid());
            	   var thischat = window.open(url,'_blank','location=no,scrollbars=yes,left=500,height=800,width=450');          	
				   chat_windows.push(thischat);
                   }
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