package com.FCI.SWE.Services;


import java.util.Vector;

import javax.swing.text.DefaultEditorKit.CutAction;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.FCI.SWE.Controller.*;
import com.FCI.SWE.Models.MyModel;
import com.FCI.SWE.Models.User;
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
@Produces(MediaType.TEXT_PLAIN)
public class UserServices {
	
	private NotificationNotify Notify = new NotificationNotify();
	private static NotificationInvoker Invoke = new NotificationInvoker();
	private static AddNotify addN ;
	private static AcceptNotify acceptN ;
	
	private static Msg_Subject MyMsg_Subject ;
	private static Vector<Msg_Observer> MyMsg_Observers;
	
	IPost post;
	
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
	public String registrationService(
			                          @FormParam("uname") String uname,
			                          @FormParam("email") String email, 
			                          @FormParam("password") String pass
			                          ) 
	{
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
	@SuppressWarnings("unchecked")
	@POST
	@Path("/SendFriendRequest")
	public String SendFriendRequest( @FormParam("UserEmail") String UserEmail ,
						             @FormParam("FriendEamil") String FriendEmailToAdd 
						           ) 
	{	
		JSONObject object = new JSONObject();
		
		if( UserEmail.compareTo( FriendEmailToAdd ) == 0 )
		   object.put("Status", "You Can't Add Yourself" );
		
		else if( !UserEntity.Check_User_Friend( FriendEmailToAdd )  )
			    object.put("Status", "Friend Email Does Not Exist" );	
		else 
		{
		  UserEntity.Send_Friend_Request(UserEmail, FriendEmailToAdd);
		  addN = new AddNotify(Notify);
		  Invoke.placeNotifications(addN, UserEmail,FriendEmailToAdd);
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
			acceptN = new AcceptNotify(Notify);
			Invoke.placeNotifications(acceptN, FriendEmail , UserEmail);
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
	
	/**
	 * This Method is used to make user able to send a single message to anyone of his friends 
	 * @param UserEmail    Email Of User Who Sends The Message  
	 * @param FriendEmail  FriendEmail To Send The Message To 
	 * @param MsgContent   Message Content To Send And Be Retrieved 
	 * @return <Code>String</Code>
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/SendMsg")
	public String SendMsg( 
            			   @FormParam("UserEmail") String UserEmail ,
            			   @FormParam("FriendEmail") String FriendEmail , 
            			   @FormParam("MsgContent") String MsgContent
						 )
	{
		JSONObject object = new JSONObject();
		if( UserEmail == null || FriendEmail == null )
		{
		 object.put( "Status" , "Msg Has Not Been Sent" );	
		}
		else   
		{
		  
		  MyMsg_Subject = new GroupChat();
		  String [] MyUsersNames = FriendEmail.split(",");
		  MyMsg_Observers = new Vector<Msg_Observer>();
		  for( int i=0;i<MyUsersNames.length; i++)
		  {
			MyMsg_Observers.add( new Msg_Observer( MyMsg_Subject , MyUsersNames[i] ) );
			MyMsg_Subject.Add_Observer( MyMsg_Observers.elementAt(i) );
			MyMsg_Subject.Set_Msg( MsgContent );
		    MyMsg_Subject.Notify_Observers();
		  }
		  
		  MsgNotify msgN = new MsgNotify(Notify);
		  Invoke.placeNotifications(msgN, UserEmail,FriendEmail);
		 
          UserEntity.Send_Message( UserEmail , FriendEmail , MsgContent );
          object.put("Status", "Msg Has Been Sent");
		}
		return object.toString();	
	}
	
	/**
	 * This Method is used to make user able to send a single message to anyone of his friends 
	 * @param UserEmail    Email Of User Who Sends The Message  
	 * @param FriendEmail  FriendEmail To Send The Message To 
	 * @param MsgContent   Message Content To Send And Be Retrieved 
	 * @return <Code>String</Code>
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/SendGroupChat")
	public String SendGroupChat( 
            			   @FormParam("UserEmail") String UserEmail ,
            			   @FormParam("FriendsEmail") String FriendsEmail , 
            			   @FormParam("MsgContent") String MsgContent
						 )
	{
		System.out.println("Msg Group Chat Service.");
		JSONObject object = new JSONObject();
		if( UserEmail == null || FriendsEmail == null )
		{
		 object.put( "Status" , " Group Chat Msg Has Not Been Sent" );	
		}
		else   
		{
		 String DateTime = MyModel.Get_Current_DateTime();
			
		 GroupChat GC = new GroupChat( FriendsEmail, DateTime, MsgContent, UserEmail , -1 );
		 GC.Save_Group_Chat_Update(); 
		 
         //UserEntity.AddNog( UserEmail , "U Send Messge To F "  , Fried , 1 )
		 object.put("Status" , "Group Chat Msg Has Been Sent");
		 MsgNotify msgN = new MsgNotify(Notify);
		 Invoke.placeNotifications(msgN, UserEmail,FriendsEmail);
	  }
		return object.toString();
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/ReplyToGroupChat")
	public String ReplyToGroupChat( 
								   @FormParam("UserEmail") String UserEmail ,
								   @FormParam("GroupChatNumber") int GroupChatID , 
								   @FormParam("MsgContent") String MsgContent
  		   						  )
	{
		System.out.println("ReplyToGroupChat Services.");
		System.out.println( "With Group Number : " + GroupChatID );
		JSONObject object = new JSONObject();
		if( UserEmail == null || GroupChatID <1 )
		   object.put( "Status" , "1" );	
		
		else   
		{		
		 GroupChat.Reply_To_Group_Chat( UserEmail , GroupChatID , MsgContent );
		 object.put("Status" , "2");
		 //MsgNotify msgN = new MsgNotify(Notify);
		 //Invoke.placeNotifications(msgN, UserEmail,FriendEmail);
		}
		return object.toString();
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/CreatePage")
	public String CreatePage( 
							  @FormParam("pname") String pname ,
			  				  @FormParam("type") String type ,
			  				  @FormParam("category") String category ,
			  				  @FormParam("UserEmail") String UserEmail 
   						    ) throws JSONException
	{
		if( pname.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "1" );	
		else if( type.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "2" );
		else if( category.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "3" );
		else if( UserEmail.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "4" );
		else   
		{		
		  if( UserEntity.Create_page(UserEmail, pname, type, category) )
			  MyModel.MyJsonObject.put( "Status" , "6" );
		  else  MyModel.MyJsonObject.put( "Status" , "5" );
		}
		
		return MyModel.MyJsonObject.toString();
	}
		
	@SuppressWarnings("unchecked")
	@POST
	@Path("/WritePostTofriendtimeline")
	public String WritePostTofriendtimeline( 
							  @FormParam("UserEmail") String UserEmail ,
			  				  @FormParam("PostPrivacy") String PostPrivacy ,
			  				  @FormParam("PostContent") String PostContent , 
			  				  @FormParam("Custom") String Custom ,
							  @FormParam("PostFeeling") String PostFeeling ,
							  @FormParam("PostFeelingDescription") String PostFeelingDescription ,
							  @FormParam("FriendEmail") String FriendEmail , 
							  @FormParam("HashTag") String HashTag
   						    ) throws JSONException
	{
		IPost newPost = new Post_FriendTimeLine();
		if( UserEmail.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "1" );	
		else if( PostPrivacy.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "2" );
		else if( PostContent.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "3" );
		else if( Custom.compareTo("") == 0 && PostPrivacy.compareTo("Custom") == 0)
			MyModel.MyJsonObject.put( "Status" , "4" );
		else if( PostFeeling.compareTo("") != 0 && PostFeelingDescription.compareTo("") == 0)
			MyModel.MyJsonObject.put( "Status" , "5" );
		else   
		{		
		  if( newPost.Post_This(UserEmail, PostPrivacy, PostContent, Custom , PostFeeling , PostFeelingDescription , FriendEmail , HashTag ) )
			  MyModel.MyJsonObject.put( "Status" , "6" );
		  if( HashTag.compareTo("") != 0 )
			  {
			    int Size = HashTagStat.show( HashTag ) + 1 ;
			    UserEntity.Add_hashtag( HashTag , Size );
			  }
		    
		}
		return MyModel.MyJsonObject.toString();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/WritePostTimeline")
	public String WritePostTIMELINE(
			                  @FormParam("UserEmail") String UserEmail	,		  				  
			                  @FormParam("PostPrivacy") String PostPrivacy ,
			  				  @FormParam("PostContent") String PostContent , 
			  				  @FormParam("Custom") String Custom ,
							  @FormParam("PostFeeling") String PostFeeling ,
							  @FormParam("PostFeelingDescription") String PostFeelingDescription ,
							  @FormParam("FriendEmail") String FriendEmail ,
							  @FormParam("HashTag") String HashTag 
   						    ) throws JSONException
	{
		Post_Timeline newPost = new Post_Timeline();
		if( UserEmail.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "1" );	
		else if( PostPrivacy.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "2" );
		
		else if( PostContent.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "3" );
		
		else if( Custom.compareTo("") == 0 && PostPrivacy.compareTo("Custom") == 0)
			MyModel.MyJsonObject.put( "Status" , "4" );
		else if( PostFeeling.compareTo("") != 0 && PostFeelingDescription.compareTo("") == 0)
			MyModel.MyJsonObject.put( "Status" , "5" );
		else   
		{		
		  if( newPost.Post_This(UserEmail, PostPrivacy, PostContent, Custom, PostFeeling, PostFeelingDescription, FriendEmail , HashTag))
			  MyModel.MyJsonObject.put( "Status" , "6" );
		  if( HashTag.compareTo("") != 0 )
		  {
		    int Size = HashTagStat.show( HashTag ) + 1 ;
		    UserEntity.Add_hashtag( HashTag , Size );
		  }
		  
		}
		return MyModel.MyJsonObject.toString();
	}



	@SuppressWarnings("unchecked")
	@POST
	@Path("/LikePostID")
	public String LikePostID( 
			  				  @FormParam("UserEmail") String UserEmail ,				
			  				  @FormParam("PostID") String PostID
   						    ) throws JSONException
	{
		if( UserEmail.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "1" );	
		else if( PostID.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "2" );
		else   
		{		
		  if( UserEntity.LikePostID( UserEmail , PostID ) )
			  MyModel.MyJsonObject.put( "Status" , "6" );
		  else MyModel.MyJsonObject.put( "Status" , "5" );
		}
		return MyModel.MyJsonObject.toString();
	}
	
	@POST
	@Path("/SharePost")
	public String SharePost ( 
			                  @FormParam("UserEmail") String UserEmail ,
						      @FormParam("postid") String postid 
						    ) 
	{
		if( UserEmail.compareTo("") == 0 ) 
			MyModel.MyJsonObject.put( "Status" , "1" );	
		long Post_Id = Long.parseLong( postid.toString() );
		if( Post_Id <= 0 )
			MyModel.MyJsonObject.put( "Status" , "2" );
		else   
		{
			UserEntity.SharePostID( UserEmail , Post_Id );
			MyModel.MyJsonObject.put( "Status" , "3" );
		}
		return MyModel.MyJsonObject.toString();
	}
	
	

	@SuppressWarnings("unchecked")
	@POST
	@Path("/PagesToLike")
	public String PagesToLike( 
			  				   @FormParam("UserEmail") String UserEmail ,				
			  				   @FormParam("PagesToLike") String PagesToLike
   						     ) throws JSONException
	{
		if( UserEmail.compareTo("") == 0 ) 
			MyModel.MyJsonObject.put( "Status" , "1" );	
		else if( PagesToLike.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "2" );
		else   
		{		
		  if( UserEntity.LikePage( UserEmail , PagesToLike ) )
			  MyModel.MyJsonObject.put( "Status" , "3" );
		  else MyModel.MyJsonObject.put( "Status" , "4" );
		}
		return MyModel.MyJsonObject.toString();
	}
		
	@SuppressWarnings("unchecked")
	@POST
	@Path("/MyHashStatstics")
	public String MyHashStatstics(						  
							     @FormParam("HashTag") String HashTag 
   						       ) throws JSONException
	{
		System.out.println( "HT : " + HashTag  );
		System.out.println("Errorrr : " + HashTagStat.show(HashTag));	 
		if( HashTag.compareTo("") == 0 )
			MyModel.MyJsonObject.put( "Status" , "1" );	
		else   
		{
		  if( HashTagStat.show(HashTag) == 0 )
			   MyModel.MyJsonObject.put( "Status" , "2" );
		  else MyModel.MyJsonObject.put( "Status" , "3" );
		}
		return MyModel.MyJsonObject.toString();
	}
	
}