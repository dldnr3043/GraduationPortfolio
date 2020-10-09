package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.concurrent.ExecutionException;

public class ManagerAccountActivity extends AppCompatActivity {



    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_account);

        initDatabase();
        setListview();


        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_m_account_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                startActivity(intent);
            }
        });

        Button register = (Button)findViewById(R.id.button_m_account_register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerAccountActivity.this, WriteaccountActivity.class);
                intent.putExtra("value", Integer.toString(CountList()));
                startActivity(intent);
                /*Intent intent = new Intent(getApplicationContext(), WriteaccountActivity.class);
                startActivity(intent);*/
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

    public int CountList() {
        ListView list = (ListView)findViewById(R.id.listview_m_account);
        int size;
        size = list.getCount();

        return size+1;
    }

    public void setListview() {

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listview_m_account);

        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final accountAdapter adapter = new accountAdapter();

        mReference = mDatabase.getReference("accounts"); // 변경값을 확인할 child 이름
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> postinfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getNum = postinfo.get("num").toString();
                    String getDate = postinfo.get("date").toString();
                    String getCost = postinfo.get("cost").toString();
                    String getPlace = postinfo.get("place").toString();
                    String getType = postinfo.get("type").toString();


                    adapter.addItem(getNum, getDate, getCost, getPlace, getType);
                    listview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });
    }


}
