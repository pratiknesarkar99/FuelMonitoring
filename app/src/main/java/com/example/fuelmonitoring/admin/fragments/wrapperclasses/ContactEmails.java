package com.example.fuelmonitoring.admin.fragments.wrapperclasses;

public class ContactEmails {
    private String mail;
    private  String date, time;

    public ContactEmails() {
    }

    public ContactEmails(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
