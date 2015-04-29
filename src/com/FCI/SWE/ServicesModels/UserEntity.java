package com.FCI.SWE.ServicesModels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.FCI.SWE.Models.MyModel;
import com.FCI.SWE.Models.User;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity {
	private String name;
	private String email;
	private String password;
	private long id;
	/**
	 * Command DP
	 */
	
	//private static NotificationNotify Notify;
	//static AddNotify addN = new AddNotify(Notify);
	//static MsgNotify msgN = new MsgNotify(Notify);
	//static AcceptNotify acceptN = new AcceptNotify(Notify);
	//static NotificationInvoker Invoke = new NotificationInvoker();
	/**
	 * Command DP
	 */

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	/**
	 * This method is used to set the id for the user. 
	 * @param id representing id of the user .   
	 */
	private void setId(long id){
		this.id = id;
	}
	
	/**
	 * This method is used to get the id of the user . 
	 * @return a <code>long</code> representing User ID .   
	 */
	public long getId(){
		return id;
	}

	/**
	 * This method is used to get the name of the user . 
	 * @return a <code>String</code> representing User Name .   
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method is used to get the email of the user . 
	 * @return a <code>String</code> representing User email .   
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * This method is used to get the password of the user . 
	 * @return a <code>String</code> representing User Password .   
	 */
	public String getPass() {
		return password;
	}

	
	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable() ) 
		{
			if (entity.getProperty("name").toString().equals(name) && entity.getProperty("password").toString().equals(pass)) 
			{
			  UserEntity returnedUser = new UserEntity
					                   ( entity.getProperty("name").toString() , 
					                     entity.getProperty("email").toString() , 
					                     entity.getProperty("password").toString()
					                   );
				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService(); 
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity employee = new Entity("users", list.size() + 1);

		employee.setProperty("name", this.name);
		employee.setProperty("email", this.email);
		employee.setProperty("password", this.password);
		
		datastore.put(employee);
		return true;

	}
	
	/**
	 *  Check Friend If Or Not Has Registered In The System . 
	 *  @param FriendEmail Friend Email To Check For Existence . 
	 *  @return <Code>FriendEmail</Code>
	 */
	public static boolean Check_User_Friend( String FriendEmail )
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable() ) 
		{
			if (entity.getProperty("email").toString().equals( FriendEmail )) 
			{
			  System.out.println("Friend Email Has Been Found , You Can Add Him.");	
			  return true ;	
			}
			 
		}
	 return false ;
	}
	
	/**
	 * 
	 * This static method will save the send friend request on the datastore .
	 * 
	 * @param UserEmail the user email who sends friend request
	 * @param FriendEmail the friend email who in the future maybe accept that request .
	 * 
	 * @return <code>Boolean</code> which returns true if the sending process is successful .
	 */
	public static boolean Send_Friend_Request( String UserEmail , String FriendEmail )
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService(); 
		Query gaeQuery = new Query("FriendShipStatus");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity FriendRequestRelationShip = new Entity("FriendShipStatus", list.size() + 1);

		FriendRequestRelationShip.setProperty("FriendEmail", FriendEmail );
		FriendRequestRelationShip.setProperty("UserEmail", UserEmail );
		FriendRequestRelationShip.setProperty("Status", "Pending");
		datastore.put(FriendRequestRelationShip);
		
		//Invoke.placeNotifications(addN, UserEmail , FriendEmail);
		return true;	
	}
	
	/**
	 * 
	 * This static method will accept friend request which is send to user when he/she open his/her home page .
	 * @param UserEmail the user email who will accept friend request .
	 * @param FriendEmail who will be notified about acceptance of send request.
	 * @return <code>Boolean</code> which returns true if the accept friend request process is successful .
	 */
	public static boolean Accept_Add_Friend_Request( String UserEmail , String FriendEmail )
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("FriendShipStatus");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		int counter = 0 ;
		for (Entity entity : pq.asIterable()) 
		{ 
			counter++;
			if (   entity.getProperty("UserEmail").toString().equals(UserEmail) 
			    && entity.getProperty("FriendEmail").toString().equals(FriendEmail) 
			    && entity.getProperty("Status").toString().equals("Pending") ) 
			{
			  Entity FriendAcceptRelationShip = new Entity( "FriendShipStatus" , counter);
			 // System.out.println("Key : " + entity.getKey().getId());
			  FriendAcceptRelationShip.setProperty( "FriendEmail" , FriendEmail);
			  FriendAcceptRelationShip.setProperty( "UserEmail" , UserEmail );
			  FriendAcceptRelationShip.setProperty( "Status" , "Accept" );
			  datastore.put(FriendAcceptRelationShip);
			  
			  //Invoke.placeNotifications(acceptN, UserEmail,FriendEmail);
			  return true;
			}
		}
		return false;	
	}
	

	/**
	 * This Method is used to get user friend requests which sent to him 
	 * @param UserEmail The Current user in the system .
	 * @return <Code>Map</Code>
	 */
	public static Vector<String> get_Friend_Requests( String UserEmail)
	{
	    Vector<String> FriendEmails = new Vector<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("FriendShipStatus");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		for (Entity entity : pq.asIterable()) 
		{ 
			if (   entity.getProperty("FriendEmail").toString().equals(UserEmail) 
			    && entity.getProperty("Status").toString().equals("Pending")	
			  ) 
			{
			  FriendEmails.add( entity.getProperty("UserEmail").toString() );			  
			  System.out.println("UserEmail : " + UserEmail + " , FriendEmail : " + entity.getProperty("UserEmail").toString() );
			}
		}
	 return FriendEmails ;
	}
	
	/**
	 * This Method is used to get user friends  
	 * @param UserEmail The Current user in the system .
	 * @return <Code>Map</Code>
	 */
	public static Vector<String> Get_MyFriends( String UserEmail)
	{
		System.out.println( "Get_MyFriends Function : " + UserEmail );
	    Vector<String> MyFriends = new Vector<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("FriendShipStatus");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		for (Entity entity : pq.asIterable()) 
		{ 
			if (   
				   entity.getProperty("FriendEmail").toString().equals(UserEmail)
				&& entity.getProperty("Status").toString().equals("Accept")
			   )
			{
			  MyFriends.add( entity.getProperty("UserEmail").toString() );	
			  //map.put( String.valueOf( counter++ ) , entity.getProperty("FriendEmail").toString() );
			  System.out.println("UserEmail : " + entity.getProperty("UserEmail") + " , FriendEmail : " + entity.getProperty("FriendEmail").toString() );
			}
			else if 
			   (
				   entity.getProperty("UserEmail").toString().equals(UserEmail)
				&& entity.getProperty("Status").toString().equals("Accept")
			   )
			  
			{
			   MyFriends.add( entity.getProperty("FriendEmail").toString() );	
			  //map.put( String.valueOf( counter++ ) , entity.getProperty("FriendEmail").toString() );
			  System.out.println("UserEmail : " + entity.getProperty("UserEmail") + " , FriendEmail : " + entity.getProperty("FriendEmail").toString() );
			}
			//entity.getProperty("UserEmail").toString().equals(UserEmail)
		}
	 return MyFriends ;	 
	}
	
	public static void Send_Message( String UserEmail , String FriendEmail , String MsgContent )
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Single_Messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		long ID = 1 ;
		boolean No_Message_Before = false ;
		for (Entity entity : pq.asIterable()) 
		{ 
			if ( 
				 (
				     entity.getProperty("FriendEmail").toString().equals(FriendEmail)  
				  && entity.getProperty("UserEmail").toString().equals(UserEmail)
				 )
				 ||
			    (
				     entity.getProperty("FriendEmail").toString().equals(UserEmail)  
				  && entity.getProperty("UserEmail").toString().equals(FriendEmail)
				)
			  )
		    {
			 ID = Integer.parseInt( entity.getProperty("SharedID").toString() ) ;
			 No_Message_Before = true ;
			 break;
			}
		}
		
		datastore = DatastoreServiceFactory.getDatastoreService();
		gaeQuery = new Query("Single_Messages");
		pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());	
		int List_Size = list.size() + 1 ;
		
		if( No_Message_Before == false )
		{
			System.out.println("Here.");
			ID = List_Size ;
		}
		
		System.out.println( "ID : " + ID + " , List Size : " + List_Size );
		Entity Single_Message = new Entity("Single_Messages", List_Size );

		Single_Message.setProperty("UserEmail", UserEmail );
		Single_Message.setProperty("FriendEmail", FriendEmail );
		Single_Message.setProperty("MsgContent", MsgContent );
		Single_Message.setProperty("SharedID", ID );
		Single_Message.setProperty("Seen-NotSeen", "NotSeen" );
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar Cal = Calendar.getInstance();
		System.out.println(dateFormat.format(Cal.getTime())); //2014/08/06 16:00:22
		//Single_Message.setProperty( "DateTime" , dateFormat.format(Cal.getTime()).toString() );
		
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//Date date = new Date();
		//System.out.println( "Current Msg Time : " + dateFormat.format(date));
		//Single_Message.setProperty( "DateTime" , dateFormat.format(date).toString() );
		
		Single_Message.setProperty( "DateTime" , dateFormat.format(Cal.getTime()).toString() );
		datastore.put( Single_Message );	
		//Invoke.placeNotifications(msgN, UserEmail,FriendEmail);
	}
	////////////////////////////////////////////////
	public static void Add_hashtag( String hashtagname , int number  )
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("hashtag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		datastore = DatastoreServiceFactory.getDatastoreService();
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());	
		int List_Size = list.size() + 1 ;
		Entity hashtag = new Entity("hashtag", List_Size );
		hashtag.setProperty("Name", hashtagname );
		hashtag.setProperty("Number", number );
		datastore.put(hashtag);

		}
	/////////////////////////////////////////////////
	
	public static Vector<String> Get_MyMessages( String UserEmail)
	{
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Single_Messages");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		
		Vector<Messages> MyMessages = new Vector<Messages>();
		
		for (Entity entity : pq.asIterable()) 
		{ 
			if (   entity.getProperty("FriendEmail").toString().equals(UserEmail) 
				|| entity.getProperty("UserEmail").toString().equals(UserEmail)
			   ) 
			{
			 String Sender = entity.getProperty("UserEmail").toString() ;
			 String Reciever = entity.getProperty("FriendEmail").toString() ;
			 String Msg_Content = entity.getProperty("MsgContent").toString() ;
			 String Msg_DateTime = entity.getProperty("DateTime").toString() ;
			 String Msg_Seen_Not = entity.getProperty("Seen-NotSeen").toString() ;
			 int Msg_SharedKey = Integer.parseInt( entity.getProperty("SharedID").toString() );
			 
			 MyMessages.add( new Messages( Sender , Reciever , Msg_Content , Msg_Seen_Not , Msg_DateTime , Msg_SharedKey ) );
			}
		}
	 Messages.Sorter( MyMessages );
	 
	 Messages.Display( MyMessages );
	 
	 Vector<String> My_Strs = new Vector<String>();
	 for( int i=0; i<MyMessages.size(); i++ )
	 {
	  My_Strs.add( MyMessages.elementAt(i).ToString() );
	  //My_Strs.add("\n");
	 }
	 return My_Strs ;	 
	}
	
	public static Vector<String> Get_My_Notification ( String UserEmail )
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Notifications");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		
		Vector<String> MyNotification = new Vector<String>();
		
		
		for (Entity entity : pq.asIterable()) 
		{ 
			if ( entity.getProperty("NotificationOwner").toString().equals(UserEmail) )  
			{
			 System.out.println ( entity.getProperty("Satement").toString() ) ;
			 MyNotification.add( entity.getProperty("Satement").toString() );
			}
		}
		
	 return MyNotification ;
	}
	
	public static boolean Create_page( String UserEmail , String pagename , String type , String categorey)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : pq.asIterable()) 
			if ( entity.getProperty("PageName").toString().equals( pagename ) )  
			    return false ;				
		
		Entity creationpage = new Entity("Page", list.size() + 1);
		creationpage.setProperty("UserEmail", UserEmail);
		creationpage.setProperty("PageName", pagename );
		creationpage.setProperty("Type", type );
		creationpage.setProperty("Categorey", categorey);
		creationpage.setProperty("DateTime", MyModel.Get_Current_DateTime() );
		
		datastore.put(creationpage);
		return true;	
	}

	public static boolean Create_Post( 
			 					       String UserEmail , 
			 					       String PostPrivacy , 
			 					       String PostContent , 
			 					       String Custom , 
			 					       String PostFeeling , 
			 					       String PostFeelingDescription , 
			 					       String FriendEmail
			 					     ) 
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		Entity CreatePost = new Entity("Posts", list.size() + 1);
		CreatePost.setProperty("UserEmail", UserEmail);
		CreatePost.setProperty("PostPrivacy", PostPrivacy );
		CreatePost.setProperty("PostContent", PostContent );
		CreatePost.setProperty("Custom", Custom);
		CreatePost.setProperty("DateTime", MyModel.Get_Current_DateTime() );
		CreatePost.setProperty("PostFeeling", PostFeeling);
		CreatePost.setProperty("PostFeelingDescription" , PostFeelingDescription );
		CreatePost.setProperty("Likes" , 0 );
		CreatePost.setProperty("FriendEmail" , FriendEmail );
		datastore.put( CreatePost );
		return true;	
	}

	public static Vector<String> Show_MyTimeLine(String UserEmail , Vector<String> MyFriends ) 
	{
		Vector<String> MyTimeLine = new Vector<String>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		int MyFriendsSize = MyFriends.size() , Counter = 1 ;
		String Str = "" ;
		int Posts_Size = MyModel.Get_Specific_Data_Store_Size("Posts");
		System.out.println( "Posts Size : " + Posts_Size ); 
		if( Posts_Size != 0 )
		{
		  for (Entity entity : pq.asIterable()) 
		    for( int i=0; i<MyFriendsSize; i++ )
			 if ( 
				     entity.getProperty("UserEmail").toString().equals( MyFriends.elementAt(i) ) 
			      || entity.getProperty("FriendEmail").toString().equals( UserEmail ) 
			      || entity.getProperty("UserEmail").toString().equals( UserEmail )
			    )  
			    {
				 MyTimeLine.add( "(" + Counter++ + ")" );
				 Str = "Post ID : " + entity.getKey().getId() ;
				 MyTimeLine.add( Str );
				 Str = "Post Owner : " + entity.getProperty("UserEmail").toString();
				 MyTimeLine.add( Str );
				 Str = "Post Privacy : " + entity.getProperty("PostPrivacy").toString();
				 MyTimeLine.add( Str );
				 Str = "Post Feelings : " + entity.getProperty("PostFeeling").toString();
				 MyTimeLine.add( Str );
				 Str = "Post Feelings Description : " + entity.getProperty("PostFeelingDescription").toString();
				 MyTimeLine.add( Str );
				 Str = "Post Content : " + entity.getProperty("PostContent").toString();
				 MyTimeLine.add( Str );
				 Str = "#Likes : " + entity.getProperty("Likes").toString();
			     MyTimeLine.add( Str );
			     MyTimeLine.add( "" );
			    }
		}
		return MyTimeLine;	
	}

	public static boolean LikePostID(String UserEmail, String PostID) 
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		int Pid = Integer.parseInt( PostID );
		for ( Entity entity : pq.asIterable() ) 
			{
			 if ( entity.getKey().getId() == Pid )
			 {
				Entity Like = new Entity("Posts", Pid );
				Like.setProperty("UserEmail", entity.getProperty("UserEmail").toString() );
				Like.setProperty("PostPrivacy", entity.getProperty("PostPrivacy").toString() );
				Like.setProperty("PostContent", entity.getProperty("PostContent").toString() );
				Like.setProperty("Custom", entity.getProperty("Custom").toString() );
				Like.setProperty("DateTime", entity.getProperty("DateTime").toString() );
				Like.setProperty("PostFeeling", entity.getProperty("PostFeeling").toString() );
				Like.setProperty("PostFeelingDescription" , entity.getProperty("PostFeelingDescription").toString() );
                Pid = Integer.parseInt(entity.getProperty("Likes").toString() ) ;
				Like.setProperty("Likes" , Pid + 1 );	
				datastore.put( Like );
				return true;	
			 }
			}
			 
		return false;	
	}

	public static Vector<String> Get_PagesILike(String UserEmail) 
	{
		Vector<String> PagesUserLiked = new Vector<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("PagesUserLiked");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable() ) 
		{
			if ( entity.getProperty("UserEmail").toString().equals( UserEmail ) )  
				{
				  PagesUserLiked.add( entity.getProperty("PageName").toString() );
				}
		}
		System.out.println("Pages I Liked");
		System.out.println( PagesUserLiked.toString() ) ;
		return PagesUserLiked ;		
	}

	public static Vector<String> Get_PagesToLike(String UserEmail,Vector<String> PagesILiked) 
	{
		Vector<String> PagesToLike = new Vector<String>();		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable() ) 
			  if( !PagesILiked.contains( entity.getProperty("PageName").toString() ) )	
				  {
				   System.out.println(  entity.getProperty("PageName").toString() );
				   PagesToLike.add( entity.getProperty("PageName").toString() );		
				  }
		return PagesToLike ;		
	}

	public static boolean LikePage(String UserEmail, String PagesToLike) 
	{		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("PagesUserLiked");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		Entity LikePage = new Entity("PagesUserLiked", list.size() + 1);
		LikePage.setProperty("UserEmail" , UserEmail );
		LikePage.setProperty("PageName" , PagesToLike );
		LikePage.setProperty("DateTime", MyModel.Get_Current_DateTime() );
		datastore.put( LikePage );
		return true;
	}

	
	public static boolean sharePostID(String UserEmail, String PostID) 
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		int Pid = Integer.parseInt( PostID );
		for ( Entity entity : pq.asIterable() ) 
			{
			 if ( entity.getKey().getId() == Pid )
			 {
				Entity share = new Entity("Posts", Pid );
				share.setProperty("UserEmail", entity.getProperty("UserEmail").toString() );
				share.setProperty("PostPrivacy", entity.getProperty("PostPrivacy").toString() );
				share.setProperty("PostContent", entity.getProperty("PostContent").toString() );
				share.setProperty("Custom", entity.getProperty("Custom").toString() );
				share.setProperty("DateTime", entity.getProperty("DateTime").toString() );
				share.setProperty("PostFeeling", entity.getProperty("PostFeeling").toString() );
				share.setProperty("PostFeelingDescription" , entity.getProperty("PostFeelingDescription").toString() );
                Pid = Integer.parseInt(entity.getProperty("Likes").toString() ) ;
                share.setProperty("share" , Pid + 1 );	
				datastore.put( share );
				return true;	
			 }
			}
			 
		return false;	
	}
	
}
	