define (["jco/show/relatedevents/eventsPane",
	"dojo/_base/array","dojo/data/ItemFileReadStore","dojox/grid/EnhancedGrid",
    "dojox/grid/enhanced/plugins/Menu","dijit/Menu","dijit/registry"],
    function (eventsPane,array,ItemFileReadStore,EnhancedGrid,enhancedMenu,Menu,registry) {
	
    var instancesPane = function (groupname,member_rows) {
    	
    	var items = new Array();
    	
    	var numevents = member_rows.length;
    	//use pivot event - assume first member
    	if (member_rows[0].hasOwnProperty('observations')) {
    		for (var index in member_rows[0].observations) {
    			var item = {instance:member_rows[0].observations[index],
    					    events:numevents,observationindex:index};
    			items.push(item);
    		}
    	}
 
    	var data = {identifier:'instance',label:'instance',items:items};    	
    	var instancestore = new ItemFileReadStore({data:data});

        var layout = buildLayout();
        var menusObject = buildMenus();
    	
    	if (registry.byId('listInstancesGrid')) {
    	       registry.byId('listInstancesGrid').destroyRecursive();
    	}
        var grid = new EnhancedGrid({
            jsId: 'listInstancesGrid',
            style: "height: 100%; width: 100%; whitespace:pre",
            id: 'listInstancesGrid',
            store: instancestore,
            rowSelector: '20px',
            selectionMode: 'single',
            plugins : { 
                        menus: menusObject
                      },
           structure:layout});
        grid.placeAt('listInstancesContainer');
        grid.startup();
        
        // call instancesPane when a row is selected
        dojo.connect(grid,'onRowClick',function(e) {
        	e.preventDefault();
        	var selecteditems = grid.selection.getSelected();
        	if (selecteditems.length == 1) {
        		 array.forEach(selecteditems, function(selectedItem){
        			 if (selectedItem !== null) {
        					 var index = grid.store.getValues(selectedItem, 'observationindex');
        					 var $showurl = window.location.origin + 
        					 "/JChatOrchestrator/show/relatedevents?groupname="+groupname+"&observationindex=" + index;
                					 $.ajax({
        					  		 type: "GET",
        					  	 	 url: $showurl,
        					  		 contentType:'application/json',
        					  		 timeout: 30000,
        					  		 beforeSend: function() {},
        					  		 complete: function() {},
        					  		 success: function(reply) {eventsHandler(reply);},
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

    var layout =  [[{ field: 'instance', name:'Instance', width:'50%',formatter:formatEpochToDate},                 
                    { field: 'events', name:'#Events', width:'50%'}]];  
    return layout;
    }
    
    
    function buildMenus() {
    	
    var menusObject = {rowMenu: new Menu()};
    
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Fetch FireTimes", onClick:function(){}}));
    menusObject.rowMenu.addChild(new dijit.MenuItem({label: "Action2", onClick:function(){}}));
    menusObject.rowMenu.startup();
    
    return menusObject;
    
    }
    
    function eventsHandler (reply) {
 	   console.log(reply);
 		if (reply != null) {
    		 if (reply.hasOwnProperty('error')) {
      			     window.location.href = "../JChatOrchestrator/connectioninvalid.html";
      	 } else if (reply.hasOwnProperty('appdata')){
      		  var appdata = reply.appdata;
      		  if (appdata.hasOwnProperty('result_rows')) {
      			 eventsPane(appdata.result_rows);
               }
  
          }
        }
     }
    
    
	return instancesPane;
});