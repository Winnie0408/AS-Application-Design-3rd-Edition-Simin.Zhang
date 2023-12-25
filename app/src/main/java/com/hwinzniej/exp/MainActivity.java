package com.hwinzniej.exp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Button connBtn;
    TextView txt;
    HttpURLConnection conn;
    InputStream inStream;
    String str = "http://192.168.213.213:8080/AndroidTest.jpg";
    MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imageView2);
        txt = findViewById(R.id.textView2);
        connBtn = findViewById(R.id.button2);
        connBtn.setOnClickListener(new mClick());
    }

    class mClick implements View.OnClickListener {
        String str;

        @Override
        public void onClick(View v) {
            StrictMode.setThreadPolicy(
                    new StrictMode
                            .ThreadPolicy
                            .Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build()
            );
            StrictMode.setVmPolicy(
                    new StrictMode
                            .VmPolicy
                            .Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .penaltyDeath()
                            .build()
            );
            getPic();
        }
    }

    private void getPic() {
        try {
            URL url = new URL(str);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                inStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                handler.obtainMessage(0, bitmap).sendToTarget();
                int result = inStream.read();
                while (result != -1) {
                    txt.append("\n" + (char) result);
                    result = inStream.read();
                }
                inStream.close();
                txt.append("\n建立输入流成功");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            txt.append("\n图片下载完成");
            img.setImageBitmap((Bitmap) msg.obj);
        }
    }
}
