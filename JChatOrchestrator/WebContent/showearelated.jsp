<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="../css/bot.css">
    <link rel="stylesheet" href="../css/jco.css">
    <link rel="stylesheet" type="text/css" href="/JChatOrchestrator/js/dojo-1.8.0/dojo/resources/dojo.css"> 
    <link rel="stylesheet" type="text/css" href="/JChatOrchestrator/js/dojo-1.8.0/dijit/themes/claro/claro.css">
    <link rel="stylesheet" type="text/css" href="/JChatOrchestrator/js/dojo-1.8.0/dojox/grid/resources/Grid.css"> 
    <link rel="stylesheet" type="text/css" href="/JChatOrchestrator/js/dojo-1.8.0/dojox/grid/resources/claroGrid.css"> 
    <link rel="stylesheet" type="text/css" href="/JChatOrchestrator/js/dojo-1.8.0/dojox/grid/enhanced/resources/EnhancedGrid.css"> 
    <link rel="stylesheet" type="text/css" href="/JChatOrchestrator/js/dojo-1.8.0/dojox/grid/enhanced/resources/claroEnhancedGrid.css"> 
    <title><c:out value="${workspacename}"/>  </title>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.5/socket.io.min.js"></script>
    <script src="https://cdn.polyfill.io/v2/polyfill.min.js"></script>
 
    <script type="text/javascript">
            var dojoConfig = {dojoBlankHtmlUrl: location.pathname.replace(/\/[^/]+$/, '') + '/blank.html',
				packages: [{ name: 'jco',location: '/JChatOrchestrator/js/jco'}]
			};
    </script>
       <script type="text/javascript" src="/JChatOrchestrator/js/dojo-1.8.0/dojo/dojo.js" data-dojo-config="async: 1, parseOnLoad: 1"></script>
    <script type="text/javascript">  
    require([
    	"jco/show/relatedevents/Ready",
        "dojo/ready",
        "dijit/layout/ContentPane",
        "dijit/layout/TabContainer",
        "dijit/layout/BorderContainer",
        "dojo/domReady!"], // wait until DOM is loaded
        function(relatedReady,dojoReady){
        dojoReady(function(){
            console.log("dojo is ready");
            relatedReady();
        });
     });
     </script>  
        
</head>
<body class="claro">

<div dojoType="dijit.layout.BorderContainer" id="topPane" design="screenDesign" style="width: 100%; height: 25%; border: none 0;">

<div dojoType="dijit.layout.ContentPane" id="summaryRegion" region="summary"
    style="width: 98%;  margin: 0px; padding-right: 1px">
<div id="summaryDisplay" style="height:30%;min-height:10%;padding-top:5px;">
<h3 id="SummaryResults">Related Event Top10</h2>
<p class="action-bar"></p>
<dl>
      <dt><label id="summaryDateTime"></label></dt>
</dl>
</div>
</div>
</div>

<div dojoType="dijit.layout.BorderContainer" id="mainPane" design="screenDesign" style="width: 100%; height: 75%; border: none 0;">


<div dojoType="dijit.layout.ContentPane" id="groupsRegion" region="groups"
    style="width: 30%;  min-height: 100%; margin: 0px; padding-right: 5px">
<div id="groupsDisplay" style="min-height:50%;padding-top:5px;">
<h3 id="GroupResults">Related Event-Groups</h2>
<p class="action-bar"></p>
<dl>
      <dt><label id="listGroupsLabel"></label></dt>
      <dd><div id="listGroupsContainer" style="width:100%;height:95%;min-height:10%"> </div></dd>
</dl>
</div>
<div id="instancesDisplay" style="min-height:50%;padding-top:5px;">
<h3 id="InstancesResults">Related Event-Group Firing Times</h2>
<p class="action-bar"></p>
<dl>
      <dt><label id="listInstancesLabel"></label></dt>
      <dd><div id="listInstancesContainer" style="width:100%;height:95%;min-height:10%"> </div></dd>
</dl>
</div>
</div>

<div dojoType="dijit.layout.ContentPane" id="eventsRegion"  region="events" style="width: 68%; min-height:100%"> 
<div id="eventsDisplay" style=" min-height:100%;padding-top:5px;">
<h3 id="EventResults">Related Event-Group Constituent Events</h2>
<p class="action-bar"></p>
<dl>
      <dt><label id="listEventsLabel"></label></dt>
      <dd><div id="listEventsContainer" style="width:100%;height:95%;min-height:10%"> </div></dd>
</dl>
</div>
</div>



</div>


</body>
</html>