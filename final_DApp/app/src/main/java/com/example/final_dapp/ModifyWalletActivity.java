package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
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
import java.util.Map;

public class ModifyWalletActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_wallet);

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_modify_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        // 확인 버튼
        Button confirm = (Button)findViewById(R.id.modify_wallet_button);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initDatabase();
                update();

                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
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

    private void update() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String user_mail = currentUser.getEmail();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            EditText address = (EditText)findViewById(R.id.modify_wallet_address);
            String ADDRESS = address.getText().toString();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getnum = userInfo.get("num").toString();
                    String getmail = userInfo.get("mail").toString();

                    if(getmail.equals(user_mail)) {
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put(getnum +"/address", ADDRESS);
                        mReference.updateChildren(taskMap); // firebass RealtimeDB에 데이터를 수정
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
