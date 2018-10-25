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
    
    function buildLayout() {

    var layout =  [[{ field: 'eventepoch', name:'time', width:'30%',},                 
                    { field: 'identifier', name:'event', width:'70%'}]];  
    return layout;
    }
    
    
    function buildMenus() {
    	
    var menusObject = {rowMenu: new Menu()};
    
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Fetch FireTimes", onClick:function(){}}));
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Action2", onClick:function(){}}));
    menusObject.rowMenu.startup();
    
    return menusObject;
    
    }
    
	return eventsPane;
});