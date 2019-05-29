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

function addLogicRadio(filtercomponentid,parentrow) {
		var andip = document.createElement('input');
    	andip.type = 'radio';
       	andip.name = filtercomponentid+'-logic-radio';
        andip.value = 'and';
        andip.checked = true;
        var andlabel = document.createElement('label');
        andlabel.htmlFor = 'and';
        andlabel.innerHTML='and';
		var orip = document.createElement('input');
    	orip.type = 'radio';
       	orip.name = filtercomponentid+'-logic-radio';
        orip.value = 'or';
        var orlabel = document.createElement('label');
        orlabel.htmlFor = 'or';
        orlabel.innerHTML='or';
        parentrow.cells[0].appendChild(andlabel);
        parentrow.cells[0].appendChild(andip);
        parentrow.cells[0].appendChild(orlabel);
        parentrow.cells[0].appendChild(orip);	
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
  		addLogicRadio(filterdiv.id,tbl.rows[numrows-1]);	    
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
	        	  removeFilterRow(tbl,filter_fields);
	});
	        
	tbl.rows[numrows-1].cells[2].appendChild(removerow);
	}
	
}

return ComplexfilterParameter;

});