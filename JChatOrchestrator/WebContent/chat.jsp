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
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojo/1.7.1/dojo/resources/dojo.css"> 
    <link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/dojo/1.7.1/dijit/themes/claro/claro.css">
    <title><c:out value="${workspacename}"/>  </title>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.5/socket.io.min.js"></script>
    <script src="https://cdn.polyfill.io/v2/polyfill.min.js"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/dojo/1.7.12/dojo/dojo.js"></script>
    <script type="text/javascript">
    require([
        "dojo/query", //alias the query API
        "dojo/dom", // alias DOM api to "dom"
        "dojo/_base/array", // alias array api to "arrayUtil"
        "dojo/store/Memory",
        "dojo/ready",
        "dijit/form/TextBox",
        "dijit/form/DateTextBox",
        "dijit/form/TimeTextBox",
        "dijit/form/FilteringSelect",
        "dojo/domReady!"], // wait until DOM is loaded
        function(dQuery, dDom, dArray, dMemory, dReady){
    	fieldStore = new dMemory({data:[],idproperty:"name"});
    	operatorStore = new dMemory({data:[],idproperty:"name"});
        dReady(function(){
            console.log("dojo is ready");
        });
     });
     </script>
     <script type="text/javascript" charset="utf-8" src="js/chat-ready.js"></script>
</head>
<body class="claro">
<div class="container">
    <div class="chatbot">
        <div id="chatBox" class="dialogContainer">
        <div class="bot_message"><div class="bot"><c:out value="${welcome}"/></div></div>
        </div>
        <form id="emit" method="POST" action="#" lpchecked="1">
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