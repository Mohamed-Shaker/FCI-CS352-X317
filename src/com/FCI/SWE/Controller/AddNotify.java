package com.FCI.SWE.Controller;

public class AddNotify implements NotificationShow {

	private NotificationNotify ntf;
	
	public AddNotify(NotificationNotify ntf)
	{
		this.ntf = ntf;
	}
	
	public void execute(String FriendEmail,String UserEmail){
		ntf.Add(FriendEmail, UserEmail);
	}
	}
