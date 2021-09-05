package com.example.koreachessok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameRoomAdapter extends BaseAdapter {
    ArrayList<GameRoom> gameRooms;
    LayoutInflater inflater;
    String userNickname, userNicknameRate;
    TextView gameList;
    GameRoomAdapter(ArrayList<GameRoom> gameRoom, LayoutInflater li, String nickName, String userRate){
        gameRooms = gameRoom;
        inflater = li;
        userNickname = nickName;
        userNicknameRate = userRate;
    }

    @Override
    public int getCount() {
        return gameRooms.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final GameRoom gameRoom = gameRooms.get(i);
        View itemView;

        itemView = inflater.inflate(R.layout.listgameroom, viewGroup,false);
        gameList = itemView.findViewById(R.id.gameRoom);
        if(gameRoom.getNextNickName().equals("empty")){
            gameList.setText("\n" + "\n    " + gameRoom.getNickName() + "(" + gameRoom.getNickNameRate() + ")");
            gameList.setBackgroundResource(R.drawable.waitingframe);
        } else {
            gameList.setText("\n" + "\n    " + gameRoom.getNickName() + "(" + gameRoom.getNickNameRate() + ")"
                    + " VS " + gameRoom.getNextNickName() + "(" + gameRoom.getNextNickNameRate() + ")");
            gameList.setBackgroundResource(R.drawable.ingframe);
        }
        return itemView;
    }
}