define (["jco/show/relatedevents/instancesPane",
	"dojo/_base/array","dojo/data/ItemFileReadStore","dojox/grid/EnhancedGrid",
    "dojox/grid/enhanced/plugins/Menu","dijit/Menu","dijit/registry"],
    function (instancesPane,array,ItemFileReadStore,EnhancedGrid,enhancedMenu,Menu,registry) {
	
    var groupsPane = function (group_rows) {
 
    	var data = {identifier:'groupname',label:'groupname',items:group_rows};    	
    	var groupstore = new ItemFileReadStore({data:data});

        var layout = buildLayout();
        var menusObject = buildMenus();
    	
    	if (registry.byId('listGroupsGrid')) {
    	       registry.byId('listGroupsGrid').destroyRecursive();
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
        					 var $showurl = window.location.origin + 
        					 "/JChatOrchestrator/show/relatedevents?groupname=" + groupname;
                					 $.ajax({
        					  		 type: "GET",
        					  	 	 url: $showurl,
        					  		 contentType:'application/json',
        					  		 timeout: 30000,
        					  		 beforeSend: function() {},
        					  		 complete: function() {},
        					  		 success: function(reply) {instancesHandler(reply,groupname);},
        					  		 fail: function(data) {}     		
        					  	     }); 	  
  
        				 }
        	     });
        	}
        });
      
     }
    
    function formatEpochToDate(field) {
        if (isNaN(field)) {
        return "";
        } else {
        var thisdate = new Date(field);
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
    
    function instancesHandler (reply,groupname) {
 		if (reply != null) {
    		 if (reply.hasOwnProperty('error')) {
      			     window.location.href = "../JChatOrchestrator/connectioninvalid.html";
      	 } else if (reply.hasOwnProperty('appdata')){
      		  var appdata = reply.appdata;
      		  if (appdata.hasOwnProperty('result_rows')) {
      			 instancesPane(groupname,appdata.result_rows);
               }
  
          }
        }
     }
    
    
	return groupsPane;
});