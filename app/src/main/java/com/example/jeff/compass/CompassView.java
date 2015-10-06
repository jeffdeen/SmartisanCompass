package com.example.jeff.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by dingj on 2015/9/26.
 */
class CompassView extends ImageView {
    private float mDirection;// 方向旋转浮点数
    private Drawable compass;// 图片资源
    private int color = 0;
    private Paint mPaint;
    private int mAscent;

    //三个重载构造方法
    public CompassView(Context context) {
        super(context);
        mDirection = 0.0f;// 默认不旋转
        compass = null;
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDirection = 0.0f;
        compass = null;
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDirection = 0.0f;
        compass = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (compass == null) {
            compass = getDrawable();// 获取当前view的图片资源
            compass.setBounds(0, 0, getWidth(), getHeight());// 图片资源在view的位置，此处相当于充满view
        }

        canvas.save();
        canvas.rotate(mDirection, getWidth() / 2, getHeight() / 2);// 绕图片中心点旋转，
        compass.draw(canvas);// 把旋转后的图片画在view上，即保持旋转后的样子
        canvas.restore();// 保存一下
    }

    /**
     * 自定义更新方向的方法
     *
     * @param direction
     *            传入的方向
     */
    public void updateDirection(float direction) {
        mDirection = direction;
        invalidate();// 重新刷新一下，更新方向
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }*/
}

