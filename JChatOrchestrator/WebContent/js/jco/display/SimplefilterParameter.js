define(["jco/display/DatetimePicker","jco/utils/childtable",
	"dojo/dom-construct","dijit/form/TextBox","dijit/form/DateTextBox","dijit/form/TimeTextBox",
    "dijit/form/FilteringSelect","dojo/store/Memory"],
    function (displayDatetimePicker,childtable,
    		domconstruct,textbox,datetextbox,timetextbox,filteringselect,memory) {

var SimplefilterParameter = function (parent,name,filter_fields) {
	 	var paramtable = childtable(parent,parent.id +'table',1,4);
	  	paramtable.rows[0].cells[0].innerHTML = name;
	  	paramtable.rows[0].cells[0].style ='width:90px;max-width:90px;overflow:hidden';
	  	var fieldselect = document.createElement('div');
	  	fieldselect.id = parent.id + 'fieldselect';
	 	paramtable.rows[0].cells[1].appendChild(fieldselect);
	  	var operatorselect = document.createElement('div');
	  	operatorselect.id = parent.id + 'operatorselect';
	  	operatorselect.style.width = '15px';
	 	paramtable.rows[0].cells[2].appendChild(operatorselect);
	  	var valuediv = document.createElement('div');
	  	valuediv.id = parent.id + 'valuediv';
	 	paramtable.rows[0].cells[3].appendChild(valuediv);
	 	
	    var opsA = ([{name:"=",id:"="},{name:"<>",id:"<>"},
	    	{name:">=",id:">="},
	        {name:">",id:">"},{name:"<=",id:"<="},
	        {name:"<",id:"<"}]);

	    var opsB = ([{name:"like",id:"like"},{name:"not like",id:"not like"},
	    	{name:"=",id:"="},{name:"<>",id:"<>"},
	    	{name:">=",id:">="},
	        {name:">",id:">"},{name:"<=",id:"<="},
	        {name:"<",id:"<"}]);
	 	
		var fieldStore = new memory({data:[],idproperty:"name"});
		var operatorStore = new memory({data:[],idproperty:"name"});
	 	fieldStore.setData(filter_fields);
	 	var fieldFilterSelect = new filteringselect({
	 		id: fieldselect.id,
	        name: fieldselect.id,
	        placeHolder: "Please select a field",
	        store: fieldStore,
	        searchAttr: "name",
	        onChange:function(value) {
	        	
	        	if (dijit.byId(valuediv.id + '-pickerdateinput')) {
	        		dijit.byId(valuediv.id + '-pickerdateinput').destroyRecursive();
	        	}
	        	if (dijit.byId(valuediv.id + '-pickertimeinput')) {
	        		dijit.byId(valuediv.id + '-pickertimeinput').destroyRecursive();
	        	}
	        	if (dijit.byId(valuediv.id + '-textinput')) {
	        		dijit.byId(valuediv.id + '-textinput').destroyRecursive();
	        	}
	        	
	       	    if (document.getElementById(valuediv.id + '-picker')) {
	      		  var currentpickerdiv = document.getElementById(valuediv.id + '-picker');
	      		  valuediv.removeChild(currentpickerdiv);
	      	    }
	       	    
	     	    if (document.getElementById(valuediv.id + '-textcontainer')) {
	        		  var textdiv = document.getElementById(valuediv.id + '-textcontainer');
	        		  valuediv.removeChild(textdiv);
	        	}
	        	    
	        	if (this.item.type == "CHARACTER VARYING") {
	        	  operatorStore.setData(opsB);
	        	  dijit.byId(operatorselect.id).set('value','like');
	        	  displayTextinput(valuediv);
	        	}
	        	if (this.item.type == "INTEGER"){
	          	  operatorStore.setData(opsA);
	          	  dijit.byId(operatorselect.id).set('value','=');
	          	  displayTextinput(valuediv);
	          	}
	        	if (this.item.type == "TIMESTAMP"){
	              operatorStore.setData(opsA);
	              dijit.byId(operatorselect.id).set('value','=');  
	     		  var pickerdiv = document.createElement('div');
	          	  pickerdiv.id = valuediv.id +'-picker';
	          	  valuediv.appendChild(pickerdiv);
	              displayDatetimePicker(pickerdiv);
	            }
	        }
	        }, fieldselect.id);
	        fieldFilterSelect.startup();
	        
	        operatorStore.setData(opsA);
	    var operatorFilterSelect =  new filteringselect({
	 		id: operatorselect.id,
	        name: operatorselect.id,
	        store: operatorStore,
	        style: "width:90px",
	        value: "=",
	        searchAttr: "name"
	        }, operatorselect.id);
	        operatorFilterSelect.startup();
}

function displayTextinput(parent) {
	var textdiv = document.createElement('div');
  	textdiv.id = parent.id + '-textcontainer';
  	parent.appendChild(textdiv);
 	domconstruct.place('<input id="'+parent.id+'-textinput"></input>',document.getElementById(textdiv.id));
 	var myTextBox = new dijit.form.TextBox({
        name: "firstname",
        value: "",
        placeHolder: "Enter your value here"
    }, parent.id+"-textinput");
}

return SimplefilterParameter;

});