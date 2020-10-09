package com.example.final_dapp;

public class PostInfo {

    private String num;
    private String title;
    private String content;
    private String date;

    public PostInfo() {}

    public PostInfo(String num, String title, String content, String date) {
        this.num = num;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public void setnum(String num) {
        this.num = num;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public void setdate(String date) { this.date = date; }

    public String getnum() {
        return num;
    }

    public String gettitle() {
        return title;
    }

    public String getcontent() {
        return content;
    }

    public String getdate() { return date; }

}
