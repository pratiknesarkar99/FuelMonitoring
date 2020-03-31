package com.example.fuelmonitoring.user.fragments;

public class UnameFetcher {

    private  String fname, lname;

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
}
