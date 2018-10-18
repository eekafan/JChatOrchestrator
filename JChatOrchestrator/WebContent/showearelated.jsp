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
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojo/1.7.12/dojo/resources/dojo.css"> 
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojo/1.7.12/dijit/themes/claro/claro.css">
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojox/1.7.12/grid/resources/claroGrid.css">
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojox/1.7.12/grid/enhanced/resources/EnhancedGrid.css">
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojox/1.7.12/grid/enhanced/resources/claroEnhancedGrid.css">
    <title><c:out value="${workspacename}"/>  </title>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.5/socket.io.min.js"></script>
    <script src="https://cdn.polyfill.io/v2/polyfill.min.js"></script>
 
    <script type="text/javascript">
            var dojoConfig = {dojoBlankHtmlUrl: location.pathname.replace(/\/[^/]+$/, '') + '/blank.html',
				packages: [{ name: 'jco',location: location.pathname.replace(/\/[^/]+$/, '') + '/js/jco'}]
			};
    </script>
       <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/dojo/1.7.12/dojo/dojo.js" data-dojo-config="async: 1, parseOnLoad: 1"></script>
    <script type="text/javascript">  
    require([
    	"jco/show/related/Ready",
        "dojo/ready",
        "dojox/grid/DataGrid",
        "dojox/grid/EnhancedGrid",
        "dojox/grid/enhanced/plugins/Menu",
        "dojox/grid/enhanced/plugins/Pagination",
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
<div dojoType="dijit.layout.BorderContainer" id="mainPane" design="screenDesign" style="width: 100%; height: 98%; border: none 0;">

<div dojoType="dijit.layout.ContentPane" id="leftRegion" region="left"
    style="width: 30%; height: 98%; margin: 0px; padding-right: 5px">
    <div id="groupDisplay" style="height:30%;min-height:10%;padding-top:5px;">
<h2 id="GroupResults">Related Event Groups</h2>
<p class="action-bar"></p>
<dl>
      <dt><label id="listGroupsLabel"></label></dt>
      <dd><div id="listGroupsContainer" style="width:100%;height:95%;min-height:10%"> </div></dd>
</dl>
</div>
</div>

<div dojoType="dijit.layout.ContentPane" id="rightRegion"  region="right" style="width: 68%; height: 98%"> 
<div id="eventDisplay" style="height:30%;min-height:10%;padding-top:5px;">
<h2 id="EventResults">Events within Group</h2>
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