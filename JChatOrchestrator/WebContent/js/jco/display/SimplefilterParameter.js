define(["jco/display/DatetimePicker","jco/utils/childtable","jco/display/FilterParameter",
	"dojo/dom-construct","dijit/form/TextBox","dijit/form/DateTextBox","dijit/form/TimeTextBox",
    "dijit/form/FilteringSelect","dojo/store/Memory"],
    function (displayDatetimePicker,childtable,FilterParameter,
    		domconstruct,textbox,datetextbox,timetextbox,filteringselect,memory) {

var SimplefilterParameter = function (parent,name,filter_fields) {
	 	var paramtable = childtable(parent,parent.id +'-table',1,2);
	  	paramtable.rows[0].cells[0].innerHTML = name;
	  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
	  	var filterdiv = document.createElement('div');
	  	filterdiv.id = parent.id + '-filter';
	  	paramtable.rows[0].cells[1].appendChild(filterdiv);
	  	FilterParameter(filterdiv,filter_fields);
}

return SimplefilterParameter;

});