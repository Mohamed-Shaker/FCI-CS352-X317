<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title> User Home Page </title>
</head>

<body>
<p> Welcome Client : ${it.name} </p> <br>


 <form action="/social/ViewMyTimeLine" method="post">
    <h3> View My TimeLine 
 	     <input type="submit" value="View My TimeLine">  
 	</h3> 
 	<input type="hidden" name="UserEmail" value="${it.email}"> 
 </form>

<h3> Search Friend To Add By Email </h3>
<form action="/social/SendFriendRequestID" method="post">
 <input type="text" name="FriendEamil" >
 <input type="submit" value="Add This Friend" > <br>
 <input type="hidden" name="UserEmail" value="${it.email}"> <br> 
</form>

<h3> Check Friend Requests </h3>
 <ul>
   <li>
	<form action="/social/checkFriendRequests" method="post">
	<input type="submit" value="Check Friend Requests"> <br>
	</form>
   </li>
 </ul>

<h3> Single Messages </h3>
 <ul>
   <li>
	<form action="/social/ListOfMyFriends" method="post">
	<input type="submit" value="Send Message To A Friend"> <br>
	<input type="hidden" name="UserEmail" value="${it.email}">
	</form>
   </li>
 </ul>  

<h3> Write Post on friend timeline </h3> 
	<form action="/social/WritePostTofriendtimeline" method="post">
	<label> Friend Email </label>
	<input type="text" name="FriendEmail" required/> <br>
	
	<label> HashTag </label>
	<input type="text" name="HashTag" /> <br>
	
	 <label> Post Feelings </label> 
	<select name="PostFeeling"> 
	  <option >  </option>
	  <option value="Training"> Training </option>
	  <option value="Running"> Running </option>
	  <option value="Angry"> Angry </option>
	  <option value="Sad"> Sad </option>
	</select>
	<input type="text" name="PostFeelingDescription" /> <br>
	
	<label> Post Privacy </label> 
	<select name="PostPrivacy" required>
	  <option value="Public" > Public </option>
	  <option value="Private"> Private </option>
	  <option value="Custom"> Custom </option>
	</select> 
	<br>
	Custom Friends : <input type="text" name="Custom" > <br>
	<label> Post Content </label> <br>
	<textarea name="PostContent" placeholder="Enter Your Post" required ></textarea> <br>
	<input type="submit" value="Post" > <br>
	<input type="hidden" name="UserEmail" value="${it.email}">	
	</form>
	
	<h3> Write Post on my Timeline </h3> 
	<form action="/social/WritePostToyourtimeline" method="post">
	
	<label> HashTag </label>
	<input type="text" name="HashTag" /> <br>
	
	<label> Post Feelings </label> 
	<select name="PostFeeling" required> 
	  <option >  </option>
	  <option value="Training"> Training </option>
	  <option value="Running"> Running </option>
	  <option value="Angry"> Angry </option>
	  <option value="Sad"> Sad </option>
	</select>
	<input type="text" name="PostFeelingDescription" /> <br>
	
	<label> Post Privacy </label> 
	<select name="PostPrivacy" required>
	  <option value="Public" > Public </option>
	  <option value="Private"> Private </option>
	  <option value="Custom"> Custom </option>
	</select> 
	<br>
	Custom Friends : <input type="text" name="Custom" > <br>
	<label> Post Content </label> <br>
	<textarea name="PostContent" placeholder="Enter Your Post" required ></textarea> <br>
	<input type="hidden" name="UserEmail" value="${it.email}">
	<input type="hidden" name="FriendEmail" value="">
	<input type="submit" value="Post" > <br>	
	</form>	
	   
<h3> Check Notification </h3>
 <ul>
  <li>
	<form action="/social/CheckMyNotification" method="post">
	<input type="submit" value="Check My Notification"> <br>
	<input type="hidden" name="UserEmail" value="${it.email}">
	</form>  
  </li>
  </ul>

 <h3> Group Chat </h3>
 <ul>
  <li> 
	<form action="/social/SendGroupChatMsg" method="post">
	<input type="submit" value="Send Group Chat Messages"> <br>
	<input type="hidden" name="UserEmail" value="${it.email}">
	</form>
  </li>
  
  <li>
	<form action="/social/CheckGroupChatMessages" method="post">
	<input type="submit" value="Check My Group Chat Messages"> <br>
	<input type="hidden" name="UserEmail" value="${it.email}">
	</form>
  </li>
 
 </ul>

<h3> Create Page </h3>
	<form action="/social/CreatePage" method="post" autocomplete="on">
	<ol>
  	  <li> Name : <input type="text" name="pname" required /> </li>
      <li> Type : <input type="text" name="type" required /> </li>
      <li> Category : <input type="text" name="category" required /> </li>
      <li> <input type="submit" value="Create Page" />  </li>
	</ol>
	<input type="hidden" name="UserEmail" value="${it.email}">
	</form>  


  <form action="/social/ViewPagesToLike" method="post" >
   <h3>
    View Pages To Like 
    <input type="hidden" name="UserEmail" value="${it.email}">
    <input type="submit" value="View Pages To Like " />
   </h3>  
  </form>
 
 <h3> HashStatstics </h3>
	<form action="/social/MyHashStatstics" method="post" autocomplete="on">
	<ol>
  	  <li> Name : <input type="text" name="HashTag" required /> </li>
      <li> <input type="submit" value="GetStatstics" />  </li>
	</ol>
	</form>  

 <h3> Logout </h3>
 <ul>
  <li>
 	<form action="/social/LogOUt">
 	<input type="submit" value="LogOut"> <br>
 	</form>
  </li>
 </ul>

  
</body>

</html>
