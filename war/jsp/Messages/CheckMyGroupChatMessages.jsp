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
  
  <form action="/social/ReplyToGroupChat" method="post">
	  <ul>
	  <c:forEach items="${it.MyMsg}"  var="Msg" > 
	    <li>
	    <label> <c:out value="${Msg}"/> </label> 
	    </li> <br>
	  </c:forEach>
	  </ul>
	
	<label> Enter Group Chat Number </label> <br>
	<input type = "text" name="GroupChatNumber" /> <br>
	
	<label> Enter Msg Content </label> <br>
	<textarea name="MsgContent" placeholder="Enter Your Msg" ></textarea> <br>
	
	<input type="submit" value="Reply To Group Chat"> <br>
	
	<c:forEach items="${it.MyEmail}"  var="Me">
	 <input type="hidden" name="UserEmail" value="${Me}">
	</c:forEach>
	
  </form>
</body>
</html>