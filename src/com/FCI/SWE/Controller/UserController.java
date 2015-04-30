package com.FCI.SWE.Controller;

import java.util.HashMap;
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

import com.FCI.SWE.Models.MyModel;
import com.FCI.SWE.Models.User;
import com.FCI.SWE.Services.GroupChat;
import com.FCI.SWE.ServicesModels.UserEntity;
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
	public Response CheckFriendRequests() 
	{
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
	
	/**
	 * 
	 * @return
	 */
	@POST
	@Path("/ListOfMyFriends")
	public Response ListOfMyFriends( @FormParam("UserEmail") String UserEmail )
	{
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Vector<String> MyFriends = UserEntity.Get_MyFriends( UserEmail );
		//for( String S : MyFriends )
		//	System.out.println( "Email : " + S );
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		MyMap.put("MyFriend", MyFriends );	
		return Response.ok(new Viewable("/jsp/ShowMyFriends" , MyMap ) ).build();
	} 
	
	/**
	 * This Method is used to make user able to send a single message to anyone of his friends 
	 * @param UserEmail    Email Of User Who Sends The Message  
	 * @param FriendEmail  FriendEmail To Send The Message To 
	 * @param MsgContent   Message Content To Send And Be Retrieved 
	 * @return <Code>String</Code>
	 */
	@POST
	@Path("/SendMsg")
	@Produces(MediaType.TEXT_PLAIN)
	public String SendMsg( 
			               @FormParam("UserEmail") String UserEmail ,
                           @FormParam("FriendEmail") String FriendEmail , 
                           @FormParam("MsgContent") String MsgContent
                          )
	{
		String serviceUrl = Domain_Name + "/rest/SendMsg";
		String urlParameters = "UserEmail=" +  UserEmail   
				             + "&FriendEmail=" + FriendEmail
				             + "&MsgContent=" + MsgContent ;
		
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try 
		{
			obj = parser.parse(retJson); 
			JSONObject object = (JSONObject) obj;
			
			if (object.get("Status").equals("Msg Has Not Been Sent") )
				return "Msg Has Not Been Sent";
			
			else if(object.get("Status").equals("Msg Has Been Sent") )
				return "Msg Has Been Sent";
		} 
		catch (ParseException e) { e.printStackTrace(); }
	 	return "There Is A Problem In Sending Single Message Process" ; 
	}
	
	@POST
	@Path("/CheckRecievedMsgs")
	public Response CheckRecievedMsgs( @FormParam("UserEmail") String UserEmail )
	{
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		Vector<String> MyMsg = UserEntity.Get_MyMessages(UserEmail);
		MyMap.put( "MyMsg" , MyMsg );
		
		return Response.ok(new Viewable("/jsp/Messages/CheckMyMessages" , MyMap ) ).build();
	} 
	
	@POST
	@Path("/CheckMyNotification")
	public Response CheckMyNotification( 
			                           @FormParam("UserEmail") String UserEmail 
			                         )
	{
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		Vector<String> MyNotification = UserEntity.Get_My_Notification(UserEmail);
		MyMap.put( "MyNotification" , MyNotification );
		return Response.ok(new Viewable("/jsp/Notifications/CheckMyNotifications" , MyMap ) ).build();
	}
	
	@POST
	@Path("/SendGroupChatMsg")
	public Response SendGroupChatMsg( 
			                           @FormParam("UserEmail") String UserEmail 
			                         )
	{
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Vector<String> MyFriends = UserEntity.Get_MyFriends( UserEmail );
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		MyMap.put("MyFriend", MyFriends );
		return Response.ok(new Viewable("/jsp/Messages/SendGroupChat" , MyMap ) ).build();
	}
	
	/**
	 * This Method is used to make user able to send a single message to anyone of his friends 
	 * @param UserEmail    Email Of User Who Sends The Message  
	 * @param FriendEmail  FriendEmail To Send The Message To 
	 * @param MsgContent   Message Content To Send And Be Retrieved 
	 * @return <Code>String</Code>
	 */
	
	@POST
	@Path("/SendGroupChatController")
	@Produces(MediaType.TEXT_PLAIN)
	public String SendGroupChat( 
			               @FormParam("UserEmail") String UserEmail ,
                           @FormParam("FriendsEmail") String FriendsEmail , 
                           @FormParam("MsgContent") String MsgContent
                          )
	{
		System.out.println("Send Group Chat Msg.");
		
		FriendsEmail += "," + UserEmail ;
		String serviceUrl = Domain_Name + "/rest/SendGroupChat";
		String urlParameters = "UserEmail=" +  UserEmail   
				             + "&FriendsEmail=" + FriendsEmail
				             + "&MsgContent=" + MsgContent ;
		
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try 
		{
			obj = parser.parse(retJson); 
			JSONObject object = (JSONObject) obj;
			
			if (object.get("Status").equals("Group Chat Msg Has Not Been Sent") )
				return "Group Chat Msg Has Not Been Sent";
			
			else if(object.get("Status").equals("Group Chat Msg Has Been Sent") )
				return "Group Chat Msg Has Been Sent";
		} 
		catch (ParseException e) { e.printStackTrace(); }
		
	 	return "Problem In Sending Group Chat Message" ; 
	}
	
	
	@POST
	@Path("/CheckGroupChatMessages")
	public Response CheckGroupChatMessages( @FormParam("UserEmail") String UserEmail )
	{
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		Vector<String> MyMsg = GroupChat.Get_MyGroupChatMessages(UserEmail);
		MyMap.put( "MyMsg" , MyMsg );
		return Response.ok(new Viewable("/jsp/Messages/CheckMyGroupChatMessages.jsp" , MyMap ) ).build();
	}
	
	@POST
	@Path("/ReplyToGroupChat")
	@Produces(MediaType.TEXT_PLAIN)
	public String ReplyToGroupChat( 
			               		    @FormParam("UserEmail") String UserEmail ,
			               		    @FormParam("GroupChatNumber") int GroupChatID , 
			               		    @FormParam("MsgContent") String MsgContent
                          		   )
	{
		System.out.println("Reply To Group Chat Msg .");
		
		String serviceUrl = Domain_Name + "/rest/ReplyToGroupChat";
		String urlParameters = "UserEmail=" +  UserEmail   
				             + "&GroupChatNumber=" + GroupChatID
				             + "&MsgContent=" + MsgContent ;
		
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try 
		{
			obj = parser.parse(retJson); 
			JSONObject object = (JSONObject) obj;
			
			if (object.get("Status").equals("2") )
				return "Reply Msg Has Been Sent To Group Chat";
			
			else if(object.get("Status").equals("1") )
				return "Reply Msg Has Not Been Sent To Group Chat";
		} 
		catch (ParseException e) { e.printStackTrace(); }
		
	 	return "Problem In Replying To Group Chat Message" ; 
	}
	
	@POST	
	@Path("/CreatePage")
	@Produces(MediaType.TEXT_PLAIN)
	public String CreatePage( 
							  @FormParam("pname") String pname ,
							  @FormParam("type") String type ,
							  @FormParam("category") String category ,
							  @FormParam("UserEmail") String UserEmail 
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/CreatePage";
		String urlParameters = "pname=" +  pname + 
				               "&type=" + type +
				               "&category=" + category + 
				               "&UserEmail=" + UserEmail;

        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        MyModel.Json_Result_Msg = "Page Has Not Been Created Successfully." ;
        if ( MyModel.MyJsonObject.get("Status").equals("1") )
        	 {
         	  System.out.println("Here");
        	  MyModel.Json_Result_Msg = "Page Name Has Not Been Entered";
        	 }
        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg = "Page Type Has Not Been Entered";
        else if ( MyModel.MyJsonObject.get("Status").equals("3") )
        	 MyModel.Json_Result_Msg =  "Page Category Has Not Been Entered";
        else if ( MyModel.MyJsonObject.get("Status").equals("4") )
        	 MyModel.Json_Result_Msg = "Page UserEmail {Owner} Has Not Been Entered";
        else if ( MyModel.MyJsonObject.get("Status").equals("5") )
        	 MyModel.Json_Result_Msg = "Page With Same Name Has Been Created Before";
        else if ( MyModel.MyJsonObject.get("Status").equals("6") )
        	 MyModel.Json_Result_Msg = "Page Has Been Created Successfully";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
}

	@POST	
	@Path("/WritePostTofriendtimeline")
	@Produces(MediaType.TEXT_PLAIN)
	public String WritePostTofriendtimeline( 
							  @FormParam("UserEmail") String UserEmail ,
							  @FormParam("PostPrivacy") String PostPrivacy ,
							  @FormParam("PostContent") String PostContent , 
							  @FormParam("Custom") String Custom ,
							  @FormParam("PostFeeling") String PostFeeling ,
							  @FormParam("PostFeelingDescription") String PostFeelingDescription ,
							  @FormParam("FriendEmail") String FriendEmail ,
							  @FormParam("HashTag") String HashTag
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/WritePostTofriendtimeline";
		String urlParameters = "UserEmail=" +  UserEmail + 
				               "&PostPrivacy=" + PostPrivacy +
				               "&PostContent=" + PostContent + 
				               "&Custom=" + Custom +
				               "&PostFeeling=" + PostFeeling + 
				               "&PostFeelingDescription=" + PostFeelingDescription +
				               "&FriendEmail=" + FriendEmail +
				               "&HashTag=" + HashTag ;

        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        MyModel.Json_Result_Msg = "Post Has Not Been Created Successfully." ;
        if ( MyModel.MyJsonObject.get("Status").equals("1") )
        	 MyModel.Json_Result_Msg = "UserEmail Is Empty.";        	 
        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg = "Post Privacy Not Selected.";
        else if ( MyModel.MyJsonObject.get("Status").equals("3") )
        	 MyModel.Json_Result_Msg =  "No Data In The Post";
        else if ( MyModel.MyJsonObject.get("Status").equals("4") )
        	 MyModel.Json_Result_Msg = "Custom Friends Are Not Specified.";        
        else if ( MyModel.MyJsonObject.get("Status").equals("5") )
       	 MyModel.Json_Result_Msg = "Post Feeling Description Not Inserted.";        
        else if ( MyModel.MyJsonObject.get("Status").equals("6") )
        	 MyModel.Json_Result_Msg = "Post Has Been Created Successfully";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
}
	///////////////////////////////////////////////////////////////
	@POST	
	@Path("/WritePostToyourtimeline")
	@Produces(MediaType.TEXT_PLAIN)
	public String WritePostToyourtimeline( 
							  @FormParam("UserEmail") String UserEmail ,
							  @FormParam("PostPrivacy") String PostPrivacy ,
							  @FormParam("PostContent") String PostContent , 
							  @FormParam("Custom") String Custom ,
							  @FormParam("PostFeeling") String PostFeeling ,
							  @FormParam("PostFeelingDescription") String PostFeelingDescription ,
							  @FormParam("FriendEmail") String FriendEmail , 
							  @FormParam("HashTag") String HashTag
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/WritePostTimeline";
		String urlParameters = "UserEmail=" +  UserEmail + 
				               "&PostPrivacy=" + PostPrivacy +
				               "&PostContent=" + PostContent + 
				               "&Custom=" + Custom +
				               "&PostFeeling=" + PostFeeling + 
				               "&PostFeelingDescription=" + PostFeelingDescription +
				               "&FriendEmail=" + FriendEmail +
        					   "&HashTag=" + HashTag ;
		
        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        MyModel.Json_Result_Msg = "Post Has Not Been Created Successfully." ;
        if ( MyModel.MyJsonObject.get("Status").equals("1") )
        	 MyModel.Json_Result_Msg = "UserEmail Is Empty.";        	 
        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg = "Post Privacy Not Selected.";
        else if ( MyModel.MyJsonObject.get("Status").equals("3") )
        	 MyModel.Json_Result_Msg =  "No Data In The Post";
        else if ( MyModel.MyJsonObject.get("Status").equals("4") )
        	 MyModel.Json_Result_Msg = "Custom Friends Are Not Specified.";        
        else if ( MyModel.MyJsonObject.get("Status").equals("5") )
       	 MyModel.Json_Result_Msg = "Post Feeling Description Not Inserted.";        
        else if ( MyModel.MyJsonObject.get("Status").equals("6") )
        	 MyModel.Json_Result_Msg = "Post Has Been Created Successfully";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
}

	////////////////////////////////////////////////////////

	@POST
	@Path("/ViewMyTimeLine")
	public Response ViewMyTimeLine( @FormParam("UserEmail") String UserEmail )
	{
		Vector<String> MyFriends = UserEntity.Get_MyFriends( UserEmail );
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		Vector<String> MyTimeLine = UserEntity.Show_MyTimeLine( UserEmail , MyFriends );
		MyMap.put( "MyTimeLine" , MyTimeLine );
		return Response.ok(new Viewable("/jsp/MyTimeLine.jsp" , MyMap ) ).build();
	}
	
	@POST	
	@Path("/LikePostID")
	@Produces(MediaType.TEXT_PLAIN)
	public String LikePostID( 
							  @FormParam("UserEmail") String UserEmail ,				
							  @FormParam("PostID") String PostID
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/LikePostID";
		String urlParameters = "UserEmail=" +  UserEmail + 
				               "&PostID=" + PostID ;

        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        MyModel.Json_Result_Msg = "Post Has Not Been Liked Successfully." ;
        if ( MyModel.MyJsonObject.get("Status").equals("1") )
        	 MyModel.Json_Result_Msg = "UserEmail Is Empty.";        	 
        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg = "Post ID Is Empty.";
        else if ( MyModel.MyJsonObject.get("Status").equals("5") )
       	     MyModel.Json_Result_Msg = "Post ID Was Not Found .";
        else if ( MyModel.MyJsonObject.get("Status").equals("6") )
      	     MyModel.Json_Result_Msg = "Post ID Liked Successfully .";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
  }
	
	/////////////////////////////////////////////////////////////////////
	@POST	
	@Path("/SharePost")
	@Produces(MediaType.TEXT_PLAIN)
	public String sharePostID( 
							  @FormParam("UserEmail") String UserEmail ,				
							  @FormParam("postid") String postid
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/SharePost";
		String urlParameters = "UserEmail=" +  UserEmail + 
				               "&postid=" + postid ;

        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        MyModel.Json_Result_Msg = "Post Has Not Been Shared Successfully." ;
        if ( MyModel.MyJsonObject.get("Status").equals("1") )
        	 MyModel.Json_Result_Msg = "UserEmail Is Empty.";        	 
        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg = "Post ID Is Empty.";
        else if ( MyModel.MyJsonObject.get("Status").equals("3") )
      	     MyModel.Json_Result_Msg = "Post ID Shared Successfully .";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
  }

	/////////////////////////////////////////////////////////////////////
	@POST
	@Path("/ViewPagesToLike")
	public Response ViewPagesToLike( @FormParam("UserEmail") String UserEmail )
	{
		Vector<String> MyFriends = UserEntity.Get_MyFriends( UserEmail );
		//String UserEmail = User.getCurrentActiveUser().getEmail();
		Map< String , Vector<String> > MyMap = new HashMap<String, Vector<String>>();
		Vector<String> Me = new Vector<String>();
		Me.add( UserEmail );
		MyMap.put("MyEmail", Me );
		
		Vector<String> PagesILiked = UserEntity.Get_PagesILike(UserEmail);
		Vector<String> PagesToLike = UserEntity.Get_PagesToLike( UserEmail , PagesILiked );
		PagesILiked.clear();
		MyMap.put( "PagesToLike" , PagesToLike );
		return Response.ok(new Viewable("/jsp/Pages/PagesToLike.jsp" , MyMap ) ).build();
	}

	@POST	
	@Path("/LikePage")
	@Produces(MediaType.TEXT_PLAIN)
	public String LikePage( 
							  @FormParam("UserEmail") String UserEmail ,				
							  @FormParam("PagesToLike") String PagesToLike
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/PagesToLike";
		String urlParameters = "UserEmail=" +  UserEmail + 
				               "&PagesToLike=" + PagesToLike ;

        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        MyModel.Json_Result_Msg = "Page Has Not Been Liked Successfully." ;
        if ( MyModel.MyJsonObject.get("Status").equals("1") )
        	 MyModel.Json_Result_Msg = "UserEmail Is Empty.";        	 
        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg = "Page Name Is Empty.";
        else if ( MyModel.MyJsonObject.get("Status").equals("3") )
      	     MyModel.Json_Result_Msg = "Page Has Been Liked Successfully .";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
  }
	
	@POST	
	@Path("/MyHashStatstics")
	@Produces(MediaType.TEXT_PLAIN)
	public String MyHashStatstics( 
								@FormParam("HashTag") String HashTag 
                            ) throws JSONException , ParseException  
{
		String serviceUrl = Domain_Name + "/rest/MyHashStatstics";
		String urlParameters = "HashTag=" +  HashTag;

        String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        
        System.out.println("retJson : " + retJson );
        MyModel.MyObject = MyModel.MyJsonParser.parse( retJson ) ;
        MyModel.MyJsonObject = (JSONObject) MyModel.MyObject;
        
        if ( MyModel.MyJsonObject.get("Status").equals("3") )
        	 {
         	  System.out.println("Here");
        	  MyModel.Json_Result_Msg = "HashTag stat : "+ HashTagStat.show(HashTag);
        	 }
        else if ( MyModel.MyJsonObject.get("Status").equals("1") )
       	 MyModel.Json_Result_Msg =  "No HashTag enterd";

        else if ( MyModel.MyJsonObject.get("Status").equals("2") )
        	 MyModel.Json_Result_Msg =  "No HashTag with this name IDIOT";
        
        MyModel.MyJsonObject.clear(); /** Remove Content Inside Json Object.*/
        return MyModel.Json_Result_Msg ;
}
}