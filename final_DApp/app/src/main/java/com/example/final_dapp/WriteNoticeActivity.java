package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteNoticeActivity extends AppCompatActivity {

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writenotice);

        Intent intent = getIntent();
        String post_num = intent.getStringExtra("value");

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_writenotice_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerNoticeActivity.class);
                startActivity(intent);
            }
        });

        // 등록 버튼
        Button register = (Button)findViewById(R.id.button_writenotice_register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Register(post_num);
            }
        });
    }

    private void Register(String post_num) {
        EditText title = (EditText)findViewById(R.id.edit_title_writenotice);
        EditText content = (EditText)findViewById(R.id.edit_content_writenotice);

        String TITLE = title.getText().toString();
        String CONTENT = content.getText().toString();

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date.getTime());

        mReference = FirebaseDatabase.getInstance().getReference();
        PostInfo post = new PostInfo(post_num, TITLE, CONTENT, formatDate);

        mReference.child("post").child(post_num).setValue(post); // firebass RealtimeDB에 데이터를 저장

        title.setText("");
        content.setText("");

        Intent intent = new Intent(getApplicationContext(), ManagerNoticeActivity.class);
        startActivity(intent);
    }
}
