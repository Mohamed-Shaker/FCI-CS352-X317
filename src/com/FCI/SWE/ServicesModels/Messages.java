package com.FCI.SWE.ServicesModels;

import java.util.Vector;

public class Messages 
{

	String Sender , Reciever , Msg_Content , Msg_Seen_Not , Msg_DateTime ;
    int SharedKey ;
	
    public Messages()
    {
    	
    }
    
	public Messages( String S , String R , String MC , String MSN , String MDT , int SK )
	{
	 Sender = S;
	 Reciever = R;
	 Msg_Content = MC ;
	 Msg_Seen_Not = MSN ;
	 Msg_DateTime = MDT ;
	 SharedKey = SK ;
	}
	
	public static void Sorter( Vector<Messages> MyMessages )
	{
	  String Temp_Str ;
	  int Temp_SK ;
	  
	 for(int i=0; i<MyMessages.size(); i++)
	 {
		 for(int r=i+1; r<MyMessages.size(); r++)
		 {
		   if( MyMessages.elementAt(i).SharedKey > MyMessages.elementAt(r).SharedKey )
		   {
			 Temp_Str = MyMessages.elementAt(i).Sender ;
			 MyMessages.elementAt(i).Sender = MyMessages.elementAt(r).Sender ;
			 MyMessages.elementAt(r).Sender = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Reciever ;
			 MyMessages.elementAt(i).Reciever = MyMessages.elementAt(r).Reciever ;
			 MyMessages.elementAt(r).Reciever = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Msg_Content ;
			 MyMessages.elementAt(i).Msg_Content = MyMessages.elementAt(r).Msg_Content ;
			 MyMessages.elementAt(r).Msg_Content = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Msg_DateTime ;
			 MyMessages.elementAt(i).Msg_DateTime = MyMessages.elementAt(r).Msg_DateTime ;
			 MyMessages.elementAt(r).Msg_DateTime = Temp_Str ;
			 
			 Temp_Str = MyMessages.elementAt(i).Msg_Seen_Not ;
			 MyMessages.elementAt(i).Msg_Seen_Not = MyMessages.elementAt(r).Msg_Seen_Not ;
			 MyMessages.elementAt(r).Msg_Seen_Not = Temp_Str ;
			 
			 Temp_SK = MyMessages.elementAt(i).SharedKey ;
			 MyMessages.elementAt(i).SharedKey = MyMessages.elementAt(r).SharedKey ;
			 MyMessages.elementAt(r).SharedKey = Temp_SK ;
			 
		   }
		 } 
	 }
	}
	
	public String ToString()
	{
	 return  "From : " + Sender 
			+"\nTo : " + Reciever 
			+"\nDateTime : " + Msg_DateTime
	        +"\nMessage : " + Msg_Content ;
	}
	
	public static void Display( Vector<Messages> MyMsg )
	{
	 for( int i=0; i<MyMsg.size(); i++ )
	 {
	  System.out.println( MyMsg.elementAt(i).ToString()  );
      System.out.println( MyMsg.elementAt(i).SharedKey );  	  
	  System.out.println("*********************************");
	 }
	}
}
