package com.example.koreachessok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText emailTextbox, passwordTextbox;
    Button btEnter, btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        emailTextbox = (EditText) findViewById(R.id.inputEmail);
        passwordTextbox = (EditText) findViewById(R.id.inputPassword);
        btEnter = (Button) findViewById(R.id.enter);
        btRegister = (Button) findViewById(R.id.register);
        btEnter.setBackgroundResource(R.drawable.button_default_layout);
        btRegister.setBackgroundResource(R.drawable.button_default_layout);
        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailTextbox.getText().toString().length() < 6) {
                    Toast.makeText(MainActivity.this, "잘못 입력하셨습니다...", Toast.LENGTH_SHORT).show();
                } else {
                    runLogin();
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runRegister();
            }
        });
    }

    private  void runLogin(){
        final String email = emailTextbox.getText().toString();
        final String password = passwordTextbox.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            System.out.println("성공");
                            goMain(user.getEmail());
                        }
                    }
                });
    }

    public void runRegister(){
        startActivity(new Intent(this, Register.class));
    }

    public void goMain (String email) {
        Intent it = new Intent(this, MainService.class);
        it.putExtra("it_email", email);
        startActivity(it);
        finish();
    }
}