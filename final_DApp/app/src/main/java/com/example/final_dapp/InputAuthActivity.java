package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class InputAuthActivity extends AppCompatActivity {

    Web3j web3j = Web3j.build(new HttpService("https://goerli.infura.io/v3/2aad25e674bc45fca46b7e7e48f14f5e"));

    private String AdminMail = "dldnr3043@koreatech.ac.kr";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputauth);

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_inputauth_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 확인 버튼
        Button confirm = (Button)findViewById(R.id.button_inputauth);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initDatabase();
                auth();
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

    private void auth() {
        final EditText userauth = (EditText) findViewById(R.id.edittext_inputauth);
        final String USERAUTH = userauth.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String user_mail = currentUser.getEmail();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            String adminauth;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getmail = userInfo.get("mail").toString();
                    String getauth = userInfo.get("AuthNum").toString();

                    if(getmail.equals(AdminMail)) {
                        adminauth = getauth;
                        break;
                    }
                }

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getnum = userInfo.get("num").toString();
                    String getmail = userInfo.get("mail").toString();
                    String getaddress = userInfo.get("address").toString();
                    String getactive = userInfo.get("active").toString();

                    if(adminauth.equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InputAuthActivity.this);
                        AlertDialog dialog = builder.setMessage("토큰을 받을 수 없습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        break;
                    }

                    if(getmail.equals(user_mail)) {
                        if(getactive.equals("false") && USERAUTH.equals(adminauth)) {
                            try {
                                Map<String, Object> taskMap = new HashMap<String, Object>();
                                taskMap.put(getnum +"/active", "true");
                                mReference.updateChildren(taskMap); // firebass RealtimeDB에 데이터를 수정

                                Toast.makeText(InputAuthActivity.this, "확인 창이 나올 때까지 잠시만 기다려주세요 ..." , Toast.LENGTH_SHORT).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            ExcuteContract(getaddress);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 500);

                                break;
                            } catch (Exception e) {
                                e.printStackTrace();

                                AlertDialog.Builder builder = new AlertDialog.Builder(InputAuthActivity.this);
                                AlertDialog dialog = builder.setMessage("다시 시도해주세요.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        else if(getactive.equals("true")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InputAuthActivity.this);
                            AlertDialog dialog = builder.setMessage("이미 토큰을 받았습니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InputAuthActivity.this);
                            AlertDialog dialog = builder.setMessage("인증 번호가 다릅니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private Credentials getCredential(String privateKeyInHex) {
        BigInteger privateKeyInBT = new BigInteger(privateKeyInHex, 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);
        Credentials aCredential = Credentials.create(aPair);
        return aCredential;
    }

    public void ExcuteContract(String key) throws Exception {
        Credentials crd = getCredential("e1571274b2e3abb5540cec4412d39a3ebdf9578e93fc2e087007bbdf07dfee87");
        SimpleToken contract = SimpleToken.load("0x9d1b9302aa3d7f980c42098e7af9230a6b26950d", web3j, crd, BigInteger.valueOf(2000L), BigInteger.valueOf(1000000L));

        contract.transfer(key, BigInteger.valueOf(1000000000000000000L)).sendAsync().get();

        AlertDialog.Builder builder = new AlertDialog.Builder(InputAuthActivity.this);
        AlertDialog dialog = builder.setMessage("토큰을 지급받았습니다.")
                .setPositiveButton("확인", null)
                .create();
        dialog.show();
    }
}
