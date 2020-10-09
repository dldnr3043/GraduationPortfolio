package com.example.final_dapp;

public class accountInfo {
    private String num;
    private String date;
    private String cost;
    private String place;
    private String type;
    private String DateTime_UTC;

    public accountInfo() {}

    public accountInfo(String num, String date, String cost, String place, String type, String DateTime_UTC) {
        this.num = num;
        this.date = date;
        this.cost = cost;
        this.place = place;
        this.type = type;
        this.DateTime_UTC = DateTime_UTC;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateTime_UTC() {
        return DateTime_UTC;
    }

    public void setDateTime_UTC(String DateTime_UTC) {
        this.DateTime_UTC = DateTime_UTC;
    }

    @Override
    public String toString() {
        return "accountInfo{" +
                "date='" + date + '\'' +
                ", cost=" + cost +
                ", place='" + place + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
