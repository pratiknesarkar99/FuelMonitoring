package com.example.fuelmonitoring.user.fragments.wrapperclasses;

public class FeedbackForm {
    private  String mail, ans1, ans2;
    private float r1, r2, r3, r4;

    public FeedbackForm() {
    }

    public FeedbackForm(String mail, String ans1, String ans2, float r1, float r2, float r3, float r4) {
        this.mail = mail;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public float getR1() {
        return r1;
    }

    public void setR1(float r1) {
        this.r1 = r1;
    }

    public float getR2() {
        return r2;
    }

    public void setR2(float r2) {
        this.r2 = r2;
    }

    public float getR3() {
        return r3;
    }

    public void setR3(float r3) {
        this.r3 = r3;
    }

    public float getR4() {
        return r4;
    }

    public void setR4(float r4) {
        this.r4 = r4;
    }
}
