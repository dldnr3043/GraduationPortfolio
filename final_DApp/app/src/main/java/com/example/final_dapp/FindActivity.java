package com.example.final_dapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FindActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        // 뒤로가기 버튼
        Button back = (Button) findViewById(R.id.button_find_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 아디/비번 찾기 버튼
        Button confirm = (Button)findViewById(R.id.find_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initDatabase();
                find();
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

    private void find() {
        EditText mail = (EditText) findViewById(R.id.find_mail);
        EditText num = (EditText) findViewById(R.id.find_num);

        final String MAIL = mail.getText().toString();
        final String NUM = num.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mReference = mDatabase.getReference("users"); // 변경값을 확인할 child 이름
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getMAIL = userInfo.get("mail").toString();
                    String getNUM = userInfo.get("num").toString();

                    if(MAIL.length() == 0 || NUM.length() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
                        AlertDialog dialog = builder.setMessage("빈 칸을 채워주십시오.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        break;
                    }

                    if(getMAIL.equals(MAIL) && getNUM.equals(NUM)) {
                        mAuth.sendPasswordResetEmail(getMAIL);

                        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
                        AlertDialog dialog = builder.setMessage("재전송 메일을 보냈습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        break;
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
                        AlertDialog dialog = builder.setMessage("정보를 다시 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });
    }
}