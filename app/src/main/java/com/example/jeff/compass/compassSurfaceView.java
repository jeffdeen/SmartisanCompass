package com.example.jeff.compass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jeffdeen on 2016/5/2.
 */
public class compassSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private SurfaceHolder mHolder; // 用于控制SurfaceView
    private Thread t; // 声明一条线程
    private volatile boolean flag; // 线程运行的标识，用于控制线程
    private Canvas mCanvas; // 声明一张画布
    //private Paint p; // 声明一支画笔
    float degree=0;
    public compassSurfaceView(Context context) {
        super(context);

        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        setFocusable(true); // 设置焦点
    }
    public compassSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        setFocusable(true); // 设置焦点
    }

    public compassSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        setFocusable(true); // 设置焦点
    }

    /**
     * 当SurfaceView创建的时候，调用此函数
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        t = new Thread(this); // 创建一个线程对象
        flag = true; // 把线程运行的标识设置成true
        t.start(); // 启动线程
    }

    /**
     * 当SurfaceView的视图发生改变的时候，调用此函数
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    /**
     * 当SurfaceView销毁的时候，调用此函数
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false; // 把线程运行的标识设置成false
        //mHolder.removeCallback(this);
    }

    /**
     * 当屏幕被触摸时调用
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
    @Override
    public void run() {
        while (flag) {
            try {
                synchronized (mHolder) {
                    Thread.sleep(10); // 让线程休息100毫秒
                    Draw(); // 调用自定义画画方法
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    // mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。

                }
            }
        }
    }
    protected void setDegree(float degree){
        this.degree=degree;
    }
    /**
     * 自定义一个方法，在画布上画一个圆
     */
    protected void Draw() {
        try{
            mHolder = getHolder();
            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            mCanvas.drawColor( 0, PorterDuff.Mode.CLEAR );//
            if (mCanvas != null) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.BLUE);
                paint.setStrokeWidth(10);
                paint.setStyle(Paint.Style.FILL);
                Bitmap pic = ((BitmapDrawable) getResources().getDrawable(
                        R.drawable.needle)).getBitmap();
                mCanvas.rotate(degree%360,450,450);
                mCanvas.drawBitmap(pic,150,150,null);
                mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    protected void onStop(){
        flag=false;
    }

    protected void onresume(){
        flag=true;
    }
}
