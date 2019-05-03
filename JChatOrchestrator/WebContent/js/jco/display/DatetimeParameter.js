define(["jco/display/DatetimePicker"],
    function (displayDatetimePicker) {

var DatetimeParameter = function (parent,name) {
 	var paramtable = tableCreate(parent,parent.id +'table',1,2);
  	paramtable.rows[0].cells[0].innerHTML = name;
  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
  	var pickerdiv = document.createElement('div');
  	pickerdiv.id = parent.id +'-picker';
 	paramtable.rows[0].cells[1].appendChild(pickerdiv);
 	displayDatetimePicker(pickerdiv);
}

function tableCreate(parent,name,rows,columns){
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

return DatetimeParameter;

});
