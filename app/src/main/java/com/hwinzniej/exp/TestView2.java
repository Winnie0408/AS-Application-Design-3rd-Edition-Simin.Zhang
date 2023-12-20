package com.hwinzniej.exp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TestView2 extends View {
    int x = 200;
    int y = 120;

    public TestView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void getXY(int x, int y)   //设计一个传值函数
    {
        this.x = x;
        this.y = y;
    }

    /* 必须重写View的抽象方法 onDraw() */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.CYAN);  //设置组件的背景色
        Paint paint = new Paint();

        paint.setStrokeWidth(3);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);  //设置空心
        canvas.drawRect(30,30, 100,100, paint);
    }
}
