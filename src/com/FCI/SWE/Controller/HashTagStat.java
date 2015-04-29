package com.FCI.SWE.Controller;

import java.util.List;

import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class HashTagStat {
	
	public int show(String HashTagName)
	{

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery2 = new Query("hashtag");
		PreparedQuery q = datastore.prepare(gaeQuery2); 
		datastore = DatastoreServiceFactory.getDatastoreService();
		List<Entity> list2 = q.asList(FetchOptions.Builder.withDefaults());	
		int List_Size = list2.size() + 1 ;
		Entity hashtag = new Entity("hashtag", List_Size );
		int newNumber = 0;

		for (Entity entity : q.asIterable()) 
		{ 
			if (   entity.getProperty("Name").toString().equals(HashTagName)	  ) 
			{
				newNumber =Integer.parseInt(hashtag.getProperty("Number").toString() );

			}
			else
			{
				return 0;
			}

		}
		return newNumber;
	}

}
