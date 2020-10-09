package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class StoreActivity extends AppCompatActivity {

    Web3j web3j = Web3j.build(new HttpService("https://goerli.infura.io/v3/2aad25e674bc45fca46b7e7e48f14f5e"));

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent intent = getIntent();
        String value = intent.getStringExtra("value");

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_store_back);

        // 상품 버튼
        Button goods1 = (Button)findViewById(R.id.button_store_1);
        Button goods2 = (Button)findViewById(R.id.button_store_2);
        Button goods3 = (Button)findViewById(R.id.button_store_3);
        Button goods4 = (Button)findViewById(R.id.button_store_4);


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

        goods1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(StoreActivity.this, "확인 창이 나올 때까지 잠시만 기다려주세요 ..." , Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatabase();
                        sendToken(BigInteger.valueOf(3000000000000000000L));
                    }
                }, 500);
            }
        });
        goods2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(StoreActivity.this, "확인 창이 나올 때까지 잠시만 기다려주세요 ..." , Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatabase();
                        sendToken(BigInteger.valueOf(5000000000000000000L));
                    }
                }, 500);
            }
        });
        goods3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(StoreActivity.this, "확인 창이 나올 때까지 잠시만 기다려주세요 ..." , Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatabase();
                        sendToken(BigInteger.valueOf(5000000000000000000L));
                    }
                }, 500);
            }
        });
        goods4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(StoreActivity.this, "확인 창이 나올 때까지 잠시만 기다려주세요 ..." , Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatabase();
                        sendToken(BigInteger.valueOf(3000000000000000000L));
                    }
                }, 500);
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

    // 사용자 정보 set 함수 기능
    private void sendToken(BigInteger amounts) {
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
                    String getkey = userInfo.get("key").toString();
                    String getmail = userInfo.get("mail").toString();


                    if(user_mail.equals(getmail)) {
                        try {
                            ExcuteContract(getkey, amounts);

                            AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
                            AlertDialog dialog = builder.setMessage("감사합니다. 학생회로 수령하러 오시면 됩니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();

                            AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
                            AlertDialog dialog = builder.setMessage("구매에 실패했습니다. 잔여 토큰을 확인해주세요.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                        }


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

    private Credentials getCredential(String privateKeyInHex) {
        BigInteger privateKeyInBT = new BigInteger(privateKeyInHex, 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);
        Credentials aCredential = Credentials.create(aPair);
        return aCredential;
    }

    public void ExcuteContract(String key, BigInteger amounts) throws Exception {
        Credentials crd = getCredential(key);
        SimpleToken contract = SimpleToken.load("0x9d1b9302aa3d7f980c42098e7af9230a6b26950d", web3j, crd, BigInteger.valueOf(2000L), BigInteger.valueOf(1000000L));

        contract.transfer("0x74485ef859FA344E4D25f7889eA911109BD021F6", amounts).sendAsync().get();

        AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
        AlertDialog dialog = builder.setMessage("감사합니다. 학생회로 수령하러 오시면 됩니다.")
                .setPositiveButton("확인", null)
                .create();
        dialog.show();
    }
}
