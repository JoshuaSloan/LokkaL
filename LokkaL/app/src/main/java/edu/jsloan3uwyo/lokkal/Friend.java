package edu.jsloan3uwyo.lokkal;

/**
 * Created by Kolby on 4/16/18.
 */

public class Friend {
    int FriendshipID;
    int PersonID;
    String PersonName;

    Friend(int fid, int pid, String n)
    {
        FriendshipID = fid;
        PersonID = pid;
        PersonName = n;
    }
}
