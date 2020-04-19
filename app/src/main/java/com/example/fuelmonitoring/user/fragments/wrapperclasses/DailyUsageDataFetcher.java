package com.example.fuelmonitoring.user.fragments.wrapperclasses;

public class DailyUsageDataFetcher {
    private String amt, price;
    private  String date, time;

    public DailyUsageDataFetcher() {
    }

    public String getAmt() {
        return amt;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
