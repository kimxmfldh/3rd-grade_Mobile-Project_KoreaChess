package com.example.koreachessok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText inputEmail, inputPassword, inputName, inputNickName;
    Button btRegister, btCancel;

    protected  void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register_frame);

        firebaseAuth = FirebaseAuth.getInstance();
        inputEmail = (EditText)findViewById(R.id.registerEmail);
        inputPassword = (EditText)findViewById(R.id.registerPassoword);
        inputName = (EditText)findViewById(R.id.registerName);
        inputNickName = (EditText)findViewById(R.id.reigsterNickName);
        btRegister = (Button)findViewById(R.id.register);
        btCancel = (Button)findViewById(R.id.cancle);
        btRegister.setBackgroundResource(R.drawable.button_default_layout);
        btCancel.setBackgroundResource(R.drawable.button_default_layout);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runUploadAuth();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runCancel();
            }
        });
    }

    public void runUploadAuth(){// 입력된 email, password를 firebaseAuth에 저장하는 기능.
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String name = inputName.getText().toString();

        if(email == null || email.equals("")){
            Toast.makeText(this, "이메일 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password == null || password.equals("") || password.length() < 6){
            Toast.makeText(this, "6자리 이상의 암호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name == null || name.equals("")){
            Toast.makeText(this, "사용자의 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            insertUserInformationToDB();
                            String email = inputEmail.getText().toString().trim();
                            String md5StringEmail = ChangeEmailMD5.encrypt(email);
                            System.out.println("변환된 아이디 key 값 : " + md5StringEmail);
                            String nickname = inputNickName.getText().toString().trim();
                            String md5StringNick = ChangeEmailMD5.encrypt(nickname);
                            System.out.println("변환된 아이디 key 값 : " + md5StringNick);
                            Toast.makeText(getApplicationContext(), "가입 축하합니다!!!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(), "등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void runCancel(){
        Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void insertUserInformationToDB(){// MD5화 한 email과 mickName을 key값 노드로 각각의 정보를 database에 저장하는 기능.
        String email = inputEmail.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String nickName = inputNickName.getText().toString().trim();

        String md5StringEmail = ChangeEmailMD5.encrypt(email);
        String md5StringNickName = ChangeEmailMD5.encrypt(nickName);

        final UserInformation users = new UserInformation(email, password, name, nickName);
        final UserGameInformation gameInformation = new UserGameInformation(nickName, "1", "1", "9급");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference userinfoRef = firebaseDatabase.getReference("userLoginInformation");
        final DatabaseReference usergameRef = firebaseDatabase.getReference("userGameInformation");
        HashMap<String, Object> loginMap = new HashMap<>();
        HashMap<String, Object> gameMap = new HashMap<>();

        loginMap.put(md5StringEmail, users.toMap());
        gameMap.put(md5StringNickName, gameInformation.toMap());
        userinfoRef.updateChildren(loginMap);
        usergameRef.updateChildren(gameMap);
    }
}
