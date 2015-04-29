package com.FCI.SWE.Controller;
import com.FCI.SWE.Models.User;

public class Post {
	
	IPost MyPost ;
	User MyUser ;

	public Post( IPost Ip , User U )
	{
	 MyPost = Ip;
	 MyUser = U;
	}

	public void Set_MyPost( IPost Ip )
	{
	 MyPost = Ip;	
	}
	
	/*
	public void PostPost()
	{
	  MyPost.Post_This( MyUser.getEmail() , MyPost.Privacy , MyPost.Privacy );
	}
	*/
}

/*
String UserEmail , 
String PostPrivacy , 
String PostContent , 
String Custom , 
String PostFeeling , 
String PostFeelingDescription , 
String FriendEmail,
String HashTag
*/