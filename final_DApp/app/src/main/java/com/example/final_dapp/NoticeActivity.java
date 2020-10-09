package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


import java.util.HashMap;

public class NoticeActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        initDatabase();
        setListview();

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_notice_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    public void setListview() {

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.noticeListView);

        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final MyAdapter adapter = new MyAdapter();

        mReference = mDatabase.getReference("post"); // 변경값을 확인할 child 이름
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> postinfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getNum = postinfo.get("num").toString();
                    String getTitle = postinfo.get("title").toString();
                    String getContent = postinfo.get("content").toString();
                    String getDate = postinfo.get("date").toString();

                    adapter.addItem(getNum, getTitle, getContent, getDate);

                    listview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });

        EnterPost(listview, adapter);
    }

    public void EnterPost(ListView listview, MyAdapter adapter) {

        // 리스트뷰 각 리스트 클릭 이벤트 핸들러
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get item
                Intent intent = new Intent(NoticeActivity.this, PostActivity.class);
                intent.putExtra("title", adapter.getItem(position).gettitle());
                intent.putExtra("content", adapter.getItem(position).getcontent());
                intent.putExtra("type", "client");
                startActivity(intent);

                // TODO : use item data.
            }
        }) ;
    }
}