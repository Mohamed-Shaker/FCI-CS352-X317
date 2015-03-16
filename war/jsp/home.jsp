<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title> User Home Page </title>
</head>

<body>
<p> Welcome b2a ya ${it.name} </p> <br>
<p> Yalaeee IDeeeehek : ${it.ID} </p> <br>
<p> This is should be user home page </p> <br>
<p> Current implemented services "http://fci-swe-apps.appspot.com/rest/RegistrationService --- {requires: uname, email, password}" </p> <br>
<p> and "http://fci-swe-apps.appspot.com/rest/LoginService --- {requires: uname,  password}" </p> <br>
<p> you should implement sendFriendRequest service and addFriend service </p> <br>

<form action="/social/SendFriendRequestID" method="post">
 
 <label> Search Friend To Add By Email </label>
 <input type="text" name="FriendEamil" >
 <input type="submit" value="Add This Friend" > <br>
 <input type="hidden" name="UserEmail" value="${it.email}"> <br> 
</form>

<form action="/social/LogOUt">
<input type="submit" value="LogOut"> <br>
</form>

<form action="/social/checkFriendRequests" method="post">
<input type="submit" value="Check Friend Requests"> <br>
</form>

</body>

</html>
