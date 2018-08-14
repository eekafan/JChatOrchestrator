<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="css/bot.css">
    <link rel="stylesheet" href="css/jco.css">
    <title>Chat Interface</title>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.5/socket.io.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="js/launcher-ready.js"></script>
</head>
<body >
<div class="container">
    <div class="chatbot">
      <div id="chatBox" class="dialogContainer">
      <c:forEach items="${workspaces}" var="workspace">
        <button class="launcher-button-input" id=${workspace.name} type="button">${workspace.name}</button></option>
     </c:forEach>
      <button class="login-signon-input" id="logout" type="button">Logout</button>
      </div>
    </div>
</div>
</body>
</html>