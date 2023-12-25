package com.hwinzniej.exp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    EditText account, password;
    HttpURLConnection connPost = null;
    BufferedReader br = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.registerButton);
        account = findViewById(R.id.editTextAccount);
        password = findViewById(R.id.editTextPassword);
        loginBtn.setOnClickListener(v -> Login());
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }
    public void Login() {
        new Thread(() -> {
            String accountString = account.getText().toString();
            String passwordString = password.getText().toString();
            try {
                String str = "http://192.168.91.43:8082/account/logIn";
                URL url = new URL(str);
                connPost = (HttpURLConnection) url.openConnection();
                connPost.setRequestMethod("POST");
                connPost.setConnectTimeout(5000);
                connPost.setDoOutput(true);
                connPost.setDoInput(true);

                PrintWriter pw = new PrintWriter(connPost.getOutputStream());
                Map<String, String> postData = new HashMap<>();
                postData.put("no", accountString);
                postData.put("password", passwordString);
                pw.write(JSONObject.toJSONString(postData));
                pw.flush();
                br = new BufferedReader(new InputStreamReader(connPost.getInputStream()));
                JSONObject json = JSONObject.parse(br.readLine());
                if (json.get("result").equals("1")) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    });
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("account", json.getString("no"));
                    bundle.putString("name", json.getString("name"));
                    bundle.putString("gender", json.getString("gender"));
                    bundle.putString("email", json.getString("email"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    runOnUiThread(() -> {
                        password.setText("");
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    });
                }
                br.close();
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "登录错误: " + e, Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        }).start();
    }

//    class mClick implements View.OnClickListener {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = null;
//        HttpURLConnection connGet = null;
//        HttpURLConnection connPost = null;
//
//        @Override
//        public void onClick(View v) {
//            log.setText("");
//            if (v == getBtn) {
//                String sid = id.getText().toString();
//                String sname = name.getText().toString();
//                String semail = email.getText().toString();
//                try {
//                    String str = "http://192.168.41.128/play-get.php" + "?sid=" + sid + "&name=" + sname + "&email=" + semail;
//                    URL url = new URL(str);
//                    connGet = (HttpURLConnection) url.openConnection();
//                    connGet.setRequestMethod("GET");
//                    connGet.setConnectTimeout(5000);
//                    if (connGet.getResponseCode() == 200) {
//                        br = new BufferedReader(new InputStreamReader(connGet.getInputStream()));
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            sb.append(line).append("\n");
//                        }
//                        log.setText(sb.toString());
//                        br.close();
//                    }
//
//                } catch (IOException e) {
//                    log.append("\nGET Error: " + e);
//                }
//            }
//            if (v == postBtn) {
//                String sid = id.getText().toString();
//                String sname = name.getText().toString();
//                String semail = email.getText().toString();
//                try {
//                    String str = "http://192.168.41.128/play-post.php";
//                    URL url = new URL(str);
//                    connPost = (HttpURLConnection) url.openConnection();
//                    connPost.setRequestMethod("POST");
//                    connPost.setConnectTimeout(5000);
//                    connPost.setDoOutput(true);
//                    connPost.setDoInput(true);
//
//                    PrintWriter pw = new PrintWriter(connPost.getOutputStream());
//                    String postData = "sid=" + URLEncoder.encode(sid, "UTF-8") +
//                            "&name=" + URLEncoder.encode(sname, "UTF-8") +
//                            "&email=" + URLEncoder.encode(semail, "UTF-8");
//                    pw.write(postData);
//                    pw.flush();
//                    br = new BufferedReader(new InputStreamReader(connPost.getInputStream()));
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line).append("\n");
//                    }
//                    log.setText(sb.toString());
//                    br.close();
//                } catch (IOException e) {
//                    log.append("\nPOST Error: " + e);
//                }
//            }
//        }
//    }
}
