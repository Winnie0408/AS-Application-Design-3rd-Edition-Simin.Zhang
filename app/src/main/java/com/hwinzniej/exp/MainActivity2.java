package com.hwinzniej.exp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            //Intent int1 = new Intent(MainActivity.this,MainActivity2.class);
            Intent int2 = new Intent(MainActivity2.this, MainActivity.class);
            if (x1 - x2 > 50) {//向左滑
                // startActivity(int1);
            } else if (x2 - x1 > 50) {//向右滑
                startActivity(int2);
            }
        }
        return super.onTouchEvent(event);
    }
}
