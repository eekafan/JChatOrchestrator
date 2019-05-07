define(["jco/display/DatetimePicker","jco/utils/childtable",
	"jco/display/SimplefilterParameter",
	"dijit/form/TextBox","dijit/form/DateTextBox","dijit/form/TimeTextBox",
    "dijit/form/FilteringSelect","dojo/store/Memory"],
    function (displayDatetimePicker,childtable,displaySimplefilterParameter,
    		domconstruct,textbox,datetextbox,timetextbox,filteringselect,memory) {

var ComplexfilterParameter = function (parent,name,filter_fields) {
	 	var filtertable = childtable(parent,parent.id +'table',1,4);
	  	filtertable.rows[0].cells[0].innerHTML = name;
	  	filtertable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
    	var paramdiv = document.createElement('div');
    	paramdiv.id = name + 'cfilter'+ String(0);   	
    	parent.appendChild(paramdiv);
	  	displaySimplefilterParameter(paramdiv,name,filter_fields);

}

return ComplexfilterParameter;

});