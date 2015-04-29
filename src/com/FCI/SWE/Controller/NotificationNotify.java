package com.FCI.SWE.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.FCI.SWE.Models.MyModel;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class NotificationNotify {
	
public void NotificationDataStore(int ID,String FriendEmail , String UserEmail) 
{
	DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService(); 
	Query gaeQuery = new Query("Notifications");
	PreparedQuery pq = datastore.prepare(gaeQuery);
	List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

	Entity employee = new Entity("Notifications", list.size() + 1);

	employee.setProperty("NotificationID", ID);
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Calendar Cal = Calendar.getInstance();
	employee.setProperty( "DateTime" , dateFormat.format(Cal.getTime()).toString() );
	
	//System.out.println(dateFormat.format(Cal.getTime())); //2014/08/06 16:00:22
	
	employee.setProperty("NotificationDateTime", MyModel.Get_Current_DateTime());
	if(ID == 1)
	{
	employee.setProperty("Satement", FriendEmail+" Added you");
	employee.setProperty("NotificationOwner", UserEmail);
	}
	else if(ID == 2)
	{
	employee.setProperty("Satement", FriendEmail+" Accepted you");
	employee.setProperty("NotificationOwner", UserEmail);
	}
	else if (ID == 3)
	{
	employee.setProperty("Satement", FriendEmail+" Messeaged you");
	employee.setProperty("NotificationOwner", UserEmail);
	}
	datastore.put(employee);
}

public void Add(String FriendEmail, String UserEmail) {
	NotificationDataStore(1,FriendEmail,UserEmail);
	System.out.print("Added") ;
	
}
public void Accept(String FriendEmail, String UserEmail){
	NotificationDataStore(2,FriendEmail,UserEmail);
	System.out.print("Accepted") ;
	
}
public void Msg(String FriendEmail, String UserEmail) {
	System.out.println("Msg Notification.");
	NotificationDataStore(3,FriendEmail,UserEmail);
	System.out.print("Hey ,  yout have a msg ") ;
	
}
	
}
