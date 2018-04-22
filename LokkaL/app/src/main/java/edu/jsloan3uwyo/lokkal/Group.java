package edu.jsloan3uwyo.lokkal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 4/18/2018.
 */

public class Group {
    int GroupID;
    String GroupName;
    public List<GroupMember> logm; //List Of Group Members


    Group(int gid, String gn)
    {
        GroupID = gid;
        GroupName = gn;
        logm = new ArrayList<GroupMember>();
    }
}
