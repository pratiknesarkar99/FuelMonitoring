package com.example.fuelmonitoring.admin.fragments.wrapperclasses;

public class FuelPrices {
    private  String Kolhapur, Mumbai, Pune;

    public FuelPrices() {
    }

    public FuelPrices(String kolhapur, String mumbai, String pune) {
        Kolhapur = kolhapur;
        Mumbai = mumbai;
        Pune = pune;
    }

    public String getKolhapur() {
        return Kolhapur;
    }

    public void setKolhapur(String kolhapur) {
        Kolhapur = kolhapur;
    }

    public String getMumbai() {
        return Mumbai;
    }

    public void setMumbai(String mumbai) {
        Mumbai = mumbai;
    }

    public String getPune() {
        return Pune;
    }

    public void setPune(String pune) {
        Pune = pune;
    }
}
