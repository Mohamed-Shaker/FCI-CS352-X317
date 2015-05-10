package com.FCI.SWE.Controller;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import com.FCI.SWE.Models.User;
import com.google.appengine.labs.repackaged.org.json.JSONException;

public class UserControllerTest {
  
  UserController MyUserController = new UserController();
	
  @SuppressWarnings("deprecation")
  @Test
  public void AcceptFriendRequest() {
    Assert.assertEquals( "Friend Request Has Been Accepted Successfully" , MyUserController.AcceptFriendRequest("Gaber@yahoo.com", "ali@yahoo.com"));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void CreatePage() throws JSONException, ParseException {
	String Result = MyUserController.CreatePage("FCI", "Education" , "HighLevel" , "MohamedShaker");  
    Assert.assertEquals( "Page Has Been Created Successfully" , Result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void LikePage() throws JSONException, ParseException {
	String Result = MyUserController.LikePage( "ali@yahoo.com" , "FCI" );    
    Assert.assertEquals( "Page Has Been Liked Successfully." ,  Result );
  }

  @SuppressWarnings("deprecation")
  @Test
  public void MyHashStatstics() throws JSONException, ParseException {
	String Result = MyUserController.MyHashStatstics( "#FCI" );
    Assert.assertEquals( "No HashTag with this name IDIOT" , Result );
  }

  @SuppressWarnings("deprecation")
  @Test
  public void SendFriendRequest() {
    Assert.assertEquals( "Friend Request Has Been Sent Successfully." , MyUserController.SendFriendRequest("ali@yahoo.com" , "Gaber@yahoo.com") );
  }

  @SuppressWarnings("deprecation")
  @Test
  public void SendMsg() {
	String S = MyUserController.SendMsg( "ali@yahoo.com" , "gaber@yahoo.com" , "Hi 2zayeek" );  
    Assert.assertEquals( "Msg Has Been Sent" , S );
  }

  @SuppressWarnings("deprecation")
  @Test
  public void login() {
	  Response R1 = Response.ok(new Viewable("/jsp/login")).build();
	  Response R2 = MyUserController.login();
      Assert.assertEquals( R1 , R2 );
  }

  @SuppressWarnings("deprecation")
  @Test
  public void response() {
	 Assert.assertEquals( "Registered Successfully" ,  MyUserController.response( "Ali" , "ali@yahoo.com" , "123" )); 
  }

}
