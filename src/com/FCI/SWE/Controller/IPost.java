package com.FCI.SWE.Controller;

public abstract class IPost {

	
	public abstract boolean  Post_This 
      (      String UserEmail , 
		     String PostPrivacy , 
		     String PostContent , 
		     String Custom , 
		     String PostFeeling , 
		     String PostFeelingDescription , 
		     String FriendEmail,
		     String HashTag
     );
	
	
	/*
	public void Post_On_My_TimeLine();
	public void Post_On_My_Friends_TimeLine();
	public void Post_On_Page();
	*/
	
}
