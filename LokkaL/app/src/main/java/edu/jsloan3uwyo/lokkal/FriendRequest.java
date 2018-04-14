package edu.jsloan3uwyo.lokkal;

/**
 * Created by Kolby on 4/14/18.
 */

public class FriendRequest {
   int FriendshipID;
   int LeftPersonID;
   String PersonName;

   FriendRequest(int fid, int pid, String n)
   {
        FriendshipID = fid;
        LeftPersonID = pid;
        PersonName = n;
   }
}
