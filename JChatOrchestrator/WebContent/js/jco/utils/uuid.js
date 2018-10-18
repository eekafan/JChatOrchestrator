define ([],function () {
    var uuid = function () {  

    	       var new_uuid = "", i, random;
    	        for (i = 0; i < 32; i++) {
    	        random = Math.random() * 16 | 0;

    	        if (i == 8 || i == 12 || i == 16 || i == 20) {
    	        new_uuid += "-"
    	        }
    	        new_uuid += (i == 12 ? 4 : (i == 16 ? (random & 3 | 8) : random)).toString(16);
    	        }
    	        return new_uuid;
     }
	
	return uuid;
});