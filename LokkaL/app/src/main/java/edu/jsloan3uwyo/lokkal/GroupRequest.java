package edu.jsloan3uwyo.lokkal;

/**
 * Created by Josh on 4/18/2018.
 */

public class GroupRequest {
    int GroupMemberID;
    String GroupName;
    String GroupCreatorName;

    GroupRequest(int gmid, String gn, String gcn)
    {
        GroupMemberID = gmid;
        GroupName = gn;
        GroupCreatorName = gcn;
    }

}
