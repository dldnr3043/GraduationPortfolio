package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;
     boolean login_check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 회원가입 버튼
        Button sign_up = (Button)findViewById(R.id.button_sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼
        Button login = (Button)findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initDatabase();
                Login();
            }
        });

        // ID/PW 찾기 버튼
        TextView find = (TextView)findViewById(R.id.button_find);
        find.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("users");
        mReference.child("users");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

    private void Login() {
        EditText mail = (EditText) findViewById(R.id.login_mailText);
        EditText pw = (EditText) findViewById(R.id.login_passwordText);

        final String MAIL = mail.getText().toString();
        final String PW = pw.getText().toString();

        mReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance(); //이메일 비밀번호 로그인 모듈 변수
        currentUser = mAuth.getCurrentUser();
        final String user_mail = currentUser.getEmail();


        if(PW.length() == 0 || MAIL.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            AlertDialog dialog = builder.setMessage("빈 칸을 입력해주세요.")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();

            return ;
        }

        mAuth.signInWithEmailAndPassword(MAIL,PW).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"로그인에 실패하였습니다." ,Toast.LENGTH_SHORT).show(); return ;
                }else{
                    currentUser = mAuth.getCurrentUser();

                    Toast.makeText(LoginActivity.this, "로그인 성공" + "/" + currentUser.getEmail() + "/" + currentUser.getUid() , Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, LoginCheckActivity.class));
                    finish();
                }
            }
        });
    }
}
