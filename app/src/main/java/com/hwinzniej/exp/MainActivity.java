package com.hwinzniej.exp;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.function.impl.ToShort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button readBtn, saveBtn;
    EditText editText;
    String fileName = "test.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextText);
        readBtn = findViewById(R.id.readButton);
        saveBtn = findViewById(R.id.saveButton);
        readBtn.setOnClickListener(v -> {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File path = Environment.getExternalStorageDirectory();
                File sdFile = new File(path, "Download/" + fileName);
                try {
                    FileInputStream fileInputStream = new FileInputStream(sdFile);
                    byte[] bytes = new byte[1024];
                    int len = fileInputStream.read(bytes);
                    String text = new String(bytes, 0, len);
                    editText.setText(text);
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        saveBtn.setOnClickListener(v -> {
            String text = editText.getText().toString();
            String type = Environment.DIRECTORY_DOWNLOADS;
            File fileDir = Environment.getExternalStoragePublicDirectory(type);
            File sdFile = new File(fileDir + "/" + fileName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(sdFile);
                fileOutputStream.write(text.getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "文件保存到" + sdFile, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
