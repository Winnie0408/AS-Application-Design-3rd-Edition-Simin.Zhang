package com.hwinzniej.exp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    EditText account, password, email, name;
    Button registerBtn, backBtn;
    RadioGroup gender;
    HttpURLConnection connPost = null;
    BufferedReader br = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        account = findViewById(R.id.editTextAccount);
        password = findViewById(R.id.editTextPassword);
        email = findViewById(R.id.editTextEmail);
        name = findViewById(R.id.editTextName);
        registerBtn = findViewById(R.id.registerButton);
        backBtn = findViewById(R.id.backButton);
        gender = findViewById(R.id.gender);
        registerBtn.setOnClickListener(v -> Register());
        backBtn.setOnClickListener(v -> finish());
    }
    public void Register() {
        new Thread(() -> {
            String accountString = account.getText().toString();
            String passwordString = password.getText().toString();
            try {
                String str = "http://192.168.91.43:8082/account/register";
                URL url = new URL(str);
                connPost = (HttpURLConnection) url.openConnection();
                connPost.setRequestMethod("POST");
                connPost.setConnectTimeout(5000);
                connPost.setDoOutput(true);
                connPost.setDoInput(true);

                PrintWriter pw = new PrintWriter(connPost.getOutputStream());
                Map<String, String> postData = new HashMap<>();
                postData.put("stuEmpNo", accountString);
                postData.put("password", passwordString);
                postData.put("email", email.getText().toString());
                postData.put("name", name.getText().toString());
                if (gender.getCheckedRadioButtonId() == R.id.man) {
                    postData.put("gender", "男");
                } else if (gender.getCheckedRadioButtonId() == R.id.woman) {
                    postData.put("gender", "女");
                } else {
                    postData.put("gender", "");
                }
                pw.write(JSONObject.toJSONString(postData));
                pw.flush();
                br = new BufferedReader(new InputStreamReader(connPost.getInputStream()));
                JSONObject json = JSONObject.parse(br.readLine());
                if (json.get("result").equals("1")) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity2.this, "注册成功，1秒后返回登录界面", Toast.LENGTH_SHORT).show();
                    });
                    Thread.sleep(1000);
                    finish();
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity2.this, "注册失败", Toast.LENGTH_SHORT).show();
                    });
                }
                br.close();
            } catch (IOException | InterruptedException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity2.this, "注册错误: " + e, Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        }).start();
    }

}
