package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class WriteaccountActivity extends AppCompatActivity {

    Web3j web3j = Web3j.build(new HttpService("https://goerli.infura.io/v3/2aad25e674bc45fca46b7e7e48f14f5e"));

    private DatabaseReference mReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writeaccount);

        Intent intent = getIntent();
        String account_num = intent.getStringExtra("value");

        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_account_write_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerAccountActivity.class);
                startActivity(intent);
            }
        });

        // 등록 버튼
        Button register = (Button)findViewById(R.id.button_account_write);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(WriteaccountActivity.this, "잠시만 기다려주세요 ..." , Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Register(account_num);
                    }
                }, 500);


            }
        });
    }


    private void Register(String num) {
        EditText date = (EditText)findViewById(R.id.edittext_account_date);
        EditText cost = (EditText)findViewById(R.id.edittext_account_cost);
        EditText place = (EditText)findViewById(R.id.edittext_account_place);
        EditText type = (EditText)findViewById(R.id.edittext_account_type);

        String DATE = date.getText().toString();
        String COST = cost.getText().toString();
        String PLACE = place.getText().toString();
        String TYPE = type.getText().toString();

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date rdate = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(rdate.getTime());

        mReference = FirebaseDatabase.getInstance().getReference();
        accountInfo account = new accountInfo(num, DATE, COST, PLACE, TYPE, formatDate);

        mReference.child("accounts").child(num).setValue(account); // firebass RealtimeDB에 데이터를 저장

        try {
            TransactionHistoryStore(num, DATE, COST, PLACE, TYPE);
        } catch (ExecutionException e) {
            e.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(WriteaccountActivity.this);
            AlertDialog dialog = builder.setMessage("트랜젝션 등록에 실패했습니다.")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
        } catch (InterruptedException e) {
            e.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(WriteaccountActivity.this);
            AlertDialog dialog = builder.setMessage("트랜젝션 등록에 실패했습니다.")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
        }

        Intent intent = new Intent(getApplicationContext(), ManagerAccountActivity.class);
        startActivity(intent);
    }

    private Credentials getCredential(String privateKeyInHex) {
        BigInteger privateKeyInBT = new BigInteger(privateKeyInHex, 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);
        Credentials aCredential = Credentials.create(aPair);
        return aCredential;
    }

    public void TransactionHistoryStore(String num, String date, String cost, String place, String type) throws ExecutionException, InterruptedException {
        Credentials crd = getCredential("e1571274b2e3abb5540cec4412d39a3ebdf9578e93fc2e087007bbdf07dfee87");
        Trade contract = Trade.load("0xc906358036753fd3f4be64034bea79bcd99b1d00", web3j, crd, BigInteger.valueOf(2000L), BigInteger.valueOf(1000000L));

        contract.setValue(num, date, cost, place, type).sendAsync().get();
    }
}
