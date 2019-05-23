define ([
	"dojo/_base/array","dojo/data/ItemFileReadStore","dojox/grid/EnhancedGrid",
    "dojox/grid/enhanced/plugins/Menu","dojox/grid/enhanced/plugins/Pagination",
    "dijit/Menu","dijit/registry"],
    function (array,ItemFileReadStore,EnhancedGrid,enhancedMenu,Pagination,Menu,registry) {
	
    var eventsPane = function (events) {
    	
    	var data = {identifier:'rowindex',label:'rowindex',items:events};    	
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
            plugins : { pagination: {
                pageSizes: ['25','50','100','All'],
                description: true, sizeSwitch: true, pageStepper:true,
	            gotobutton: true, maxPageStep:4, position: 'bottom' },
                        menus: menusObject
                      },
           structure:layout});
        grid.placeAt('listEventsContainer');
        grid.startup();
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

    var layout =  [[                
                    { field: 'NODE', name:'node', width:'10%'},
                    { field: 'STATECHANGE', name:'statechg', width:'10%'},
                    { field: 'LASTOCCURRENCE', name:'lastocc', width:'10%'},
                    { field: 'SEVERITY', name:'sev', width:'3%'},
                    { field: 'SUMMARY', name:'summary', width:'50%'}                 
    ]];  
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