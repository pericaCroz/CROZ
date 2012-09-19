<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Početna stranica</title>
		<%
			List<String> text = (List<String>)request.getAttribute("text");
		%>
	</head>

	<body>
		<h1>Hello</h1>

		<%
 			String error = (String)request.getAttribute("error");
			if(error!=null) {
		%>
 		<div style="border: 2px solid black; background-color: #FFFFFF; color: #FF0000; padding: 5px;"><%=error %></div>
		<% } %>
		
		<p>
		<% for(String t : text) { %>
				<div><%=t %></div>
				<br />
		<% } %>
		</p>
		
		<a href="logout">Logout<a>

</body>
</html>