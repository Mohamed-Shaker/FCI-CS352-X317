package com.FCI.SWE.Services;

public class Msg_Observer {
	
	private Msg_Subject MySubject ;
	private String Email ;
	public Msg_Observer( Msg_Subject S , String E )
	{
	  Email = E ;	
	  MySubject = S ;
	  MySubject.Add_Observer( this );
	}
	
	public void Update()
	{
	  System.out.println("Email : " + Email );	
	  System.out.println("Last Msg : " + MySubject.Get_Msg() );
	}

}
