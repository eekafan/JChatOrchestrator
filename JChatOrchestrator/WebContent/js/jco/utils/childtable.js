define ([],function () {
	var childtable = function(parent,name,rows,columns){
	    var tbl  = document.createElement('table');
	    for(var i = 0; i < rows; i++){
	        var tr = tbl.insertRow();
	        for(var j = 0; j < columns; j++){
	                var td = tr.insertCell();
	        }
	    }
	    tbl.id = name;
	    parent.appendChild(tbl);
	    return tbl;
	}
	
	return childtable;
});