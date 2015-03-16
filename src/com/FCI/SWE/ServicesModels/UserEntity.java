package com.FCI.SWE.ServicesModels;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
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
}
