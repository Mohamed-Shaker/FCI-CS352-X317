package com.FCI.SWE.Controller;

public class AcceptNotify implements NotificationShow {

	private NotificationNotify ntf;
	
	public AcceptNotify(NotificationNotify ntf)
	{
		this.ntf = ntf;
	}
	
	public void execute(String FriendEmail,String UserEmail){
		ntf.Accept(FriendEmail, UserEmail);
	}

}
