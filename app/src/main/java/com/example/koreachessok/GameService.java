package com.example.koreachessok;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.StringTokenizer;

public class GameService extends AppCompatActivity {
    Intent it;
    TextView myNickName,myWinningRate, otherNickName, otherWinningRate;
    Button setTurnButton, goChattingButton, giveUpButton, outButton;
    DatabaseReference root;
    String getNickName, getWin, getDefeat, getOtherNickName, getOtherWin, getOtherDefeat, selectTeam, roomMaster;
    Button [][] koreaChessButton = new Button[10][9];
    static String move = "";
    static boolean turnCheck = false;
    CheckKoreaChessFunction checkKoreaChessFunction = new CheckKoreaChessFunction();// 장기판 기능.

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_frame);
        final String userName;
        final String userScore;
        final String otherName;

        it = getIntent();
        final String userTurn = it.getStringExtra("gameTurn");
        if(userTurn.equals("one")) {
            userName = it.getStringExtra("nickname");
            userScore = it.getStringExtra("userScore");
            getNickName = userName;
            getWin = it.getStringExtra("win");
            getDefeat = it.getStringExtra("defeat");
            otherName = "empty";
            selectTeam = "han";
            roomMaster = userName;
            turnCheck = false;
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 9; j++) {
                    String buttonKey = "button" + i + j;
                    int resID = getResources().getIdentifier(buttonKey, "id", "com.example.koreachessok");
                    koreaChessButton[i][j] = findViewById(resID);
                    checkKoreaChessFunction.startButtonImageHan(selectTeam, koreaChessButton, i, j);
                }
            }

        } else {
            userName = it.getStringExtra("nickname");
            userScore = it.getStringExtra("userScore");
            otherName = it.getStringExtra("otherNickname");
            getNickName = userName;
            getWin = it.getStringExtra("win");
            getDefeat = it.getStringExtra("defeat");
            selectTeam = "cho";
            roomMaster = otherName;
            turnCheck = true;
            getOtherWinningRate(otherName);
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 9; j++) {
                    String buttonKey = "button" + i + j;
                    int resID = getResources().getIdentifier(buttonKey, "id", "com.example.koreachessok");
                    koreaChessButton[i][j] = findViewById(resID);
                    checkKoreaChessFunction.startButtonImageCho(selectTeam, koreaChessButton, i, j);
                }
            }
        }

        myNickName = findViewById(R.id.myNickName);
        myWinningRate = findViewById(R.id.myWinningRate);
        otherNickName = findViewById(R.id.otherNickName);
        otherWinningRate = findViewById(R.id.otherWinningRate);

        setTurnButton = findViewById(R.id.setTurnButton);// 게임 디비에 장기말이름, 이동 정 후 위치값 저장해야함.
        goChattingButton = findViewById(R.id.chattingButton);// 채팅방으로 이동해야함.
        giveUpButton = findViewById(R.id.giveUpButton);// 디비에서 게임장 정보 삭제해야함, 디비에 승, 패 갱신해야함.
        outButton = findViewById(R.id.outButton);// 디비에서 게임방 정보 삭제해야함.
        setTurnButton.setBackgroundResource(R.drawable.button_default_layout);
        goChattingButton.setBackgroundResource(R.drawable.button_default_layout);
        giveUpButton.setBackgroundResource(R.drawable.button_default_layout);
        outButton.setBackgroundResource(R.drawable.button_default_layout);
        setKoreaChessButton(selectTeam, koreaChessButton);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        root = firebaseDatabase.getReference();
        DatabaseReference databaseReference = root.child("GameRooms");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GameRoom gameRoom = snapshot.getValue(GameRoom.class);
                if (gameRoom.getNickName().equals(userName)){
                    String nextNickName = gameRoom.getNextNickName();
                    otherNickName.setText(nextNickName);
                    otherWinningRate.setText("00%");
                    if(nextNickName.equals("empty") != true){
                        getOtherWinningRate(nextNickName);
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(int i = 0; i < MainService.gameRooms.size(); i++){
                    String roomName;
                    if(userTurn.equals("one")) {
                        roomName = userName;
                    } else {
                        roomName = otherName;
                    }
                    if(MainService.gameRooms.get(i).getNickName().equals(roomName)){
                        count++;
                    }
                }
                if(count == 0){
                    finish();
                }
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        String md5NickNameRoom = ChangeEmailMD5.encrypt(roomMaster + "GAMEROOM");
        String USER_GAME_PATH = "GameRooms/" + md5NickNameRoom + "/GAME";
        DatabaseReference databaseReference2 = firebaseDatabase.getReference().child(USER_GAME_PATH);
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GameMove gameMove = snapshot.getValue(GameMove.class);
                assert gameMove != null;
                String before_x = gameMove.getBefore_x();
                String before_y = gameMove.getBefore_y();
                String after_x = gameMove.getAfter_x();
                String after_y = gameMove.getAfter_y();
                String moveChess = gameMove.getKoreaChessName();
                String myName = gameMove.getMyName();
                int changeBefore_x = 8-Integer.parseInt(before_x);
                int changeBefore_y = 9-Integer.parseInt(before_y);
                int changeAfter_x = 8-Integer.parseInt(after_x);
                int changeAfter_y = 9-Integer.parseInt(after_y);
                if(!getNickName.equals(myName)){
                    String otherTeam;
                    if(selectTeam.equals("cho")) {
                        otherTeam = "han";
                    } else {
                        otherTeam = "cho";
                    }
                    if(koreaChessButton[changeBefore_y][changeBefore_x].getText().toString().contains(otherTeam) &&
                    koreaChessButton[changeAfter_y][changeAfter_x].getText().toString().contains("King")) {
                        turnCheck = false;
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameService.this);
                        builder.setTitle("알림!!!").setMessage(userName + "님의 패배 입니다.!!!");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateDefeatScore(getNickName, getWin, getDefeat);
                                updateWinScore(getOtherNickName, getOtherWin, getOtherDefeat);
                                if(userTurn.equals("one")) {
                                    outButtonFunctionONE(userName);
                                    finish();
                                } else {
                                    outButtonFunctionONE(otherName);
                                    finish();
                                }
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(GameService.this, userName + "님의 차례입니다!!", Toast.LENGTH_SHORT).show();
                    }
                    koreaChessButton[changeBefore_y][changeBefore_x].setBackgroundResource(R.drawable.button_default_layout);
                    koreaChessButton[changeBefore_y][changeBefore_x].setText("empty");
                    changeImageKoreaChess(moveChess, changeAfter_x, changeAfter_y);
                    koreaChessButton[changeAfter_y][changeAfter_x].setText(moveChess);
                    if(!move.equals("")) {
                        if (move.length() > 15) {
                            StringTokenizer stk = new StringTokenizer(move, "/");
                            String beforeKoreaChess = stk.nextToken().toString().trim();
                            int before_X = Integer.parseInt(stk.nextToken().toString().trim());//j
                            int before_Y = Integer.parseInt(stk.nextToken().toString().trim());//i
                            String afterKoreaChess = stk.nextToken().toString().trim();
                            int after_X = Integer.parseInt(stk.nextToken().toString().trim());
                            int after_Y = Integer.parseInt(stk.nextToken().toString().trim());
                            changeImageKoreaChess(beforeKoreaChess, before_X, before_Y);
                            changeImageKoreaChess(afterKoreaChess, after_X, after_Y);
                        } else {
                            StringTokenizer stk = new StringTokenizer(move, "/");
                            String beforeKoreaChess = stk.nextToken().toString().trim();
                            int before_X = Integer.parseInt(stk.nextToken().toString().trim());//j
                            int before_Y = Integer.parseInt(stk.nextToken().toString().trim());//i
                            changeImageKoreaChess(beforeKoreaChess, before_X, before_Y);
                        }
                        move = "";
                    }
                    turnCheck = true;
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        outButton.setOnClickListener(new View.OnClickListener() {// 나가기 버튼 구현.
            @Override
            public void onClick(View view) {
                if(userTurn.equals("one")) {
                    outButtonFunctionONE(userName);
                    finish();
                } else {
                    outButtonFunctionTWO(otherName);
                    finish();
                }
            }
        });

        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// 항복 버튼 구현.
                AlertDialog.Builder alert = new AlertDialog.Builder(GameService.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDefeatScore(getNickName, getWin, getDefeat);
                        updateWinScore(getOtherNickName, getOtherWin, getOtherDefeat);
                        if(userTurn.equals("one")) {
                            outButtonFunctionONE(userName);
                            finish();
                        } else {
                            outButtonFunctionONE(otherName);
                            finish();
                        }
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setMessage("항복 하시겠습니까?");
                alert.show();
            }
        });// 항복 버튼.

        goChattingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geChattingService(getNickName, getOtherNickName, userTurn);
            }
        });

        setTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turnCheck){
                    if(move.length() > 15) {
                        setMyChess(move, koreaChessButton, myNickName.getText().toString());
                        System.out.println("두기");
                    } else { }
                }
            }
        });

        myNickName.setText(userName);
        myWinningRate.setText(userScore);
        otherNickName.setText(otherName);
        otherWinningRate.setText("00%");
    }

    void outButtonFunctionONE(String userName){// 게임 db삭제하고, mainService로 돌아가야 함.
        String nickName = userName;
        String md5NickNameRoom = ChangeEmailMD5.encrypt(nickName + "GAMEROOM");

        System.out.println(nickName);
        System.out.println(md5NickNameRoom);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(  "GameRooms/" + md5NickNameRoom);
        databaseReference.removeValue();
    }// 방장이 나가기 버튼 누를 경우.

    void outButtonFunctionTWO(String otherName){
        GameRoom gameRoom = new GameRoom(otherName, "empty");
        String md5NickNameRoom = ChangeEmailMD5.encrypt(otherName + "GAMEROOM");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference newGameRoom = firebaseDatabase.getReference("GameRooms");
        HashMap<String, Object> gameRoomMap = new HashMap<>();

        gameRoomMap.put(md5NickNameRoom, gameRoom.toMap());
        newGameRoom.updateChildren(gameRoomMap);
    }// 두번째 입장한사람이 나가기 버튼 누를경우.

    void getOtherWinningRate(String inputUserName){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String USER_GAME_PATH = "userGameInformation";//닉네임 가져오기
        assert inputUserName != null;
        String md5 = ChangeEmailMD5.encrypt(inputUserName);
        DatabaseReference userRef = db.getReference(USER_GAME_PATH + "/" + md5);

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserGameInformation gameInformation = snapshot.getValue(UserGameInformation.class);
                assert gameInformation != null;
                otherWinningRate.setText(calculateUserScore(gameInformation));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }// 상대 승률 set하기.

    String calculateUserScore(UserGameInformation userInfo) {
        String score = "";
        int winScore = Integer.parseInt(userInfo.getWinScore());
        int defeatScore = Integer.parseInt(userInfo.getDefeatScore());

        helper(userInfo.getNickName(), userInfo.getWinScore(), userInfo.getDefeatScore());

        int winningRate = (winScore * 100) / (winScore + defeatScore);
        score = score + winningRate + "%";
        return score;
    }// 유저의 게임 스코어 계산.

    void helper(String name, String win, String defeat) {
        this.getOtherNickName = name;
        this.getOtherWin = win;
        this.getOtherDefeat = defeat;
    }// 전역변수에 값 넣어주기.

    void updateDefeatScore(String name, String win, String defeat){
        int defeatScore = Integer.parseInt(defeat) + 1;
        String afterDefeatScore = "" + defeatScore;
        String md5StringNickName = ChangeEmailMD5.encrypt(name);
        final UserGameInformation gameInformation = new UserGameInformation(name, win, afterDefeatScore, "9급");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference usergameRef = firebaseDatabase.getReference("userGameInformation");
        HashMap<String, Object> gameMap = new HashMap<>();
        gameMap.put(md5StringNickName, gameInformation.toMap());
        usergameRef.updateChildren(gameMap);
    }// 항복한 자의 패배 수 +1 해주기.

    void updateWinScore(String name, String win, String defeat){
        int winScore = Integer.parseInt(win) + 1;
        String afterWinScore = "" + winScore;
        String afterRank = updateRank(winScore);
        String md5StringNickName = ChangeEmailMD5.encrypt(name);
        final UserGameInformation gameInformation = new UserGameInformation(name, afterWinScore, defeat, afterRank);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference usergameRef = firebaseDatabase.getReference("userGameInformation");
        HashMap<String, Object> gameMap = new HashMap<>();
        gameMap.put(md5StringNickName, gameInformation.toMap());
        usergameRef.updateChildren(gameMap);
    }// 승자의 승 수 +1 해주기.

    void geChattingService(String name, String otherName, String userturn){
        Intent it = new Intent(this, ChattingService.class);
        it.putExtra("myNickName", name);
        it.putExtra("otherNickName", otherName);
        it.putExtra("turn", userturn);
        startActivity(it);
    }// 채팅방으로 이동.

    String updateRank(int rank){
        String updateRank = "급";
        if(rank > 500) {
            updateRank = "지존";
        } else if(rank > 400){
            updateRank = "6단";
        } else if(rank >300){
            updateRank = "5단";
        } else if(rank >200){
            updateRank = "4단";
        } else if(rank >150){
            updateRank = "3단";
        } else if(rank >100){
            updateRank = "2단";
        } else if(rank >90){
            updateRank = "1단";
        } else if(rank >80){
            updateRank = "1급";
        } else if(rank >70){
            updateRank = "2급";
        } else if(rank >60){
            updateRank = "3급";
        } else if(rank >50){
            updateRank = "4급";
        } else if(rank >40){
            updateRank = "5급";
        } else if(rank >30){
            updateRank = "6급";
        }  else if(rank >20){
            updateRank = "7급";
        } else if(rank >10){
            updateRank = "8급";
        } else {
            updateRank = "9급";
        }
        return updateRank;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////장기판

    void setKoreaChessButton(final String selectTeam, final Button[][] koreaChessButton) {
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                final String number_x = Integer.toString(j);
                final String number_y = Integer.toString(i);
                koreaChessButton[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selectTeam.equals("han")){
                            int x = Integer.parseInt(number_x);
                            int y = Integer.parseInt(number_y);
                            if(!move.contains(selectTeam)) {
                                if(!koreaChessButton[y][x].getText().toString().contains("han")) {
                                } else {
                                    clickButton(koreaChessButton[y][x].getText().toString(), y, x, selectTeam);
                                    if(move.equals("")) {
                                        move = move + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                    } else {
                                        move = move + "/" + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                        if(move.length() > 23) {
                                            resetButton(move, koreaChessButton);
                                            move = "";
                                        }
                                    }
                                }
                            } else {
                                clickButton(koreaChessButton[y][x].getText().toString(), y, x, selectTeam);
                                System.out.println(number_x + "" + number_y+ "" + koreaChessButton[y][x].getText());
                                if(move.equals("")) {
                                    move = move + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                } else {
                                    move = move + "/" + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                    if(move.length() > 23) {
                                        resetButton(move, koreaChessButton);
                                        move = "";
                                    }
                                }
                            }
                        } else {
                            int x = Integer.parseInt(number_x);
                            int y = Integer.parseInt(number_y);
                            if(!move.contains(selectTeam)) {
                                if(!koreaChessButton[y][x].getText().toString().contains("cho")) {
                                } else {
                                    clickButton(koreaChessButton[y][x].getText().toString(), y, x, selectTeam);
                                    if(move.equals("")) {
                                        move = move + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                    } else {
                                        move = move + "/" + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                        if(move.length() > 23) {
                                            resetButton(move, koreaChessButton);
                                            move = "";
                                        }
                                    }
                                }
                            } else {
                                clickButton(koreaChessButton[y][x].getText().toString(), y, x, selectTeam);
                                if(move.equals("")) {
                                    move = move + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                } else {
                                    move = move + "/" + koreaChessButton[y][x].getText().toString() + "/" + x + "/" + y;
                                    if(move.length() > 23) {
                                        resetButton(move, koreaChessButton);
                                        move = "";
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }// 장기말 클릭 관리 매소드.

    void clickButton(String buttonName, int y, int x, String selectTeam){
        if(buttonName.contains("han")) {
            if(buttonName.contains("Joll")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_joll_cliecked);
            } else if(buttonName.contains("Pho")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_pho_cliecked);
            } else if(buttonName.contains("Cha")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_cha_cliecked);
            } else if(buttonName.contains("Sang")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_sang_cliecked);
            } else if(buttonName.contains("Ma")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_ma_cliecked);
            } else if(buttonName.contains("Sa")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_sa_cliecked);
            } else if(buttonName.contains("King")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_king_cliecked);
            } else {
                if(selectTeam.equals("han"))
                    koreaChessButton[y][x].setBackgroundResource(R.drawable.clicked_empty_red);
                else
                    koreaChessButton[y][x].setBackgroundResource(R.drawable.clicked_empty_green);
            }
        } else {
            if(buttonName.contains("Joll")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_joll_clicked);
            } else if(buttonName.contains("Pho")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_pho_clicked);
            } else if(buttonName.contains("Cha")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_cha_clicked);
            } else if(buttonName.contains("Sang")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_sang_clicked);
            } else if(buttonName.contains("Ma")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_ma_clicked);
            } else if(buttonName.contains("Sa")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_sa_clicked);
            } else if(buttonName.contains("King")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_king_clicked);
            } else {
                if(selectTeam.equals("cho"))
                    koreaChessButton[y][x].setBackgroundResource(R.drawable.clicked_empty_green);
                else
                    koreaChessButton[y][x].setBackgroundResource(R.drawable.clicked_empty_red);
            }
        }
    }// 버튼 클릭후 클릭 이미지로 변경 메소드

    void resetButton(String _move, final Button[][] koreaChessButton){
        StringTokenizer stk = new StringTokenizer(_move, "/");
        String beforeKoreaChess = stk.nextToken().toString().trim();
        int before_X = Integer.parseInt(stk.nextToken().toString().trim());//j
        int before_Y = Integer.parseInt(stk.nextToken().toString().trim());//i
        String afterKoreaChess = stk.nextToken().toString().trim();
        int after_X = Integer.parseInt(stk.nextToken().toString().trim());
        int after_Y = Integer.parseInt(stk.nextToken().toString().trim());
        String otherKoreaChess = stk.nextToken().toString().trim();
        int other_X = Integer.parseInt(stk.nextToken().toString().trim());
        int other_Y = Integer.parseInt(stk.nextToken().toString().trim());

        koreaChessButton[after_Y][after_X].setText(afterKoreaChess);
        koreaChessButton[before_Y][before_X].setText(beforeKoreaChess);
        koreaChessButton[other_Y][other_X].setText(otherKoreaChess);

        if(afterKoreaChess.equals("empty")){
            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.button_default_layout);
        } else {
            if(koreaChessButton[after_Y][after_X].getText().toString().contains("han")) {
                if(koreaChessButton[after_Y][after_X].getText().toString().contains("Joll")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_joll);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Pho")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_pho);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Cha")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_cha);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Sang")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_sang);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Ma")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_ma);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Sa")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_sa);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("King")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_king);
                }
            } else {
                if(koreaChessButton[after_Y][after_X].getText().toString().contains("Joll")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_joll);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Pho")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_pho);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Cha")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_cha);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Sang")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_sang);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Ma")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_ma);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("Sa")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_sa);
                } else if(koreaChessButton[after_Y][after_X].getText().toString().contains("King")){
                    koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_king);
                }
            }
        }
        if(beforeKoreaChess.equals("empty")){
            koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.button_default_layout);
        } else {
            if(koreaChessButton[before_Y][before_X].getText().toString().contains("han")) {
                if(koreaChessButton[before_Y][before_X].getText().toString().contains("Joll")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_joll);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Pho")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_pho);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Cha")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_cha);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Sang")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_sang);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Ma")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_ma);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Sa")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_sa);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("King")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.han_king);
                }
            } else {
                if(koreaChessButton[before_Y][before_X].getText().toString().contains("Joll")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_joll);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Pho")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_pho);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Cha")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_cha);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Sang")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_sang);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Ma")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_ma);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("Sa")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_sa);
                } else if(koreaChessButton[before_Y][before_X].getText().toString().contains("King")){
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.cho_king);
                }
            }
        }
        if(otherKoreaChess.equals("empty")){
            koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.button_default_layout);
        } else {
            if(koreaChessButton[other_Y][other_X].getText().toString().contains("han")) {
                if(koreaChessButton[other_Y][other_X].getText().toString().contains("Joll")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_joll);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Pho")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_pho);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Cha")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_cha);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Sang")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_sang);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Ma")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_ma);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Sa")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_sa);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("King")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.han_king);
                }
            } else {
                if(koreaChessButton[other_Y][other_X].getText().toString().contains("Joll")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_joll);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Pho")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_pho);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Cha")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_cha);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Sang")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_sang);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Ma")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_ma);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("Sa")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_sa);
                } else if(koreaChessButton[other_Y][other_X].getText().toString().contains("King")){
                    koreaChessButton[other_Y][other_X].setBackgroundResource(R.drawable.cho_king);
                }
            }
        }
    }// 버튼 세개 이상 누를경우 행동 취소.

    void setMyChess(String _move, final Button[][] koreaChessButton, String myName){
        StringTokenizer stk = new StringTokenizer(_move, "/");
        String beforeKoreaChess = stk.nextToken().toString().trim();
        int before_X = Integer.parseInt(stk.nextToken().toString().trim());//j
        int before_Y = Integer.parseInt(stk.nextToken().toString().trim());//i
        String afterKoreaChess = stk.nextToken().toString().trim();
        int after_X = Integer.parseInt(stk.nextToken().toString().trim());
        int after_Y = Integer.parseInt(stk.nextToken().toString().trim());

        if(!beforeKoreaChess.equals(afterKoreaChess)){
            if(checkKoreaChessFunction.checkFunction(beforeKoreaChess, before_X, before_Y, afterKoreaChess, after_X, after_Y, koreaChessButton)){//움직임 검사.
                if(!beforeKoreaChess.substring(0, 3).equals(afterKoreaChess.substring(0, 3))) {
                    if(afterKoreaChess.contains("King")){
                        turnCheck = false;
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameService.this);
                        builder.setTitle("알림!!!").setMessage(getNickName + "님의 승리 입니다!!!");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    koreaChessButton[before_Y][before_X].setText("empty");
                    koreaChessButton[before_Y][before_X].setBackgroundResource(R.drawable.button_default_layout);
                    koreaChessButton[after_Y][after_X].setText(beforeKoreaChess);
                    if(beforeKoreaChess.contains("han")) {
                        if(beforeKoreaChess.contains("Joll")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_joll);
                        } else if(beforeKoreaChess.contains("Pho")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_pho);
                        } else if(beforeKoreaChess.contains("Cha")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_cha);
                        } else if(beforeKoreaChess.contains("Sang")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_sang);
                        } else if(beforeKoreaChess.contains("Ma")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_ma);
                        } else if(beforeKoreaChess.contains("Sa")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_sa);
                        } else if(beforeKoreaChess.contains("King")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.han_king);
                        }
                        move = "";
                    } else {
                        if(beforeKoreaChess.contains("Joll")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_joll);
                        } else if(beforeKoreaChess.contains("Pho")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_pho);
                        } else if(beforeKoreaChess.contains("Cha")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_cha);
                        } else if(beforeKoreaChess.contains("Sang")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_sang);
                        } else if(beforeKoreaChess.contains("Ma")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_ma);
                        } else if(beforeKoreaChess.contains("Sa")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_sa);
                        } else if(beforeKoreaChess.contains("King")){
                            koreaChessButton[after_Y][after_X].setBackgroundResource(R.drawable.cho_king);
                        }
                        move = "";
                    }
                    turnCheck = false;
                    String x = ""+before_X;
                    String y = ""+before_Y;
                    String a = ""+after_X;
                    String b = ""+after_Y;
                    GameMove gameMove = new GameMove(beforeKoreaChess, x, y, a, b, myName);
                    String md5NickNameRoom = ChangeEmailMD5.encrypt(roomMaster + "GAMEROOM");
                    System.out.println("roomMaster" + roomMaster + "  MD5 : " + md5NickNameRoom);
                    String USER_GAME_PATH = "GameRooms/" + md5NickNameRoom;
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child(USER_GAME_PATH + "/" + "GAME").push();
                    databaseReference.setValue(gameMove);
                } else {
                    changeImageKoreaChess(beforeKoreaChess, before_X, before_Y);
                    changeImageKoreaChess(afterKoreaChess, after_X, after_Y);
                    move = "";
                }
            } else {
                changeImageKoreaChess(beforeKoreaChess, before_X, before_Y);
                changeImageKoreaChess(afterKoreaChess, after_X, after_Y);
                move = "";
            }
        } else {
            changeImageKoreaChess(beforeKoreaChess, before_X, before_Y);
            changeImageKoreaChess(afterKoreaChess, after_X, after_Y);
            move = "";
        }
    }// 차례 넘기기.

    void changeImageKoreaChess(String name, int x, int y){
        if (name.contains("han")){
            if(name.contains("Joll")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_joll);
            }else if(name.contains("Pho")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_pho);
            }else if(name.contains("Ma")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_ma);
            }else if(name.contains("Sang")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_sang);
            }else if(name.contains("Cha")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_cha);
            }else if(name.contains("Sa")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_sa);
            }else if(name.contains("King")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.han_king);
            }
        } else if(name.contains("cho")) {
            if(name.contains("Joll")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_joll);
            }else if(name.contains("Pho")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_pho);
            }else if(name.contains("Ma")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_ma);
            }else if(name.contains("Sang")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_sang);
            }else if(name.contains("Cha")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_cha);
            }else if(name.contains("Sa")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_sa);
            }else if(name.contains("King")){
                koreaChessButton[y][x].setBackgroundResource(R.drawable.cho_king);
            }
        } else {
            koreaChessButton[y][x].setBackgroundResource(R.drawable.button_default_layout);
        }
    }// 기본 이미지 변경 메소드.

}
