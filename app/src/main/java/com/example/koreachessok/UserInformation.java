package com.example.koreachessok;

import java.util.HashMap;
import java.util.Map;

public class UserInformation {
    String email;
    String password;
    String name;
    String nickName;

    public UserInformation(){
    }

    public String getNickName(){
        return nickName;
    }

    public UserInformation(String email, String password, String name, String nickName){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
    }

    public Map<String, Object> toMap (){
        HashMap<String, Object> toHashMap = new HashMap<>();
        toHashMap.put("email", email);
        toHashMap.put("password", password);
        toHashMap.put("name", name);
        toHashMap.put("nickName", nickName);
        return toHashMap;
    }
}
