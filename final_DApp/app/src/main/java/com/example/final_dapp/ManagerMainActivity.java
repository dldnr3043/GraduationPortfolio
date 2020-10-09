package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class ManagerMainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managermain);


     /*   try {
            TokenTransfer();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

   /*     try {
            TransactionHistoryStore();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        Button notice = (Button) findViewById(R.id.button_m_notice);
        Button account = (Button) findViewById(R.id.button_m_account);
        Button store = (Button) findViewById(R.id.button_m_store);
        Button help = (Button) findViewById(R.id.button_m_help);
        Button setting = (Button) findViewById(R.id.button_m_setting);
        Button connect = (Button) findViewById(R.id.button_connect_bluetooth);
        Button f5 = (Button) findViewById(R.id.button_f5);

        notice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerNoticeActivity.class);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, testActivity.class);
                intent.putExtra("value", "admin");
                startActivity(intent);
            }
        });

        store.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, StoreActivity.class);
                intent.putExtra("value", "admin");
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, HelpActivity.class);
                intent.putExtra("value", "admin");
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, SettingActivity.class);
                intent.putExtra("value", "admin");
                startActivity(intent);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), peripheralActivity.class);
                startActivity(intent);
            }
        });

        f5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initDatabase();
                refresh();
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

    public void refresh() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String user_mail = currentUser.getEmail();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getnum = userInfo.get("num").toString();
                    String getmail = userInfo.get("mail").toString();

                    if (getmail.equals(user_mail)) {
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put(getnum + "/AuthNum", "0");
                        mReference.updateChildren(taskMap); // firebass RealtimeDB에 데이터를 수정
                    }

                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put(getnum + "/active", "false");
                    mReference.updateChildren(taskMap); // firebass RealtimeDB에 데이터를 수정
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });
        Toast.makeText(ManagerMainActivity.this, "초기화 완료 ...", Toast.LENGTH_SHORT).show();
    }
}

