package com.example.koreachessok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainService extends AppCompatActivity {
    Intent it;
    TextView insertUserNickName, insertUserWinScore, insertUserDefeatScore, insertUserRank;
    Button makeNewGameRoom;
    String userEmail;
    String userNickName;
    String userWinScore;
    String userDefeatScore;
    String userRank;
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference root;
    static ArrayList<GameRoom> gameRooms = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainservice);

        it = getIntent();
        userEmail = it.getStringExtra("it_email");

        makeNewGameRoom = findViewById(R.id.makeNewRoom);
        makeNewGameRoom.setBackgroundResource(R.drawable.button_default_layout);
        listView = findViewById(R.id.listView_gameRoomArea);

        final GameRoomAdapter gameRoomAdapter = new GameRoomAdapter(gameRooms, getLayoutInflater(), userNickName, userRank);
        listView.setAdapter(gameRoomAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        root = firebaseDatabase.getReference();
        DatabaseReference childRef = root.child("GameRooms");
        childRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GameRoom gameRoom = snapshot.getValue(GameRoom.class);
                gameRooms.add(gameRoom);
                gameRoomAdapter.notifyDataSetChanged();
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GameRoom gameRoom = snapshot.getValue(GameRoom.class);
                String getNickName = gameRoom.getNickName();
                String getNickNameRate = "";
                for(int i = 0; i < gameRooms.size(); i++){
                    if(gameRooms.get(i).getNickName().equals(getNickName)){
                        getNickNameRate = gameRooms.get(i).getNickNameRate();
                        gameRooms.remove(i);
                    }
                }
                gameRoom.nickNameRate = getNickNameRate;
                gameRooms.add(gameRoom);
                gameRoomAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GameRoom gameRoom = snapshot.getValue(GameRoom.class);
                assert gameRoom != null;
                String getNickName = gameRoom.getNickName();
                for(int i = 0; i < gameRooms.size(); i++){
                    if(gameRooms.get(i).getNickName().equals(getNickName)){
                        gameRooms.remove(i);
                        gameRoomAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        makeNewGameRoom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                enterGameRoomFirst(userNickName);
                makeNewGameRoom(userNickName, userRank);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( cheakGameRoom(gameRooms.get(i).getNextNickName()) ) {
                    enterGameRoomNext(userNickName, userRank, gameRooms.get(i).getNickName(), gameRooms.get(i).getNickNameRate());
                    Toast.makeText(MainService.this, gameRooms.get(i).getNickName() + "의 방에 입장하셨습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainService.this, "게임중 입니다...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getUserInfo(userEmail);
    }

    /////////////////////////////////////////////////////// 게임 방 관련 메소드.
    public void makeNewGameRoom(String userNickName, String userRank) { // 새로운 게임방 생성.
        GameRoom gameRoom = new GameRoom(userNickName, userRank);

        String md5NickNameRoom = ChangeEmailMD5.encrypt(userNickName + "GAMEROOM");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference newGameRoom = firebaseDatabase.getReference("GameRooms");
        HashMap<String, Object> gameRoomMap = new HashMap<>();

        gameRoomMap.put(md5NickNameRoom, gameRoom.toMap());
        newGameRoom.updateChildren(gameRoomMap);
    }//방만들기 매소드

    public void enterGameRoomFirst(String nickName) { // 게임방 입장 하는 메소드.
        String userScore = calculateUserScore(userWinScore, userDefeatScore);

        Intent it = new Intent(this, GameService.class);
        it.putExtra("nickname", nickName);
        it.putExtra("gameTurn", "one");
        it.putExtra("userScore", userScore);
        it.putExtra("win", userWinScore);
        it.putExtra("defeat", userDefeatScore);
        startActivity(it);
    }// 첫번째 입장 매소드 (방장)

    public void enterGameRoomNext(String nickName, String nickNameRank, String otherNickName, String otherNickNameRank) { // 게임방 입장 하는 메소드.
        GameRoom gameRoom = new GameRoom(otherNickName, otherNickNameRank, nickName, nickNameRank);

        String md5NickNameRoom = ChangeEmailMD5.encrypt(otherNickName + "GAMEROOM");
        final DatabaseReference newGameRoom = firebaseDatabase.getReference("GameRooms");
        HashMap<String, Object> gameRoomMap = new HashMap<>();

        gameRoomMap.put(md5NickNameRoom, gameRoom.toMap());
        newGameRoom.updateChildren(gameRoomMap);

        String userScore = calculateUserScore(userWinScore, userDefeatScore);
        Intent it = new Intent(getApplicationContext(), GameService.class);
        it.putExtra("nickname", nickName);
        it.putExtra("otherNickname", otherNickName);
        it.putExtra("gameTurn", "two");
        it.putExtra("userScore", userScore);
        it.putExtra("win", userWinScore);
        it.putExtra("defeat", userDefeatScore);
        startActivity(it);
    }//두번째 입장 매소드 (참가자.)

    public boolean cheakGameRoom(String otherNickName) {
        return otherNickName.equals("empty");
    }// 클릭한 방에 입장 할 수 있는 지 결정해주는 메소드.

    /////////////////////////////////////////////////////// 게임이용자 정보 가져오기.
    public void getUserInfo (String userEmail) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        String USER_PATH = "userLoginInformation";//닉네임 가져오기
        assert userEmail != null;
        String md5 = ChangeEmailMD5.encrypt(userEmail);
        DatabaseReference userRef = db.getReference(USER_PATH + "/" + md5);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInformation userInformation = snapshot.getValue(UserInformation.class);
                assert userInformation != null;
                setUserInfo(userInformation);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }// 유저의 이메일로 유저 정보 세팅 하는 메소드.

    public void setUserInfo (UserInformation user) {
        insertUserNickName = findViewById(R.id.insertNickName);
        userNickName = user.getNickName();
        getUserGameInfo(userNickName);// 닉네임으로 게임정보 가져오기.
        insertUserNickName.setText(userNickName);
    }// 이메일로 닉네임 가져오기.

    public void getUserGameInfo(String inputNickName){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String USER_GAME_PATH = "userGameInformation";//닉네임 가져오기
        userNickName = inputNickName;
        assert userNickName != null;
        String md5 = ChangeEmailMD5.encrypt(userNickName);
        final DatabaseReference userRef = db.getReference(USER_GAME_PATH + "/" + md5);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserGameInformation gameInformation = snapshot.getValue(UserGameInformation.class);
                assert gameInformation != null;
                if(gameInformation != null) {
                    setUserGameScore(gameInformation);
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }// 유저의 게임 스코어를 셋팅하는 메소드

    public void setUserGameScore(UserGameInformation gameInfo){
        insertUserWinScore = findViewById(R.id.insertwinScore);
        insertUserDefeatScore = findViewById(R.id.insertdefeatScore);
        insertUserRank = findViewById(R.id.insertRank);
        userWinScore = gameInfo.getWinScore();
        userDefeatScore = gameInfo.getDefeatScore();
        userRank = gameInfo.getRank();

        insertUserWinScore.setText(userWinScore);
        insertUserDefeatScore.setText(userDefeatScore);
        insertUserRank.setText(userRank);
    }// 유저의 게임 스코어를 가져오는 메소드

    public String calculateUserScore(String win, String defeat) {
        String score = "";
        int winScore = Integer.parseInt(win);
        int defeatScore = Integer.parseInt(defeat);

        int winningRate = (winScore * 100) / (winScore + defeatScore);
        score = score + winningRate + "%";
        return score;
    }// 유저의 게임 스코어 계산.l
}
