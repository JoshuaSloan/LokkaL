package edu.jsloan3uwyo.lokkal;


/**
 * Created by Kolby on 4/22/18.
 */

public class GroupMember {
    int GroupMemberID;
    String GroupMemberName;
    Double Latitude;
    Double Longitude;


    GroupMember(int gmid, String gmn, double lat, double lon)
    {
        GroupMemberID = gmid;
        GroupMemberName = gmn;
        Latitude = lat;
        Longitude = lon;

    }

}
