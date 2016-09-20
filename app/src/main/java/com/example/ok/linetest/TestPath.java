package com.example.ok.linetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ok on 2016/9/18.
 * path 类中方法的使用
 */
public class TestPath extends View {
    private Paint mPaint;
    private Path mPath;
    public TestPath(Context context) {
        super(context);
        init();
    }

    public TestPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        mPath=new Path();
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画一个闭合的弧
//        mPath.moveTo(getMeasuredWidth()/2,getMeasuredHeight()/2);
//        mPath.lineTo(getMeasuredWidth()/2+200,getMeasuredHeight()/2);
//        mPath.arcTo(new RectF(0,0,getMeasuredWidth(),getMeasuredHeight()),0,90,false);
//        mPath.close();
        // 画一个逆时针的圆角矩形
//        mPath.moveTo(getMeasuredWidth()/2,getMeasuredHeight()/2);
//        mPath.lineTo(getMeasuredWidth()/2+200,getMeasuredHeight()/2);
//        mPath.addRoundRect( new RectF(0,0,getMeasuredWidth(),getMeasuredHeight()),100,100, Path.Direction.CCW);

        canvas.drawPath(mPath,mPaint);
    }

}
