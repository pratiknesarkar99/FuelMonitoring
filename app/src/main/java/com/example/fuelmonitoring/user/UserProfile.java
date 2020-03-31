package com.example.fuelmonitoring.user;

public class UserProfile {

    private  String fname, lname,  mobile,  city, state, mail, pass, confirm;
    private  String value;

    public UserProfile() {
    }


    public UserProfile(String fname, String lname, String mobile, String city, String state, String mail, String pass, String confirm) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.city = city;
        this.state = state;
        this.mail = mail;
        this.pass = pass;
        this.confirm = confirm;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
