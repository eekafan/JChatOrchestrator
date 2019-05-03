define(["dojo/dom-construct",
	"dijit/form/DateTextBox","dijit/form/TimeTextBox"],
    function (domconstruct,datetextbox,timetextbox) {

var DatetimePicker = function(parent) {
	if (document.getElementById(parent.id + 'datecontainer')) {
		var datecontainer = document.getElementById(parent.id + 'datecontainer');
		parent.removeChild(datecontainer);
	}
	if (document.getElementById(parent.id + 'timecontainer')) {
		var timecontainer = document.getElementById(parent.id + 'timecontainer');
		parent.removeChild(timecontainer);
	}
  	var datediv = document.createElement('div');
  	datediv.id = parent.id + 'datecontainer';
  	parent.appendChild(datediv);
 	domconstruct.place('<input id="'+parent.id+'dateinput"></input>',document.getElementById(datediv.id));
 	var timediv = document.createElement('div');
 	timediv.id = parent.id + 'timecontainer';
  	parent.appendChild(timediv);
  	domconstruct.place('<input id="'+parent.id+'timeinput"></input>',document.getElementById(timediv.id));
  	var now = new Date();
  	var start = new Date(now.getTime()-(now.getMilliseconds()/1000));
  	var dateBox = new datetextbox({name: parent.id+'dateinput', value: start,
         constraints: {
         datePattern: 'dd-MM-yyyy',
         }
       }, parent.id+'dateinput');
    dateBox.startup();
    var timeBox = new timetextbox({name: parent.id+'timeinput', value: start,
         constraints: {
         timePattern: 'HH:mm',
         clickableIncrement: 'T01:00:00',
         visibleIncrement: 'T01:00:00',
         visibleRange: 'T06:00:00'
         }
    }, parent.id+'timeinput');
    timeBox.startup();
}
return DatetimePicker;

});
