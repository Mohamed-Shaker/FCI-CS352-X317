package com.FCI.SWE.Services;

public abstract class Msg_Subject {
	
	
	public abstract void Remove_Observer( Msg_Observer O ); /* De-Attach */
	
	public abstract void Add_Observer ( Msg_Observer O ); /* Attach */
	
	public abstract void Set_Msg( String Msg ); 
	
	public abstract String Get_Msg();
	
	public abstract void Notify_Observers();
	

}
