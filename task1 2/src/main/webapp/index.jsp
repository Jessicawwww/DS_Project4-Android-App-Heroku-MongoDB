<%@ taglib  prefix="c"   uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ page import="org.bson.Document" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <style type="text/css">
        td{
            text-align: center;
        }
        h2{
            text-align: center;
        }
        table{
            margin: auto;
        }
    </style>
</head>
<body>

<h2>Top 10 Popular Search Words</h2>
<table border="1" width="50%" >
    <tr>
        <th>search word</th>
        <th>times</th>
    </tr>
    <% ArrayList<Document> array1=(ArrayList<Document>) request.getAttribute("topTen");%>

    <% for(Document d:array1){ %>
        <tr>
            <td><%=d.get("_id")%></td>
            <td><%=d.get("count")%></td>
        </tr>
    <%}%>
</table>



<h2>Average Latency: <%=request.getAttribute("avgLatency")%> ms</h2>

<h2>Top 10 Popular Device Models</h2>
<table border="1" width="50%">
    <tr>
        <th>device type</th>
        <th>times</th>
    </tr>
    <% ArrayList<Document> array3=(ArrayList<Document>) request.getAttribute("topDevice");%>

    <% for(Document d:array3){ %>
    <tr>
        <td><%=d.get("_id")%></td>
        <td><%=d.get("count")%></td>
    </tr>
    <%}%>
</table>

<h2>Log</h2>
<table border="1" width="100%">
    <tr>
        <th>search word</th>
        <th>request time</th>
        <th>response time</th>
        <th>latency</th>
        <th>user agent</th>
        <th>device model</th>
        <th>response status</th>
        <th>request from app</th>
        <th>request to 3rd party API</th>
        <th>response from 3rd party API</th>
    </tr>
    <% ArrayList<Document> array2=(ArrayList<Document>) request.getAttribute("fullLog");%>

    <% for(Document d:array2){ %>
    <tr>
        <td><%=d.get("searchWord")%></td>
        <td><%=d.get("request time")%></td>
        <td><%=d.get("response time")%></td>
        <td><%=d.get("latency")%></td>
        <td><%=d.get("user-agent")%></td>
        <td><%=d.get("device model")%></td>
        <td><%=d.get("response status")%></td>
        <td><%=d.get("request from app")%></td>
        <td><%=d.get("request to 3rd party API")%></td>
        <td><%=d.get("response from 3rd party API")%></td>

    </tr>
    <%}%>
</table>

</body>
</html>