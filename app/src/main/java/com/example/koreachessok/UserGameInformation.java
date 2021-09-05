package com.example.koreachessok;

import java.util.HashMap;
import java.util.Map;

public class UserGameInformation {
    String nickName;
    String winScore;
    String defeatScore;
    String rank;

    public UserGameInformation(){

    }

    public String getNickName() { return nickName; }

    public String getWinScore(){
        return winScore;
    }

    public String getDefeatScore(){ return defeatScore; }

    public String getRank(){
        return rank;
    }

    public UserGameInformation(String nickName, String winScore, String defeatScore, String rank){
        this.nickName = nickName;
        this.winScore = winScore;
        this.defeatScore = defeatScore;
        this.rank = rank;
    }

    public Map<String, Object> toMap (){
        HashMap<String, Object> toHashMap = new HashMap<>();
        toHashMap.put("nickName", nickName);
        toHashMap.put("winScore", winScore);
        toHashMap.put("defeatScore", defeatScore);
        toHashMap.put("rank", rank);
        return toHashMap;
    }
}
