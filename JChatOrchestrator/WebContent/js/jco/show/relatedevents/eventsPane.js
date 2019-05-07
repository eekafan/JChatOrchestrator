define ([
	"dojo/_base/array","dojo/data/ItemFileReadStore","dojox/grid/EnhancedGrid",
    "dojox/grid/enhanced/plugins/Menu","dijit/Menu","dijit/registry"],
    function (array,ItemFileReadStore,EnhancedGrid,enhancedMenu,Menu,registry) {
	
    var eventsPane = function (events) {
    	
    	var items = new Array();
    	
    	for (var index in events) {
    	  var item = {identifier:events[index].identifier,eventepoch: String(events[index].eventepoch)};
    	  items.push(item);
    	}
  
    	var data = {identifier:'identifier',label:'identifier',items:items};    	
    	var eventstore = new ItemFileReadStore({data:data});

        var layout = buildLayout();
        var menusObject = buildMenus();
    	
    	if (registry.byId('listEventsGrid')) {
    	       registry.byId('listEventsGrid').destroyRecursive();
    	}
        var grid = new EnhancedGrid({
            jsId: 'listEventsGrid',
            style: "height: 100%; width: 100%; whitespace:pre",
            id: 'listEventsGrid',
            store: eventstore,
            rowSelector: '20px',
            selectionMode: 'single',
            plugins : { 
                        menus: menusObject
                      },
           structure:layout});
        grid.placeAt('listEventsContainer');
        grid.startup();
        grid.setSortIndex(1,true);
        grid.sort();
  
     }
    
    function formatEpochToDate(field) {
        if (isNaN(field)) {
        return "";
        } else {
        var thisdate = new Date(parseInt(field));
        year = "" + thisdate.getFullYear();
        month = "" + (thisdate.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
        day = "" + thisdate.getDate(); if (day.length == 1) { day = "0" + day; }
        hour = "" + thisdate.getHours(); if (hour.length == 1) { hour = "0" + hour; }
        minute = "" + thisdate.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
        second = "" + thisdate.getSeconds(); if (second.length == 1) { second = "0" + second; }
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        }
   }
    
    function buildLayout() {

    var layout =  [[{ field: 'eventepoch', name:'time', width:'30%',formatter:formatEpochToDate},                 
                    { field: 'identifier', name:'event', width:'70%'}]];  
    return layout;
    }
    
    
    function buildMenus() {
    	
    var menusObject = {rowMenu: new Menu()};
    
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Placeholder Action1", onClick:function(){}}));
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Placeholder Action2", onClick:function(){}}));
    menusObject.rowMenu.startup();
    
    return menusObject;
    
    }
    
	return eventsPane;
});