package com.hwinzniej.exp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText txt1;
    Button jsonBtn;
    String jdata = "["
            + "{\"sid\":1001, \"name\":\"张大山\"},"
            + "{\"sid\":1002, \"name\":\"李小丽\"}"
            + "]";

    JSONObject jid, jname;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        jsonBtn = findViewById(R.id.button);
        jsonBtn.setOnClickListener(v -> {
            JSONArray json = JSONArray.parse(jdata);
            String[] data = {
                    "sid: " + ((JSONObject) json.get(0)).get("sid"),
                    "name: " + ((JSONObject) json.get(0)).get("name"),
                    "sid: " + ((JSONObject) json.get(1)).get("sid"),
                    "name: " + ((JSONObject) json.get(1)).get("name")
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);
        });
    }
}
