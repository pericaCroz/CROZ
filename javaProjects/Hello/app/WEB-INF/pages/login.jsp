<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Login</title>
	</head>

	<body>
		<h1>LOGIN</h1>
		
		<%
 			String error = (String)request.getAttribute("error");
 			if(error!=null) {
		%>
  		
  		<div style="border: 2px solid black; background-color: #FFFFFF; color: #FF0000; padding: 5px;"><%=error %></div>
		<% } %>

		<form action="login" method="post">
  			Username: <input type="text" size="20" name="username"><br>
  			Password: <input type="password" size="20" name="password"><br>
  			<input type="submit" value="Send">
		</form>
		
	</body>
</html>