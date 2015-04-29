<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>User Friend Requests</title>
</head>

<body>
  <h2> 
	  <c:forEach items="${it.MyEmail}"  var="Me">
	   <p> Welcome Ya : <c:out value="${Me}"/> </p>
	  </c:forEach>
  </h2>	  
  
  <form action="" method="post">
	  <ol>
	  <c:forEach items="${it.MyNotification}"  var="MyNotification" > 
	    <li>
	    <label> <c:out value="${MyNotification}"/> </label> 
	    </li> <br>
	  </c:forEach>
	  </ol>
		
  </form>
</body>
</html>