package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;
    private boolean idcheck_state;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 뒤로가기 버튼
        Button back = (Button) findViewById(R.id.signup_button_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // register(회원 등록) 버튼
        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewUser();
            }
        });


        // 아이디 중복검사 버튼
        Button check = (Button) findViewById(R.id.button_check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDatabase();
                IDcheck();
            }
        });
    }


    // firebase DB에 새로운 유저 정보 삽입 기능
    private void writeNewUser() {

        final EditText num = (EditText) findViewById(R.id.studentIDText);
        final EditText name = (EditText) findViewById(R.id.nameText);
        final EditText address = (EditText) findViewById(R.id.addressText);
        final EditText key = (EditText) findViewById(R.id.edittext_signup_privatekey);
        final EditText mail = (EditText) findViewById(R.id.mailText);
        final EditText pw = (EditText) findViewById(R.id.passwordText);

        final String NUM = num.getText().toString();
        final String NAME = name.getText().toString();
        final String ADDRESS = address.getText().toString();
        final String KEY = key.getText().toString();
        final String MAIL = mail.getText().toString();
        final String PW = pw.getText().toString();

        mReference = FirebaseDatabase.getInstance().getReference();
        final UserInfo user = new UserInfo(NUM, NAME, KEY, ADDRESS, MAIL, "false", "0");
        mAuth = FirebaseAuth.getInstance();




        if(NUM.length() == 0 || NAME.length() == 0 ||ADDRESS.length() == 0 ||MAIL.length() == 0 ||PW.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            AlertDialog dialog = builder.setMessage("빈 칸을 채워주십시오.")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();

            return;
        }
        else if(idcheck_state == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            AlertDialog dialog = builder.setMessage("아이디 중복체크를 하시오.")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();

            return;
        }
           /* else if(NUM.length() != 10) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                AlertDialog dialog = builder.setMessage("StudentID는 10자리여야 합니다.")
                        .setPositiveButton("확인", null)
                        .create();
                dialog.show();

                return;
            }
            else if(ADDRESS.length() != 42) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                AlertDialog dialog = builder.setMessage("Address는 본인 메타마스크의 지갑 주소인 42자리여야 합니다.")
                        .setPositiveButton("확인", null)
                        .create();
                dialog.show();

                return;
            }*/

        mAuth.createUserWithEmailAndPassword(MAIL, PW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                AlertDialog dialog = builder.setMessage("비밀번호가 간단합니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                return ;
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                AlertDialog dialog = builder.setMessage("email 형식이 맞지 않습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                return ;
                            } catch(FirebaseAuthUserCollisionException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                AlertDialog dialog = builder.setMessage("이미 존재하는 이메일입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                return ;
                            } catch(Exception e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                AlertDialog dialog = builder.setMessage("다시 확인해주세요.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                return ;
                            }
                        }else{

                            currentUser = mAuth.getCurrentUser();

                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            AlertDialog dialog = builder.setMessage("가입이 완료되었습니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();

                            mReference.child("users").child(NUM).setValue(user); // firebass RealtimeDB에 데이터를 저장

                            num.setText("");
                            name.setText("");
                            address.setText("");
                            mail.setText("");
                            pw.setText("");

                            return ;
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }


    // 아이디 중복검사 기능
    private void IDcheck() {

        mReference = mDatabase.getReference("users"); // 변경값을 확인할 child 이름
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            EditText email = (EditText) findViewById(R.id.mailText);
            String EMAIL = email.getText().toString();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) postSnapshot.getValue();
                    String getData = userInfo.get("mail").toString();

                    if(getData.equals(EMAIL)) {
                        idcheck_state = false;

                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        AlertDialog dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        return ;
                    }
                }

                if(EMAIL.length() != 0 && EMAIL.length() > 9) {
                    idcheck_state = true;

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    AlertDialog dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();

                    return ;
                }
                else if(EMAIL.length() == 0){
                    idcheck_state = false;

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    AlertDialog dialog = builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();

                    return ;
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    AlertDialog dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();

                    return ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });
    }
}