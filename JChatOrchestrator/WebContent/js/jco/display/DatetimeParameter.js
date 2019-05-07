define(["jco/display/DatetimePicker","jco/utils/childtable"],
    function (displayDatetimePicker,childtable) {

var DatetimeParameter = function (parent,name) {
 	var paramtable = childtable(parent,parent.id +'table',1,2);
  	paramtable.rows[0].cells[0].innerHTML = name;
  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
  	var pickerdiv = document.createElement('div');
  	pickerdiv.id = parent.id +'-picker';
 	paramtable.rows[0].cells[1].appendChild(pickerdiv);
 	displayDatetimePicker(pickerdiv);
}



return DatetimeParameter;

});
