package com.example.fuelmonitoring.user.fragments.wrapperclasses;

public class UnameFetcher {

    private  String fname, lname;
    private  String city;

    public UnameFetcher() { }

    public UnameFetcher(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getCity() {
        return city;
    }
}
