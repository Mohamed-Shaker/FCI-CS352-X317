package com.FCI.SWE.Controller;

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
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

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
@Produces("text/html")
public class UserController {
	private String Domain_Name = //"http://1-dot-mhgbx317.appspot.com" ;
							   "http://localhost:8888" ;
	/**
	 * Action function to render Signup page, this function will be executed
	 * using url like this /rest/signup
	 * 
	 * @return sign up page
	 */
	@GET
	@Path("/LogOUt")
	public Response LogOUt() {
		User.resetCurrentActiveUser();
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}
	
	@GET
	@Path("/signup")
	public Response signUp() {
		return Response.ok(new Viewable("/jsp/register")).build();
	}

	/**
	 * Action function to render home page of application, home page contains
	 * only signup and login buttons
	 * 
	 * @return enty point page (Home page of this application)
	 */
	@GET
	@Path("/")
	public Response index() {
		//User.resetCurrentActiveUser();
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}
		
	/**
	 * Action function to render login page this function will be executed using
	 * url like this /rest/login
	 * 
	 * @return login page
	 */
	@GET
	@Path("/login")
	public Response login() {
		return Response.ok(new Viewable("/jsp/login")).build();
	}
	
	/**
	 * Action function to response to signup request, This function will act as
	 * a controller part and it will calls RegistrationService to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided user password
	 * @return Status string
	 */
	
	@POST
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public String response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {

		String serviceUrl = Domain_Name + "/rest/RegistrationService";
		String urlParameters = "uname=" + uname + "&email=" + email
				+ "&password=" + pass;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			// System.out.println(retJson);
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Registered Successfully";

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); 
		 * return uname;
		 */
		return "Failed";
	}

	/**
	 * Action function to response to login request. This function will act as a
	 * controller part, it will calls login service to check user data and get
	 * user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return Home page view
	 */
	@POST
	@Path("/home")
	@Produces("text/html")
	public Response home( @FormParam("uname") String UserName ,
			              @FormParam("password") String UserPassword 
			             ) 
	{
		String urlParameters = "uname=" + UserName + "&password=" + UserPassword ;
		String retJson = Connection.connect(
				 Domain_Name + "/rest/LoginService", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			User user = User.getUser(object.toJSONString());
			
			map.put("name", user.getName() );
			map.put("ID", String.valueOf( user.getId() ) );
			map.put("pass", user.getPass() );
			map.put("email", user.getEmail() );
			
			return Response.ok(new Viewable("/jsp/home", map)).build();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}

		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return null;
	}

	
	/**
	 * This method used to control request for send request and guide it to send request services
	 * which is send friend request services 
	 * @param UserEmail 
	 * @param FriendEmail 
	 * @return <Code>String</Code>
	 */
	
	@POST
	@Path("/SendFriendRequestID")
	@Produces(MediaType.TEXT_PLAIN)
 	public String SendFriendRequest( 
						             @FormParam("UserEmail") String UserEmail ,
						             @FormParam("FriendEamil") String FriendEmailToAdd 
						           ) 
 	{
		String serviceUrl = Domain_Name + "/rest/SendFriendRequest";
		String urlParameters = "UserEmail=" +  UserEmail   
				             + "&FriendEamil=" + FriendEmailToAdd;
		
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		
		JSONParser parser = new JSONParser();
		Object obj;
		try 
		{
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Request Has Been Sent Successfully.";

			else if(object.get("Status").equals("Friend Email Does Not Exist") )
			    return "Friend Email Does Not Exist , So You Can't Add Him.";
			
			else if( object.get("Status").equals("You Can't Add Yourself") )
				return "You Can't Add Yourself.";
		} 
		catch (ParseException e) { e.printStackTrace(); }
		
		return "Friend Request Has Been Can't Be Successfully";
	}
	
	/**
	 * This method act as a controller for user request where we handle user request 
	 * and call check friend requests response. 
	 * @return <Code>Response</Code>
	 */
	@POST
	@Path("/checkFriendRequests")
	public Response CheckFriendRequests() {
		String UserEmail = User.getCurrentActiveUser().getEmail();
		System.out.println("Email : " + UserEmail );
		Vector<String> FEmail = UserEntity.get_Friend_Requests( UserEmail );
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		MyMap.put("FutureFriend", FEmail );
		return Response.ok(new Viewable("/jsp/CheckFriendRequests"  , MyMap ) ).build();
	}
	/**
	 * This method act as a controller where we handle user request and 
	 * call services to make accept friend process 
	 * @param UserEmail the user who will accept friend request 
	 * @param FriendEmailToAdd the one who should receive a notification if user accept friend request .
	 * @return <code>String</code> representing status of user request if successful or not .
	 */
	@POST
	@Path("/AcceptFriendRequest")
	@Produces(MediaType.TEXT_PLAIN)
 	public String AcceptFriendRequest( 
						             @FormParam("UserEmail") String UserEmail ,
						             @FormParam("FriendEmail") String FriendEmailToAdd 
						           ) 
 	{
		String Temp = UserEmail ;
		UserEmail = FriendEmailToAdd ;
		FriendEmailToAdd = Temp;
		/*
		System.out.println("Accept Controller");
		System.out.println(UserEmail);
		System.out.println(FriendEmailToAdd );
		//System.out.println("UserEmail : " + UserEmail + " , FriendEmail : " + FriendEmailToAdd );
		*/
		String serviceUrl = Domain_Name + "/rest/AcceptAddFriend";
		String urlParameters = "UserEmail=" +  UserEmail   
				             + "&FriendEmail=" + FriendEmailToAdd;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try 
		{
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Request Has Been Accepted Successfully";
		} 
		catch (ParseException e) { e.printStackTrace(); }
		return "Friend Request Has Been Not Accepted";
	}
	
}