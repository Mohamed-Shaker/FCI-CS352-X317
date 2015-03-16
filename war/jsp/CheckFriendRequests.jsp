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
  
  <form action="/social/AcceptFriendRequest" method="post">
	  <ol>
	  <c:forEach items="${it.FutureFriend}"  var="Friend" > 
	    <li>
	    <input type="radio" name="FriendEmail" value="${Friend}"> <c:out value="${Friend}"/><br>
	    </li> 
	  </c:forEach>
	  </ol>
	<input type="submit" value="Accept Friend Request">
	
	<c:forEach items="${it.MyEmail}"  var="Me">
	 <input type="hidden" name="UserEmail" value="${Me}">
	</c:forEach>
	
  </form>
</body>
</html>