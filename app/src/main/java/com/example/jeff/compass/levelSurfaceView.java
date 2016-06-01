package com.example.jeff.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jeffdeen on 2016/4/30.
 */
public class levelSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback{
    private SurfaceHolder mHolder; // 用于控制SurfaceView
    private Thread t; // 声明一条线程
    private volatile boolean flag; // 线程运行的标识，用于控制线程
    private Canvas mCanvas; // 声明一张画布
    float bubbleX, bubbleY;
    int length=134;
    int MAX_ANGLE = 70;
    int radius=48;
    Paint paint=new Paint();
    public levelSurfaceView(Context context) {
        super(context);

        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        setFocusable(true); // 设置焦点
    }
    public levelSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        setFocusable(true); // 设置焦点
    }

    public levelSurfaceView(Context context, AttributeSet attrs, int defStyle) {
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

    /**
     * 自定义一个方法，在画布上画一个圆
     */
    protected void Draw() {
        try{
            mHolder = getHolder();
            mCanvas = mHolder.lockCanvas();// 获得画布对象，开始对画布画画
            //mCanvas.drawColor(Color.WHITE);
            mCanvas.drawColor( 0, PorterDuff.Mode.CLEAR );//
            if (mCanvas != null) {
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(0.5f);
                mCanvas.drawCircle(bubbleX,bubbleY,6,paint);
                mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
            }
        }catch(java.lang.NullPointerException e){
            e.printStackTrace();
        }
    }
    protected void updateLevelView(float zAngle,float yAngle){
        // 气泡位于中间时（水平仪完全水平），气泡的X、Y座标
        int x = length/2;
        int y = length/2;
        // 如果与Z轴的倾斜角还在最大角度之内
        if (Math.abs(zAngle) <= MAX_ANGLE) {
            // 根据与Z轴的倾斜角度计算X座标的变化值（倾斜角度越大，X座标变化越大）
            int deltaX = (int) (radius* zAngle / MAX_ANGLE);
            x -= deltaX;
        }
        // 如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
        else if (zAngle > MAX_ANGLE) {
            x = 116;
        }
        // 如果与Z轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
        else {
            x = 16;
        }
        // 如果与Y轴的倾斜角还在最大角度之内
        if (Math.abs(yAngle) <= MAX_ANGLE) {
            // 根据与Y轴的倾斜角度计算Y座标的变化值（倾斜角度越大，Y座标变化越大）
            int deltaY = (int) (radius * yAngle / MAX_ANGLE);
            y += deltaY;
        }
        // 如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
        else if (yAngle > MAX_ANGLE) {
            y = 116;
        }
        // 如果与Y轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
        else {
            y = 16;
        }
        // 如果计算出来的X、Y座标还位于水平仪的仪表盘内，更新水平仪的气泡座标
        if (isContain(x, y)) {
            bubbleX = x;
            bubbleY = y;
        }
        // 通知系统重回MyView组件
        //show.postInvalidate();
    }
    private boolean isContain(int x, int y) {
        // 计算水平仪仪表盘的圆心座标X、Y
        int backCx = length / 2;
        int backCy = length / 2;
        // 计算气泡的圆心与水平仪仪表盘的圆心之间的距离。
        double distance = Math.sqrt((x - backCx) * (x - backCx)
                + (y - backCy) * (y - backCy));
        // 若两个圆心的距离小于它们的半径差，即可认为处于该点的气泡依然位于仪表盘内
        if (distance < radius) {
            return true;
        } else {
            return false;
        }
    }
    protected void onStop(){
        flag=false;
    }

    protected void onresume(){
        flag=true;
    }

}
