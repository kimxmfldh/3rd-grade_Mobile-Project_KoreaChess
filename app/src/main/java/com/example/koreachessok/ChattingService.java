package com.example.koreachessok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ChattingService extends AppCompatActivity {
    Intent it;
    EditText inputMassage;
    Button sendButton, backButton;
    ListView listView;
    MassageAdapter massageAdapter;
    ArrayList<Massage> massages = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_frame);

        it = getIntent();
        final String userNickname = it.getStringExtra("myNickName");
        final String otherNickname = it.getStringExtra("otherNickName");
        final String turn = it.getStringExtra("turn");
        final String roomMaster;

        if(turn.equals("one")) {
            roomMaster = userNickname;
        } else {
            roomMaster = otherNickname;
        }

        inputMassage = findViewById(R.id.inputMassage);// 내가 적은 메시지
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView_chatArea);
        massageAdapter = new MassageAdapter(massages, getLayoutInflater(), userNickname);
        listView.setAdapter(massageAdapter);

        String md5NickNameRoom = ChangeEmailMD5.encrypt(roomMaster + "GAMEROOM");
        String USER_GAME_PATH = "GameRooms/" + md5NickNameRoom + "/CHATTING";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(USER_GAME_PATH);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMassageToDB(userNickname, roomMaster);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Massage newMassage = snapshot.getValue(Massage.class);
                massages.add(newMassage);
                massageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                finish();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void sendMassageToDB(String nickName, String roomMaster){
        String Massage = inputMassage.getText().toString();
        Calendar calendar = Calendar.getInstance();
        String data = calendar.get(Calendar.YEAR)+"."+(calendar.get(Calendar.MONTH) + 1)+"."+
                calendar.get(Calendar.DAY_OF_MONTH);
        String time = (calendar.get(Calendar.HOUR_OF_DAY)+9)+":"+calendar.get(Calendar.MINUTE)+":"+
                calendar.get(Calendar.SECOND);
        Massage massageObject = new Massage(nickName, Massage, data, time);

        String md5NickNameRoom = ChangeEmailMD5.encrypt(roomMaster + "GAMEROOM");
        String USER_GAME_PATH = "GameRooms/" + md5NickNameRoom;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(USER_GAME_PATH + "/" + "CHATTING").push();;
        databaseReference.setValue(massageObject);
        inputMassage.setText("");
    }
}
