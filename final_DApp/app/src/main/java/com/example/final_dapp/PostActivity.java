package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_post_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String type = intent.getStringExtra("type");

                if(type.equals("manager")) {
                    intent = new Intent(getApplicationContext(), ManagerNoticeActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getApplicationContext(), NoticeActivity.class);
                    startActivity(intent);
                }
            }
        });

        show();
    }

    public void show() {
        TextView title = (TextView)findViewById(R.id.textview_post_title);
        TextView content = (TextView)findViewById(R.id.textview_post_content);

        Intent intent = getIntent();
        String TITLE = intent.getStringExtra("title");
        String CONTENT = intent.getStringExtra("content");
        String type = intent.getStringExtra("type");

        title.setText(TITLE);
        content.setText(CONTENT);
        content.setMovementMethod(new ScrollingMovementMethod());
    }
}
