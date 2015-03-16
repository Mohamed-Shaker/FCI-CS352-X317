package com.FCI.SWE.ServicesModels;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * 
 * This class handles every thing which is related to any process 
 * needed to be done with the social network groups as creating , updating . 
 * @author Eng.Mohammed Samir
 *
 */
public class GroupEntity {
	private String name;
	private String description;
	private String privacy;
	private long ownerId;

	/**
	 * This method is used to get the name of the group. 
	 * @return a <code>String</code> representing Group Name .   
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * This method is used to get the Description of the group. 
	 * @return a <code>String</code> representing Group Description .   
	 */
	public String getDesc(){
		return description;
	}
	
	/**
	 * This method is used to get the privacy of the group. 
	 * @return a <code>String</code> representing Group Privacy .   
	 */
	public String getPrivacy(){
		return privacy;
	}
	
	/**
	 * This method is used to get the owner id which is the user who created the group. 
	 * @return a <code>long</code> representing Group Privacy .   
	 */
	public long getOwnerId(){
		return ownerId;
	}
	
	/**
	 * This method is used to set the name for the group. 
	 * @param name representing new name of the group .   
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * This method is used to set the description for the group. 
	 * @param desc representing new description of the group .   
	 */
	public void setDescription(String desc){
		this.description = desc;
	}
	
	/**
	 * This method is used to set the privacy for the group. 
	 * @param privacy representing new privacy of the group .   
	 */
	public void setPrivacy(String privacy){
		this.privacy = privacy;
	}
	
	/**
	 * This method is used to set the owner of the group. 
	 * @param id representing id of the owner of this group .   
	 */
	public void setOwnerId(long id){
		this.ownerId = id;
	}
	
	/**
	 * This method is used to save a new group to datastore. 
	 * @return <code>Boolean</code> which returns true if the saving process if successful  .   
	 */
	public Boolean saveGroup() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("groups");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity group = new Entity("groups", list.size() + 1);

		group.setProperty("name", this.name);
		group.setProperty("description", this.description);
		group.setProperty("privacy", this.privacy);
		group.setProperty("owner_id",this.ownerId);
		
		if(datastore.put(group).isComplete())
			return true;
		else return false;

	}




}
