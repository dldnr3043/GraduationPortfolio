package com.example.final_dapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class testActivity extends AppCompatActivity {

    String str, receiveMsg;
    private DatabaseReference mReference;
    int randomint = randomRange(10000, 99999);

    Web3j web3j = Web3j.build(new HttpService("https://goerli.infura.io/v3/2aad25e674bc45fca46b7e7e48f14f5e"));

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_account);


        Intent intent = getIntent();
        String value = intent.getStringExtra("value");


        // 뒤로가기 버튼
        Button back = (Button)findViewById(R.id.button_account_back);
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

        readMessage(receiveJSON());
    }

    public String receiveJSON() {
        URL url = null;
        try {
            url = new URL("https://testapi.openbanking.or.kr/v2.0/account/transaction_list/fin_num?bank_tran_id=T991657430U0282"+Integer.toString(randomint)+"&fintech_use_num=199165743057886706423123&inquiry_type=A&inquiry_base=D&from_date=20160404&from_time=100000&to_date=20160405&to_time=110000&sort_order=D&tran_dtime=20190910101921&befor_inquiry_trace_info=");
            HttpURLConnection myConnection = (HttpURLConnection)url.openConnection();
            myConnection.setRequestMethod("GET");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTAwNzYwNzkyIiwic2NvcGUiOlsiaW5xdWlyeSIsInRyYW5zZmVyIl0sImlzcyI6Imh0dHBzOi8vd3d3Lm9wZW5iYW5raW5nLm9yLmtyIiwiZXhwIjoxNjA5MDY3MjI2LCJqdGkiOiIzNjkzOTBhOC0wZjViLTRjOTAtODA1ZS0xMzAzMTZhMTYwYTAifQ.gwTWvKEWu3Wum7cmSASyS7uEQmnm7LYS6GrB-7Amz6g");

            if(myConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(responseBodyReader);
                StringBuffer stringBuffer = new StringBuffer();
                while((str = bufferedReader.readLine()) != null){
                    stringBuffer.append(str);
                }
                receiveMsg = stringBuffer.toString();
                bufferedReader.close();

                return receiveMsg;
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(testActivity.this);
                AlertDialog dialog = builder.setMessage("f")
                        .setPositiveButton("확인", null)
                        .create();
                dialog.show();
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void readMessage(String jsonString) {

        mReference = FirebaseDatabase.getInstance().getReference("accounts"); // 변경값을 확인할 child 이름
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                ListView lv = findViewById(R.id.accountListView);
                ArrayList<accountInfo> data =  new ArrayList<>();

                String date = "";
                String cost = "";
                String place = "";
                String type = "";
                try {
                    JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("res_list");

                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        date = jsonObject.optString("tran_date");
                        date += "/";
                        date += jsonObject.optString("tran_time");
                        cost = jsonObject.optString("tran_amt");
                        place = jsonObject.optString("branch_name");
                        type = jsonObject.optString("inout_type");

                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date rdate = new Date(now);
                        //rdate.setHours(rdate.getHours()-9);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        // nowDate 변수에 값을 저장한다.
                        String formatDate = sdfNow.format(rdate.getTime());

                        accountInfo account = new accountInfo(Integer.toString(i+1), date, cost, place, type, formatDate);

                        if((int)count < i+1) {
                            mReference.child(Integer.toString(i+1)).setValue(account); // firebass RealtimeDB에 데이터를 저장

                            // 블록체인에 거래내역 데이터 올리기
                            try {
                                TransactionHistoryStore(Integer.toString(i+1), date, cost, place, type);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        data.add(new accountInfo(Integer.toString(i+1), date, cost, place, type, formatDate)); // listview에 나타낼 data를 계속해서 저장
                    }

                    accountAdapter adapter = new accountAdapter(data);
                    lv.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
