define(["jco/display/DatetimePicker","jco/utils/childtable",
	"jco/display/FilterParameter"],
    function (displayDatetimePicker,childtable,displayFilterParameter) {

var ComplexfilterParameter = function (parent,name,filter_fields) {
 	var paramtable = childtable(parent,parent.id +'-table',1,4);
  	paramtable.rows[0].cells[0].innerHTML = name;
  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
  	var cxfilterdiv = document.createElement('div');
  	cxfilterdiv.id = parent.id + '-cxfilter';
  	paramtable.rows[0].cells[1].appendChild(cxfilterdiv);
  	var cxfiltertable = document.createElement('table');
  	cxfiltertable.id = cxfilterdiv.id +'-table';
  	cxfilterdiv.appendChild(cxfiltertable);
  	addFilterRow(cxfiltertable,filter_fields); 	
}

function addFilterRow(tbl,filter_fields) {
		
    var tr = tbl.insertRow();
    for(var j = 0; j < 3; j++){
                var td = tr.insertCell();
    }
    
    var numrows = tbl.rows.length;
	
	var filterdiv = document.createElement('div');
  	filterdiv.id = tbl.id +'-r'+String(numrows-1) + '-filter';
  	tbl.rows[numrows-1].cells[1].appendChild(filterdiv);
  	displayFilterParameter(filterdiv,filter_fields);
  	
  	if (tbl.rows.length > 1) {
  	    var androw =  document.createElement('button');
  	    androw.innerHTML = 'and';
  	    androw.addEventListener("click", function(){
  	  	  
  	    });
  	    var orrow =  document.createElement('button');
  	    orrow.innerHTML = 'or';
  	    orrow.addEventListener("click", function(){
  	    	 
  	    });
  	    tbl.rows[numrows-1].cells[0].appendChild(androw);
  	    tbl.rows[numrows-1].cells[0].appendChild(orrow);
  	    
  	}
  	
    var addrow =  document.createElement('button');
    addrow.innerHTML = '+';
    addrow.addEventListener("click", function(){
  	  addFilterRow(tbl,filter_fields);
    });
    tbl.rows[numrows-1].cells[2].appendChild(addrow);
    
    if (tbl.rows.length > 1) {
    var removerow =  document.createElement('button');
    removerow.innerHTML = '-';
    removerow.addEventListener("click", function(){
    	removeFilterRow(tbl,filter_fields);
    });

    tbl.rows[numrows-1].cells[2].appendChild(removerow);
    while (tbl.rows[numrows-2].cells[2].firstChild) {
    	tbl.rows[numrows-2].cells[2].removeChild(tbl.rows[numrows-2].cells[2].firstChild);
    }
    }
	
}

function removeFilterRow(tbl,filter_fields) {
	
	tbl.deleteRow(-1);

	var numrows = tbl.rows.length;
	
	var addrow =  document.createElement('button');
	    addrow.innerHTML = '+';
	    addrow.addEventListener("click", function(){
	  	  addFilterRow(tbl,filter_fields);
	    });
	tbl.rows[numrows-1].cells[2].appendChild(addrow);
	    
	if (tbl.rows.length > 1) {
	        var removerow =  document.createElement('button');
	        removerow.innerHTML = '-';
	        removerow.addEventListener("click", function(){
	        	  tbl.deleteRow(tbl,filter_fields);
	});
	        
	tbl.rows[numrows-1].cells[2].appendChild(removerow);
	}
	
}

return ComplexfilterParameter;

});