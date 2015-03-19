package com.FCI.SWE.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.User;
import com.FCI.SWE.ServicesModels.UserEntity;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class UserServices {
	
	
	/*@GET
	@Path("/index")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}*/


		/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		UserEntity user = new UserEntity(uname, email, pass);
		user.saveUser();
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * @param uname provided user name
	 * @param pass provided user password
	 * @return user in json format
	 */
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("uname") String uname,
			@FormParam("password") String pass) 
	{
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(uname, pass);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
			object.put("id", user.getId());
		}
		return object.toString();

	}

	/**
	 * Send Friend Request Rest Service, this service will be called to make Send Friend Request process
	 * that will save user email who sends friend request and save friend email and the stauts 
	 * which is pending datastore
	 * @param UserEmail the user email
	 * @param FriendEamil the friend email
	 * @return <code>String</code> represents status or a word about sending process
	 */
	@POST
	@Path("/SendFriendRequest")
	public String SendFriendRequest( @FormParam("UserEmail") String UserEmail ,
						             @FormParam("FriendEamil") String FriendEmailToAdd 
						           ) 
	{
		//System.out.println("User Name : " + UserName );
		//System.out.println("User ID : " + UserID );
		//System.out.println("User Password : " + UserPassword );
		/*if (user == null) 
		{
			object.put("Status", "Who Are You !!!! Failed");
		} 
		else 
		{*/
			//System.out.println("Herrrrrrrrrrrrrre.");
		//}
			
		JSONObject object = new JSONObject();
		
		if( UserEmail.compareTo( FriendEmailToAdd ) == 0 )
		{
		  object.put("Status", "You Can't Add Yourself" );
		}
		else if( !UserEntity.Check_User_Friend( FriendEmailToAdd )  )
		{
		  object.put("Status", "Friend Email Does Not Exist" );	
		}
		else 
		{
		  UserEntity.Send_Friend_Request(UserEmail, FriendEmailToAdd);
		  object.put("Status", "OK");
		}
		
		return object.toString();
	}
	
	/**
	 * Accept Add Friend Rest Service, this service will be called to make accept add Friend Request process
	 * that will change sending friend status to accept instead of pending 
	 * @param UserEmail the user email who accept friend request 
	 * @param FriendEamil the friend email who will receive a notification about accepting his/her request.
	 * @return <code>String</code> represents status or a word about accepting process
	 */
	@POST
	@Path("/AcceptAddFriend")
	public String AcceptAddFriend( @FormParam("UserEmail") String UserEmail ,
			                       @FormParam("FriendEmail") String FriendEmail ) 
	{
		JSONObject object = new JSONObject();
		
		System.out.println("Accept Services");
		System.out.println(UserEmail);
		System.out.println(FriendEmail);
		
		if( UserEntity.Accept_Add_Friend_Request( UserEmail , FriendEmail ) ) 
		   {
			System.out.println("Add Function OK.");
			object.put("Status", "OK");
		   }
		else
		{
			System.out.println("Add Function Not OK.");
			object.put("Status", "Failed");	
		}
		return object.toString();

	}
	/***
	 * This Services is used to get friend requests which has been sent to specific user.
	 * so that user can accept any of these friend requests .
	 * @return
	 */
	@POST
	@Path("/CheckFriendRequests")
	public String CheckFriendRequests() 
	{
		//@FormParam("UserEmail") String UserEmail
		JSONObject object = new JSONObject();
		String UserEmail = User.getCurrentActiveUser().getEmail();
		if( UserEmail == null )
		{
		 object.put( "Status" , "OK" );	
		}
		else 
		{
		 System.out.println("Email : " + UserEmail );
		 Vector<String> MyVector = UserEntity.get_Friend_Requests( UserEmail );
		 object.put("Status", "Failed");
		 object.put("Me", UserEmail );
		 for(int i=0; i<MyVector.size();i++)
		    object.put("FutureFriend", MyVector.elementAt(i) );
		}
		return object.toString();
	}
}