package com.FCI.SWE.Controller;

   public class NotificationInvoker 
   {

   public void placeNotifications(NotificationShow ntf,String FriendEmail,String UserEmail){

         ntf.execute(FriendEmail, UserEmail);

   }
}