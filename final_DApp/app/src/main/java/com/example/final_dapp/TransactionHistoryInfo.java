package com.example.final_dapp;

public class TransactionHistoryInfo {

    private String num;
    private String cost;
    private String place;
    private String type;
    private String date;

    public TransactionHistoryInfo() {}

    public TransactionHistoryInfo(String num, String cost, String place, String type, String date) {
        this.num = num;
        this.cost = cost;
        this.place = place;
        this.type = type;
        this.date = date;
    }


    public void setnum(String num) {
        this.num = num;
    }

    public void setcost(String cost) {
        this.cost = cost;
    }

    public void setplace(String place) {
        this.place = place;
    }

    public void settype(String type) { this.type = type; }

    public void setdate(String date) { this.date = date; }

    public String getnum(String num) {
        return num;
    }

    public String getcost(String cost) {
        return cost;
    }

    public String getplace(String place) {
        return place;
    }

    public String gettype(String type) { return type; }

    public String getdate(String date) { return date; }

}
