package edu.jsloan3uwyo.lokkal;

/**
 * Created by Josh on 4/18/2018.
 */

public class GroupRequest {
    int GroupMemberID;
    int GroupID;
    String GroupName;
    String GroupCreatorName;

    GroupRequest(int gmid, int gid, String gn, String gcn)
    {
        GroupMemberID = gmid;
        GroupID = gid;
        GroupName = gn;
        GroupCreatorName = gcn;
    }

}
