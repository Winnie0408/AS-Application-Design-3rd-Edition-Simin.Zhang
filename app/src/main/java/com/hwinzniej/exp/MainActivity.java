package com.hwinzniej.exp;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button readBtn, saveBtn, exitBtn;
    EditText editText;
    String fileName = "notebook.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextMultiLine);
        readBtn = findViewById(R.id.readButton);
        saveBtn = findViewById(R.id.saveButton);
        exitBtn = findViewById(R.id.exitButton);

        readBtn.setOnClickListener(v -> {
            try {
                FileInputStream fileInputStream = openFileInput(fileName);
                byte[] bytes = new byte[1024];
                int len = fileInputStream.read(bytes);
                String text = new String(bytes, 0, len);
                editText.setText(text);
                Toast.makeText(this, "读取成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "出错了：" + e, Toast.LENGTH_SHORT).show();
            }
        });
        saveBtn.setOnClickListener(v -> {
            String text = editText.getText().toString();
            try {
                FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                fileOutputStream.write(text.getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "出错了：" + e, Toast.LENGTH_SHORT).show();
            }
        });
        exitBtn.setOnClickListener(v -> finish());
    }
}
