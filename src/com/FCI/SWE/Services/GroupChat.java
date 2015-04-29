package com.FCI.SWE.Services;

import java.util.Vector;

import com.FCI.SWE.Models.MyModel;
import com.FCI.SWE.ServicesModels.Messages;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class GroupChat extends Msg_Subject {
    
	private Vector<Msg_Observer> MyUsers ;
	private String Chat_Users , Chat_DateTime , Chat_Sender , Chat_Current_Msg ;
	private int Chat_SharedKey = 1 ;
	
	public GroupChat()
	{
	 MyUsers = new Vector<Msg_Observer>();
	}
	public GroupChat( String Cusers , String CDT , String CCM , String CSender , int CSK )
	{
	 Chat_Users = Cusers ;
	 Chat_DateTime = CDT ;
	 Chat_Current_Msg = CCM ;
	 Chat_Sender = CSender ;
	 Chat_SharedKey = CSK ;
	 MyUsers = new Vector<Msg_Observer>();
	}

	
	public static void Sorter( Vector<GroupChat> MyMessages )
	{
	  String Temp_Str ;
	  int Temp_SK ;
	  
	 for(int i=0; i<MyMessages.size(); i++)
	 {
		 for(int r=i+1; r<MyMessages.size(); r++)
		 {
		   if( MyMessages.elementAt(i).Chat_SharedKey > MyMessages.elementAt(r).Chat_SharedKey )
		   {
			 Temp_Str = MyMessages.elementAt(i).Chat_Sender ;
			 MyMessages.elementAt(i).Chat_Sender = MyMessages.elementAt(r).Chat_Sender ;
			 MyMessages.elementAt(r).Chat_Sender = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Chat_Current_Msg ;
			 MyMessages.elementAt(i).Chat_Current_Msg = MyMessages.elementAt(r).Chat_Current_Msg ;
			 MyMessages.elementAt(r).Chat_Current_Msg = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Chat_DateTime ;
			 MyMessages.elementAt(i).Chat_DateTime = MyMessages.elementAt(r).Chat_DateTime ;
			 MyMessages.elementAt(r).Chat_DateTime = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Chat_Users ;
			 MyMessages.elementAt(i).Chat_Users = MyMessages.elementAt(r).Chat_Users ;
			 MyMessages.elementAt(r).Chat_Users = Temp_Str ;
			 
			 Temp_SK = MyMessages.elementAt(i).Chat_SharedKey ;
			 MyMessages.elementAt(i).Chat_SharedKey = MyMessages.elementAt(r).Chat_SharedKey ;
			 MyMessages.elementAt(r).Chat_SharedKey = Temp_SK ;
			 
		   }
		 } 
	 }
	}
	
	public String ToString()
	{
	 return "Group No : " + Chat_SharedKey 
			+"\nFrom : " + Chat_Sender 
			+"\nTo : " + Chat_Users  
			+"\nDateTime : " + Chat_DateTime
	        +"\nMessage : " + Chat_Current_Msg ;
	}
	
	public static void Display( Vector<GroupChat> MyMsg )
	{
	 for( int i=0; i<MyMsg.size(); i++ )
	 {
	  System.out.println( MyMsg.elementAt(i).ToString()  );
      System.out.println( MyMsg.elementAt(i).Chat_SharedKey );  	  
	  System.out.println("*********************************");
	 }
	}

	private boolean Check_Data_Store_Record_Group_Chat_Users( String[] FutureChat_Users , String GivenUsers) 
	{
		System.out.println("Starting Check_Data_Store_Record_Group_Chat_Users Function");
		System.out.println("Given Users : " + GivenUsers );
		boolean Has_Users = false ; 
		for( int i=0; i<FutureChat_Users.length; i++ )
		{
		 if( GivenUsers.contains( FutureChat_Users[i] ) )
			 {
			  System.out.println("Group Chat Members : " + FutureChat_Users[i]);
			  Has_Users = true;
			 }
		 else 
		 {
		  Has_Users = false;
		  return Has_Users ;
		 }
		}
		System.out.println("Ending Check_Data_Store_Record_Group_Chat_Users Function");
		return Has_Users;
	}
	
	public void Save_Group_Chat_Update()
	{
	  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
	  Query gaeQuery = new Query("Group_Chat");
	  PreparedQuery pq = datastore.prepare(gaeQuery);
     
	  String [] FutureChat_Users = Chat_Users.split( "," );
	  String Data_Store_Users ;
	  boolean Has_All_Users = false;
		
	  for ( Entity entity : pq.asIterable() ) 
		{ 
		  Data_Store_Users = entity.getProperty("Users").toString();
		  Has_All_Users = Check_Data_Store_Record_Group_Chat_Users( FutureChat_Users , Data_Store_Users);
		
		  if( Has_All_Users )
			{
			 Chat_SharedKey =  Integer.parseInt( entity.getProperty("SharedKey").toString());
			 Chat_Users = Data_Store_Users ; 
			 break;
			}
		}
	 	
	   int Entity_Msg_Key = MyModel.Get_Specific_Data_Store_Size("Group_Chat") + 1 ;	
	 
	   if( Has_All_Users != true )
	   {
	    Chat_SharedKey = Entity_Msg_Key ; 	 
	   }
	 
	   Entity Msg = new Entity("Group_Chat", Entity_Msg_Key );
	   Msg.setProperty("Sender", Chat_Sender );
	   Msg.setProperty("Users", Chat_Users);
	   Msg.setProperty("Msg_Content", Chat_Current_Msg);
	   Msg.setProperty("DateTime", Chat_DateTime);
	   Msg.setProperty("SharedKey", Chat_SharedKey);
	   datastore.put( Msg );	
  }



	@Override
	public void Set_Msg(String Msg) {
		Chat_Current_Msg = Msg ;
	}

	@Override
	public String Get_Msg() 
	{
		// TODO Auto-generated method stub
		return Chat_Current_Msg ;
	}

	@Override
	public void Notify_Observers() {
		
       for( int i=0; i<MyUsers.size(); i++ )
		 MyUsers.elementAt(i).Update();
	}

	@Override
	public void Remove_Observer(Msg_Observer O) {
		MyUsers.remove( O );
	}


	@Override
	public void Add_Observer(Msg_Observer O) {
		MyUsers.add( O );
	}
	
	public static Vector<String> Get_MyGroupChatMessages( String UserEmail )
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Group_Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		Vector<GroupChat> MyMessages = new Vector<GroupChat>();
		
		for (Entity entity : pq.asIterable()) 
		{ 
			if ( entity.getProperty("Users").toString().contains( UserEmail ) ) 
			{
			 String Sender = entity.getProperty("Sender").toString() ;
			 String Reciever = entity.getProperty("Users").toString() ;
			 String Msg_Content = entity.getProperty("Msg_Content").toString() ;
			 String Msg_DateTime = entity.getProperty("DateTime").toString() ;
			 int Msg_SharedKey = Integer.parseInt( entity.getProperty("SharedKey").toString() );
			 
			 MyMessages.add( new GroupChat( Reciever, Msg_DateTime, Msg_Content, Sender, Msg_SharedKey) );
			}
		}
	 GroupChat.Sorter( MyMessages );
	 
	 GroupChat.Display( MyMessages );
	 
	 Vector<String> My_Strs = new Vector<String>();
	 for( int i=0; i<MyMessages.size(); i++ )
	 {
	  My_Strs.add( MyMessages.elementAt(i).ToString() );
	  //My_Strs.add("\n");
	 }
	 return My_Strs ;
	}


	public static void Reply_To_Group_Chat(String userEmail , int groupChatID , String msgContent) 
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Group_Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery); 
		
		String Reciever = null ; 
		for (Entity entity : pq.asIterable() ) 
		{ 
			if ( Integer.parseInt( entity.getProperty("SharedKey").toString() )== groupChatID ) 
			{
			  Reciever = entity.getProperty("Users").toString() ;
			  break;
			}
		}
		
		int Entity_Msg_Key = MyModel.Get_Specific_Data_Store_Size("Group_Chat") + 1 ;
		Entity Msg = new Entity("Group_Chat", Entity_Msg_Key );
		Msg.setProperty("Sender", userEmail );
		Msg.setProperty("Users", Reciever );
		Msg.setProperty("Msg_Content", msgContent );
		Msg.setProperty("DateTime", MyModel.Get_Current_DateTime() );
		Msg.setProperty("SharedKey", groupChatID );
		datastore.put( Msg );	
	}
}
