package com.FCI.SWE.Models;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * This class contains User Attributes as name , id , email , password that can be used to describe 
 * and identify current user .
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */

public class User {
	private long id;
	private String name;
	private String email;
	private String password;
	
	private static User currentActiveUser;

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
	private User(String name, String email, String password) {
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
	 * This method is used to get Current Active User In The Website . 
	 * @return a <code>USer</code> representing Current Active User In The Website.   
	 */
	public static User getCurrentActiveUser(){
		return currentActiveUser;
	}
	
	/**
	 * This method is used to set Current Active User In The Website to null or make user 
	 * existence in the system no more case logout .    
	 */
	public static void resetCurrentActiveUser(){
		 currentActiveUser = null ;
	}
	
	/**
	 * This method is used to set Current Active User In The Website to some user .
	 * @param S which is the new user .    
	 */
	public static void setCurrentActiveUser( User S ){
		 currentActiveUser = S ;
	}
	
	/**
	 * 
	 * This static method will form UserEntity class using json format contains
	 * user data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	public static User getUser(String json) {

		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			currentActiveUser = new User(object.get("name").toString(), object.get(
					"email").toString(), object.get("password").toString());
			currentActiveUser.setId(Long.parseLong(object.get("id").toString()));
			return currentActiveUser;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * This Method is used to get user friend requests which sent to him 
	 * @param UserEmail The Current user in the system .
	 * @return <Code>Map</Code>
	 */
	public static Map<String,String> get_Friend_Requests( String UserEmail)
	{
	    Map<String, String> map = new HashMap<String, String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("FriendShipStatus");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		int counter = 0 ;
		for (Entity entity : pq.asIterable()) 
		{ 
			counter++;
			if ( entity.getProperty("UserEmail").toString().equals(UserEmail) ) 
			{
			  map.put( String.valueOf( counter++ ) , entity.getProperty("FriendEmail").toString() );
			  System.out.println("UserEmail : " + UserEmail + " , FriendEmail : " + entity.getProperty("FriendEmail").toString() );
			}
		}
	 return map ;
	 
	}

}
