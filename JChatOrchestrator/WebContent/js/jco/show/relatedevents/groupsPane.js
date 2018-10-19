define (["jco/show/relatedevents/instancesPane",
	"dojo/_base/array","dojo/data/ItemFileReadStore","dojox/grid/EnhancedGrid",
    "dojox/grid/enhanced/plugins/Menu","dijit/Menu"],
    function (instancesPane,array,ItemFileReadStore,EnhancedGrid,enhancedMenu,Menu) {
	
    var groupsPane = function (group_rows) {
 
    	var data = {identifier:'groupname',label:'groupname',items:group_rows};    	
    	var groupstore = new ItemFileReadStore({data:data});

        var layout = buildLayout();
        var menusObject = buildMenus();
    	
    	if (dojo.byId('listGroupsGrid')) {
    	       dojo.byId('listGroupsGrid').destroyRecursive();
    	}
        var grid = new EnhancedGrid({
            jsId: 'listGroupsGrid',
            style: "height: 100%; width: 100%; whitespace:pre",
            id: 'listGroupsGrid',
            store: groupstore,
            rowSelector: '20px',
            selectionMode: 'single',
            plugins : { 
                        menus: menusObject
                      },
           structure:layout});
        grid.placeAt('listGroupsContainer');
        grid.startup();
        
        // call instancesPane when a row is selected
        dojo.connect(grid,'onRowClick',function(e) {
        	e.preventDefault();
        	var selecteditems = grid.selection.getSelected();
        	if (selecteditems.length == 1) {
        		 array.forEach(selecteditems, function(selectedItem){
        			 if (selectedItem !== null) {
        					 var groupname = grid.store.getValues(selectedItem, 'groupname');
        					 instancesPane(groupname);
        				 }
        	     });
        	}
        });
      
     }
    
    function buildLayout() {

    var layout =  [[{ field: 'groupname', name:'Name', width:'20%'},
                    { field: 'type', name:'Type', width:'20%' },
                    { field: 'instances', name:'TimesFired', width:'20%' },                 
                    { field: 'unique_events', name:'#Events', width:'20%'}]];  
    return layout;
    }
    
    
    function buildMenus() {
    	
    var menusObject = {rowMenu: new Menu()};
    
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Fetch FireTimes", onClick:function(){}}));
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Action2", onClick:function(){}}));
    menusObject.rowMenu.startup();
    
    return menusObject;
    
    }
    
    
	return groupsPane;
});