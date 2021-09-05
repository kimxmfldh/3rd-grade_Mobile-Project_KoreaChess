package com.example.koreachessok;

import java.util.HashMap;
import java.util.Map;

public class Massage {
    String nickname;
    String msg;
    String date;
    String time;

    public Massage(String n, String m, String d, String t){
        nickname = n;
        msg = m;
        date = d;
        time = t;
    }
    public Massage(){}

    public String getNickname(){ return nickname; }

    public String getMsg(){
        return msg;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public Map<String, Object> toMap (){
        HashMap<String, Object> toHashMap = new HashMap<>();
        toHashMap.put("nickName", nickname);
        toHashMap.put("massage", msg);
        toHashMap.put("date", date);
        toHashMap.put("time", time);
        return toHashMap;
    }
}
