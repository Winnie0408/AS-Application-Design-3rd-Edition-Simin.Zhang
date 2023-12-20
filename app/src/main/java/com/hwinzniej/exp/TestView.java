package com.hwinzniej.exp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {
    int x = 100;
    int y = 120;

    public TestView(Context context, AttributeSet attrs) {
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
        canvas.drawRect(300,300, 500,500, paint);

        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);   //设置画实心图形
        paint.setAntiAlias(true);       //去锯齿
        paint.setColor(Color.BLUE);
        canvas.drawCircle(x, y, 30, paint);   //画圆
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x - 9, y - 9, 6, paint);   //画圆
    }
}
