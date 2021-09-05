package com.example.koreachessok;

import java.util.HashMap;
import java.util.Map;

public class GameRoom {
    String nickName = "empty";
    String nextNickName = "empty";
    String chattingRoomName = "empty";
    String nickNameRate = "empty";
    String nextNickNameRate = "empty";

    public GameRoom() {
    }

    public String getNickName(){
        return nickName;
    }

    public String getNextNickName(){
        return nextNickName;
    }

    public String getChattingRoomName(){
        return chattingRoomName;
    }

    public String getNickNameRate() { return nickNameRate; }

    public String getNextNickNameRate() { return nextNickNameRate; }

    public GameRoom(String nickName, String nickNameRank){
        this.nickName = nickName;
        this.nickNameRate = nickNameRank;
        this.nextNickName = "empty";
        this.nextNickNameRate = "empty";
        this.chattingRoomName = nickName + "chatting";

    }
    public GameRoom(String nickName, String nickNameRank, String nickName2, String nickName2Rank){
        this.nickName = nickName;
        this.nickNameRate = nickNameRank;
        this.nextNickName = nickName2;
        this.nextNickNameRate = nickName2Rank;
        this.chattingRoomName = nickName + "chatting";
    }

    public Map<String, Object> toMap (){
        HashMap<String, Object> toHashMap = new HashMap<>();
        toHashMap.put("nickName", nickName);
        toHashMap.put("nextNickName", nextNickName);
        toHashMap.put("nickNameRate", nickNameRate);
        toHashMap.put("nextNickNameRate", nextNickNameRate);
        return toHashMap;
    }
}
