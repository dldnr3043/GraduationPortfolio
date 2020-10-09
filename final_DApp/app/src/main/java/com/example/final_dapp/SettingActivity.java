package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class SettingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        String value = intent.getStringExtra("value");

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_setting_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(value.equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        // 지갑 주소 수정 버튼
        TextView modify = (TextView)findViewById(R.id.setting_modify);
        modify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModifyWalletActivity.class);
                startActivity(intent);
            }
        });


        initDatabase();
        setTextView();
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

    // 사용자 정보 set 함수 기능
    private void setTextView() {
        final TextView num = (TextView)findViewById(R.id.setting_numtext);
        final TextView name = (TextView)findViewById(R.id.setting_nametext);
        final TextView address = (TextView)findViewById(R.id.setting_addresstext);
        final TextView mail = (TextView)findViewById(R.id.setting_mailtext);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final String user_mail = currentUser.getEmail();


        mReference = mDatabase.getReference("users"); // 변경값을 확인할 child 이름
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getnum = userInfo.get("num").toString();
                    String getname = userInfo.get("name").toString();
                    String getaddress = userInfo.get("address").toString();
                    String getmail = userInfo.get("mail").toString();


                    if(user_mail.equals(getmail)) {
                        num.setText("  " + getnum);
                        name.setText("  " + getname);
                        address.setText("" + getaddress);
                        mail.setText("" + getmail);

                        break;
                    }
                    else {
                        continue;
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
