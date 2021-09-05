package com.example.koreachessok;

import java.util.HashMap;
import java.util.Map;

public class GameMove {
    String koreaChessName;
    String before_x;
    String before_y;
    String after_x;
    String after_y;
    String myName;

    public GameMove(){
    }

    public String getMyName() { return myName; }

    public String getKoreaChessName(){
        return koreaChessName;
    }

    public String getBefore_x(){
        return before_x;
    }

    public String getBefore_y(){
        return before_y;
    }

    public String getAfter_x(){ return after_x; }

    public String getAfter_y(){
        return after_y;
    }

    public GameMove(String chessName, String x, String y, String a, String b, String myName){
        this.koreaChessName = chessName;
        this.before_x = x;
        this.before_y = y;
        this.after_x = a;
        this.after_y = b;
        this.myName = myName;
    }

    public Map<String, Object> toMap (){
        HashMap<String, Object> toHashMap = new HashMap<>();
        toHashMap.put("koreaChessName", koreaChessName);
        toHashMap.put("before_x", before_x);
        toHashMap.put("before_y", before_y);
        toHashMap.put("after_x", after_x);
        toHashMap.put("after_y", after_y);
        toHashMap.put("myName", myName);
        return toHashMap;
    }
}
