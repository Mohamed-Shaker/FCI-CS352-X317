package com.FCI.SWE.Controller;

import java.util.List;

import com.FCI.SWE.Models.MyModel;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Post_FriendTimeLine extends IPost {

	public boolean Post_This
	        (
	         String UserEmail , 
             String PostPrivacy , 
             String PostContent , 
             String Custom , 
             String PostFeeling , 
             String PostFeelingDescription , 
             String FriendEmail ,
             String HashTag
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
CreatePost.setProperty("HashTag" , HashTag );

datastore.put( CreatePost );
return true;	
}
}
