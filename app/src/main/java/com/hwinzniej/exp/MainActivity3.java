package com.hwinzniej.exp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity3 extends AppCompatActivity {
    Button exitBtn;
    TextView account, gender, email,name;
    HttpURLConnection connPost = null;
    BufferedReader br = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        exitBtn = findViewById(R.id.exitBtn);
        account = findViewById(R.id.account);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        Bundle bundle = getIntent().getExtras();
        account.setText(bundle.getString("account"));
        gender.setText(bundle.getString("gender"));
        email.setText(bundle.getString("email"));
        name.setText(bundle.getString("name"));
        exitBtn.setOnClickListener(v -> Logout());
    }

    public void Logout() {
        new Thread(() -> {
            try {
                String str = "http://192.168.91.43:8082/account/logOut";
                URL url = new URL(str);
                connPost = (HttpURLConnection) url.openConnection();
                connPost.setRequestMethod("POST");
                connPost.setConnectTimeout(5000);
                connPost.setDoOutput(true);
                connPost.setDoInput(true);

                PrintWriter pw = new PrintWriter(connPost.getOutputStream());
                pw.flush();
                br = new BufferedReader(new InputStreamReader(connPost.getInputStream()));

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity3.this, "注销成功", Toast.LENGTH_SHORT).show();
                });
                br.close();
                finish();
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity3.this, "注销错误: " + e, Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        }).start();
    }

}