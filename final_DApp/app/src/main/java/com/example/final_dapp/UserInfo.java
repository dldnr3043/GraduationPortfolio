package com.example.final_dapp;


import java.util.HashMap;

public class UserInfo {
    public String mail;
    public String name;
    public String num;
    public String address;
    public String key;
    public String active;
    public String AuthNum;

    public UserInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public UserInfo(String num, String name, String key, String address, String mail, String active, String AuthNum) {
        this.mail = mail;
        this.name = name;
        this.num = num;
        this.address = address;
        this.key = key;
        this.active = active;
        this.AuthNum = AuthNum;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("num", num);
        result.put("name", name);
        result.put("address", address);
        result.put("key", key);
        result.put("mail", mail);
        result.put("active", active);
        result.put("AuthNum", AuthNum);

        return result;
    }
}
