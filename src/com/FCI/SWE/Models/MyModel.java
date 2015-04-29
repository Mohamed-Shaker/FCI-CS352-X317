package com.FCI.SWE.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class MyModel {
 
	static DateFormat dateFormat ;
	static Calendar Cal ;
	
	static DatastoreService datastore ; 
	static Query gaeQuery ;
	static PreparedQuery pq ;
	static List<Entity> list ;
	
	public static JSONObject MyJsonObject = new JSONObject();
	public static JSONParser MyJsonParser = new JSONParser();
	public static Object MyObject = new Object();  
    
	public static String Json_Result_Msg = "" ;
	
	public static String Get_Current_DateTime()
	{
		//System.out.println(dateFormat.format(Cal.getTime())); //2014/08/06 16:00:22
		//String DateTime = dateFormat.format(Cal.getTime()).toString();
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Cal = Calendar.getInstance();
		return dateFormat.format(Cal.getTime()) ;
	}
	
	public static int Get_Specific_Data_Store_Size( String Table_Name )
	{
		datastore = DatastoreServiceFactory.getDatastoreService();
		gaeQuery = new Query( Table_Name );
		pq = datastore.prepare(gaeQuery);
		list = pq.asList(FetchOptions.Builder.withDefaults()); 
		
		return list.size() ;
		
	}
}
