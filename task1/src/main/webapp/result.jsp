<%--Author: Olivia Wu, Keqing Xing--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%= request.getAttribute("doctype") %>

<html>
<head>
    <title>Cocktail Details</title>
</head>
<body>
<% if (request.getAttribute("data") != null) {
String test = request.getAttribute("data").toString(); %>
<p><%=test%></p>
<% } %>
</body>
<form action="getCocktailInfo" method="GET">
    <label for="letter">Type another name.</label>
    <input type="text" name="searchWord" value="" /><br>
    <input type="submit" value="Submit" />
</form>
</html>


