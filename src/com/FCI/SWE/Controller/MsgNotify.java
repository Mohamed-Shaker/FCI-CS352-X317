package com.FCI.SWE.Controller;

public class MsgNotify implements NotificationShow {

	private NotificationNotify ntf ;
	
	
	public MsgNotify(NotificationNotify ntf)
	{
		this.ntf = ntf;
	}
	
	public void execute(String FriendEmail,String UserEmail){
		System.out.println("ntf Msg.");
		ntf.Msg(FriendEmail, UserEmail);
	}
}
