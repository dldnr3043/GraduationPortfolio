package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginCheckActivity extends AppCompatActivity {
    Handler handler = new Handler();
    String manager_MAIL = "dldnr3043@koreatech.ac.kr";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    Runnable r = new Runnable() {
        @Override
        public void run() {
// 4초뒤에 다음화면(MainActivity)으로 넘어가기 Handler 사용
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            final String user_mail = currentUser.getEmail();

            if(user_mail.equals(manager_MAIL)) {
                Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                startActivity(intent); // 다음화면으로 넘어가기
                finish(); // Activity 화면 제거
            }
            else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent); // 다음화면으로 넘어가기
                finish(); // Activity 화면 제거
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logincheck); // xml과 java소스를 연결
    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
// 다시 화면에 들어어왔을 때 예약 걸어주기
        handler.postDelayed(r, 2000); // 4초 뒤에 Runnable 객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();
// 화면을 벗어나면, handler 에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r); // 예약 취소
    }
}
