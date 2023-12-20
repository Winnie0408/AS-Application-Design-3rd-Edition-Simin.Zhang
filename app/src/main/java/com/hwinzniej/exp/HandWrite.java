package com.hwinzniej.exp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HandWrite extends View {
    Paint paint = null;
    Bitmap originalBitmap = null;
    Bitmap new1_Bitmap = null;
    Bitmap new2_Bitmap = null;
    float clickX = 0, clickY = 0;
    float startX = 0, startY = 0;
    boolean isMove = true;
    boolean isClear = false;
    int color = Color.BLACK;
    float strokeWidth = 5.0f;

    public HandWrite(Context context, AttributeSet attrs) {
        super(context, attrs);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white); //从资源中获取原始图像
        originalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        new1_Bitmap = Bitmap.createBitmap(originalBitmap);  //建立原始图像的位图
    }

    public void clear()  //清屏
    {
        isClear = true;
        new2_Bitmap = Bitmap.createBitmap(originalBitmap);  //恢复原图
        invalidate();
    }

    public void setstyle(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setcolor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas)  //显示绘图
    {
        super.onDraw(canvas);
        canvas.drawBitmap(HandWriting(new1_Bitmap), 0, 0, null);
    }

    public Bitmap HandWriting(Bitmap o_Bitmap)  //记录新绘的图像
    {
        Canvas canvas = null;  //定义画布
        if (isClear) {
            canvas = new Canvas(new2_Bitmap);  //创建绘制新图形的画布
        } else {
            canvas = new Canvas(o_Bitmap);  //创建绘制原图形的画布
        }
        paint = new Paint();                  //定义画笔
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        if (isMove) {
            canvas.drawLine(startX, startY, clickX, clickY, paint);  //在画布上画线条
        }
        startX = clickX;
        startY = clickY;

        if (isClear) {
            return new2_Bitmap;  //返回新绘制的图像
        }
        return o_Bitmap;  // 若清屏，则返回原图像
    }

    //触摸屏事件的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) //按下屏幕时无绘图
        {
            isMove = false;
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE)  //记录在屏幕上划动的轨迹
        {
            isMove = true;
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

}
