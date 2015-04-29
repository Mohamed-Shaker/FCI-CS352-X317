<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>User TimeLine </title>
</head>

<body>
  <h2> 
	  <c:forEach items="${it.MyEmail}"  var="Me">
	   <p> Welcome Client : <c:out value="${Me}"/> </p>
	  </c:forEach>
  </h2>	  
  
  <form action="/social/LikePostID" method="post">
	  <c:forEach items="${it.MyTimeLine}"  var="TimeLine" > 
	    <c:out value="${TimeLine}"/> <br>
	  </c:forEach>
	
	<label> Enter Post ID To Like : </label>
	<input type="text" name="PostID" placeholder="Enter Post ID" required/>
	<input type="submit" value="Like Post"> <br>
	
	<c:forEach items="${it.MyEmail}"  var="Me">
	 <input type="hidden" name="UserEmail" value="${Me}">
	</c:forEach>
  </form>
  
 <form action="/social/SharePostID" method="post">
	<label> Enter Post ID To Share : </label>
	<input type="text" name="PostID" placeholder="Enter Post ID" required/>
	<input type="submit" value="Share Post"> <br>
	
	<c:forEach items="${it.MyEmail}"  var="Me">
	 <input type="hidden" name="UserEmail" value="${Me}">
	</c:forEach>
  </form>
  
</body>
</html>