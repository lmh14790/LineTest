package com.example.ok.linetest;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Ok on 2016/9/19.
 * 360 小球
 */
public class Test_360Ball extends View {
    private float mRadius,mTextSize;
    private int  mBgColor,mWaveColor,mTextColor;
    private Path mPath;
    private Paint wavePaint,textPaint,mCirclePaint;
    private boolean isStart;
    /**
     * 是否初始化完成下面的值
     * mLeftSize......
     */
    private boolean isMeasured;
    /**
     * 距离最左边的距离,曲线高度和曲线宽度，当前据最左边距离。
     */
    private float mLeftSize,mWaveHight,mWaveWdith,cLeftSize;
    /**
     * 波浪曲线的所有点需要4（n+1）+1=4n+5个点
     * 其中n为小球宽度中可见的曲线周期，多出的一个周期为最左边的不可见部分，
     * 用于平移形成动态波浪
     *
     */
    private ArrayList<Point> mPointSet=new ArrayList<>();
    /**
     * 波浪的水平线 用于慢慢充满小球
     */
    private float  mWaveLine;
    private int mWidth,mHight;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x111:
                    startMove();
                    handler.sendEmptyMessageDelayed(0x111,10);
                    break;
            }
        }
    };
    public Test_360Ball(Context context) {
        super(context);
        init(context,null);
    }

    public Test_360Ball(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public Test_360Ball(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * 初始化各种内容
     * @param context
     * @param attrs
     */
    public void init(Context context,AttributeSet attrs){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.Test_360Ball);
       mRadius=typedArray.getDimension(R.styleable.Test_360Ball_radius,DisplayUtil.dip2px(context,50));
       // Log.e("tag","mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+mRadius);
       //mRadius=DisplayUtil. px2dip(context,mRadius);
       // Log.e("tag","sadddddddddddddddd"+mRadius);
        mTextSize=typedArray.getDimension(R.styleable.Test_360Ball_textSize,DisplayUtil.sp2px(context,14));
       // mTextSize=DisplayUtil.px2sp(context,mTextSize);
        mBgColor=typedArray.getColor(R.styleable.Test_360Ball_bgColor, Color.GREEN);
        mTextColor=typedArray.getColor(R.styleable.Test_360Ball_textColor,Color.WHITE);
        mWaveColor=typedArray.getColor(R.styleable.Test_360Ball_waveColor,Color.BLUE);
        typedArray.recycle();
        wavePaint=new Paint();
        wavePaint.setColor(mWaveColor);
        wavePaint.setStyle(Paint.Style.FILL);
        wavePaint.setAntiAlias(true);
        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint=new Paint();
        mCirclePaint.setColor(mBgColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mPath=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int  realRadius= Math.min(getH(heightMeasureSpec),getW(widthMeasureSpec));
        mRadius=realRadius;
        setMeasuredDimension(realRadius+getPaddingLeft()+getPaddingRight(),realRadius+getPaddingTop()+getPaddingBottom());
        if(!isMeasured){
            isMeasured=true;
            Log.e("tag","=========================="+getMeasuredHeight());
           mHight=getMeasuredHeight()-getPaddingTop()-getPaddingBottom();
           mWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
           mWaveHight=mWidth/10;
           mWaveWdith=mWidth;
            mWaveLine=mHight+getPaddingTop();
            mLeftSize=-mWaveWdith;
           int n= (int) Math.ceil(mWidth/mWaveWdith);

            for (int i = 0; i < (4*n)+5; i++) {
                int x= (int) (-mWaveWdith+(i*mWaveWdith/4f)+getPaddingLeft());
                int y= (int) mWaveLine;
                Point p=new Point();
                switch (i%4){
                    case 0:
                    case 2:
                        break;
                    case 1:
                        y+=mWaveHight;
                        break;
                    case 3:
                        y-=mWaveHight;
                        break;
                }
                p.x=x;
                p.y=y;
                mPointSet.add(p);
            }


        }
    }

    /**
     *
     * 测量组件的高
     * @param heightMeasureSpec
     * @return
     */
    private int getH(int heightMeasureSpec) {
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        switch (mode){
            case MeasureSpec.EXACTLY:
             break;
            default:
                size= (int) Math.min(mRadius,Math.abs(size));
                mRadius=size;
        }
        return size;
    }

    /**
     * 测量组件的宽
     * @param widthMeasureSpec
     * @return
     */
    private int  getW(int widthMeasureSpec) {
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        int size=MeasureSpec.getSize(widthMeasureSpec);
        switch (mode){
            case MeasureSpec.EXACTLY:
                break;
            default:
            size= (int) Math.min(mRadius,Math.abs(size));

            mRadius=size;

        }
        return size;

    }

    /**
     * 开始绘图
     */
    public void startMove(){
        mWaveLine+=-0.1f;
        cLeftSize+=5f;
        mLeftSize+=5f;
        if(mWaveLine<=getPaddingTop()){
            mWaveLine=getPaddingTop();
        }
        for (int i = 0; i < mPointSet.size(); i++) {
           Point p= mPointSet.get(i);
            p.x= (int) (p.x+5f);
            p.y= (int) mWaveLine;
            switch (i % 4)
            {
                case 0:
                case 2:
                    break;
                case 1:
                    p.y= (int) (p.y-mWaveHight);
                    //改变y轴坐标 奇数时

                    break;
                case 3:
                    //改变y轴坐标
                    p.y= (int) (p.y+mWaveHight);
                    break;
            }
        }
        if (cLeftSize>=mWaveWdith)
        {
            // 波形平移超过一个完整波形后复位

            resetPoints();
        }
       invalidate();
        }


    /**
     * 调整到一个周期之前的位置
     */
    private void resetPoints() {
        cLeftSize = 0;
        mLeftSize=-mWaveWdith;
        for (int i = 0; i < mPointSet.size(); i++) {
            Point p=mPointSet.get(i);
            p.x=(int) (-mWaveWdith+(i*mWaveWdith/4f)+getPaddingLeft());
        }
        
    }

    /**
     * 循环执行绘图
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(!isStart){
            handler.sendEmptyMessage(0x111);
            isStart=true;
        }

    }

    /**
     * 一个具有x,y坐标的对象
     */
    private static  class Point{
        public int  x;
        public int y;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    /**
     * 图像的裁剪 可以是的画布只显示裁剪的部分
     *  这个当时很重要 不好解决
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mPointSet.get(0).x,mPointSet.get(0).y);
        for (int i = 0; i < mPointSet.size()-2; i+=2) {
         mPath.quadTo(mPointSet.get(i+1).x,mPointSet.get(i+1).y,mPointSet.get(i+2).x,mPointSet.get(i+2).y);
        }
        mPath.lineTo(mPointSet.get(mPointSet.size()-1).x,mRadius+getPaddingTop());
        mPath.lineTo(mPointSet.get(0).x, mRadius+getPaddingTop());
        mPath.close();
        canvas.save();
        Path path=new Path();
        path.addOval(getPaddingLeft(),getPaddingTop(),getPaddingLeft()+mRadius,getPaddingTop()+mRadius,
                Path.Direction.CCW);
         canvas.clipPath(path);
         canvas.drawOval(getPaddingLeft(),getPaddingTop(),getPaddingLeft()+mRadius,getPaddingTop()+mRadius,mCirclePaint);
        canvas.drawPath(mPath, wavePaint);
         canvas.restore();
          int data= (int) (((mHight+getPaddingTop()-mWaveLine)/((float)mHight))*100);
         canvas.drawText("已完成"+data+"%",getPaddingStart()+mWidth/2,getPaddingTop()+mHight/2,textPaint);

    }


}
