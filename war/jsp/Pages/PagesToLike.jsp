<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>User Pages To Like </title>
</head>

<body>
  <h2> 
	  <c:forEach items="${it.MyEmail}"  var="Me">
	   <p> Welcome Client : <c:out value="${Me}"/> </p>
	  </c:forEach>
  </h2>	  
  
  <form action="/social/LikePage" method="post">
	  <ol>
	  <c:forEach items="${it.PagesToLike}"  var="PagesToLike" > 
	    <li>
	    <input type="radio" name="PagesToLike" value="${PagesToLike}"> <c:out value="${PagesToLike}"/><br>
	    </li> 
	  </c:forEach>
	  </ol>
	
	<input type="submit" value="Like Page"> <br>
	
	<c:forEach items="${it.MyEmail}"  var="Me">
	 <input type="hidden" name="UserEmail" value="${Me}">
	</c:forEach>
	
  </form>
</body>
</html>