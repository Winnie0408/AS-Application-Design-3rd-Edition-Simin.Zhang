package com.hwinzniej.exp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    Button volleyBtn;
    ListView listData;
    DataInfo dataInfo = new DataInfo();
    Map<Integer, DataInfo> dataMap = new HashMap<>();
    Semaphore done = new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        volleyBtn = findViewById(R.id.button);
        txt = findViewById(R.id.textView);
        listData = findViewById(R.id.listView);
        volleyBtn.setOnClickListener(v -> {
            getServerData();
            try {
                done.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < dataMap.size(); i++) {
                dataInfo = dataMap.get(i);
                list.add("序号：" + dataInfo.getSid());
                list.add("姓名：" + dataInfo.getName());
                list.add("邮箱：" + dataInfo.getEmail());
                list.add("");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1, list.toArray(new String[0]));
            listData.setAdapter(adapter);
        });
    }
    public void getServerData() {
        String jsonURL = "http://192.168.41.128/conn_testdb.php";
        try {
            RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest = new StringRequest(jsonURL, response -> {
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DataInfo dataInfo = new DataInfo();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    String sid = (String) jsonData.get("sid");
                                    String sname = (String) jsonData.get("name");
                                    String semail = (String) jsonData.get("email");
                                    dataInfo.setSid(sid);
                                    dataInfo.setName(sname);
                                    dataInfo.setEmail(semail);
                                    dataMap.put(i, dataInfo);
                                }
                            } catch (JSONException e) {
                                txt.setText("list错误");
                                Log.e("json错误", e.getMessage(), e);
                            }
                        }
                    },
                    error -> Log.e("TAG错误", error.getMessage(), error));
            mQueue.add(stringRequest);
            done.release();
        } catch (Exception ignored) {
        }
    }

    public static class DataInfo {
        private String name;
        private String sid;
        private String email;

        public void setName(String name) {
            this.name = name;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getSid() {
            return sid;
        }

        public String getEmail() {
            return email;
        }
    }

}
