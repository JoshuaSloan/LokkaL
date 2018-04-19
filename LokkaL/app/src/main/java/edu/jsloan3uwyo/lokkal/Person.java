package edu.jsloan3uwyo.lokkal;

import java.io.Serializable;

/**
 * Created by Kolby on 3/26/18.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable{

    public String FirstName = "";
    public String LastName = "";
    public String DateOfBirth = "";
    public String Email = "";
    public String Password = "";
    public int PersonID = -1;
    public List<FriendRequest> lofr; //List Of Friend Requests
    public List<Friend> lof; //List Of Friends
    public List<GroupRequest> logr; //List Of Group Requests
    public Group myGroup; //User's current group

    //Empty Constructor
    Person()
    {
    }

    Person(int i, String fn, String ln, String dob, String e, String p) {
        PersonID = i;
        FirstName = fn;
        LastName = ln;
        DateOfBirth = dob;
        Email = e;
        Password = p;
        lofr = new ArrayList<FriendRequest>();
        lof = new ArrayList<Friend>();
        logr = new ArrayList<GroupRequest>();
        myGroup = new Group();
    }
}
