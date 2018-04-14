package edu.jsloan3uwyo.lokkal;

/**
 * Created by Kolby on 3/26/18.
 */

import java.io.Serializable;

public class Person implements Serializable{
    public String FirstName = "";
    public String LastName = "";
    public String DateOfBirth = "";
    public String Email = "";
    public String Password = "";
    public int PersonID = -1;

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
    }
}
