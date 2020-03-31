package com.example.fuelmonitoring.admin;

public class AdminProfile {
    private  String mail, pass, confirm;

    public AdminProfile() {
    }

    public AdminProfile(String mail, String pass, String confirm) {
        this.mail = mail;
        this.pass = pass;
        this.confirm = confirm;
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
