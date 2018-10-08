<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="css/bot.css">
    <link rel="stylesheet" href="css/jco.css">
    <title><c:out value="${workspacename}"/>  </title>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.5/socket.io.min.js"></script>
    <script src="https://cdn.polyfill.io/v2/polyfill.min.js"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/dojo/1.7.1/dojo/dojo.js"></script>
    <script type="text/javascript">
    require([
        "dojo/query", //alias the query API
        "dojo/dom", // alias DOM api to "dom"
        "dojo/_base/array", // alias array api to "arrayUtil"
        "dojo/store/Memory",
        "dojo/ready",
        "dijit/layout/ContentPane",
        "dijit/layout/TabContainer",
        "dojox/grid/cells/dijit",
        "dojo/domReady!"], // wait until DOM is loaded
        function(dQuery, dDom, dArray, dMemory, dReady){
        dReady(function(){
            console.log("dojo is ready");
        });
     });
     </script>
     <script type="text/javascript" charset="utf-8" src="js/chat-ready.js"></script>
</head>
<body>
<div class="container">
    <div class="chatbot">
        <div id="chatBox" class="dialogContainer">
        <div class="bot_message"><div class="bot"><c:out value="${welcome}"/></div></div>
        </div>
        <form id="emit" method="POST" action="#" _lpchecked="1">
            <!-- Use a form to get input. Flask template style. -->
            <!-- Use class="dialogInput, to style it like the output. -->
       <input class="dialogInput" type="text" name="emit_data" id="emit_data" autofocus="autofocus" placeholder="Type here">
      </form>
 
        
    </div>
</div>
<!-- Use bot.js to give us a nice chatbot UI. -->
<script src="js/bot.js"></script>
</body>
</html>