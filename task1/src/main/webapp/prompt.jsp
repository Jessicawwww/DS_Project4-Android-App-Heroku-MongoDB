<%--Author: Olivia Wu, Keqing Xing--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%= request.getAttribute("doctype") %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Log Page</title>
</head>
<body>
<p>Search history for this android app is as below.</p>
<form action="getCocktailInfo" method="GET">
    <label for="letter">Type the word.</label>
    <input type="text" name="searchWord" value="" /><br>
    <input type="submit" value="Click Here" />
</form>
</body>
</html>