package com.example.jeff.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by dingj on 2015/9/26.
 */
public class compassView extends ImageView {
    private float mDirection;// 方向旋转浮点数
    private Drawable compass;// 图片资源
    /** 图片宽和高的比例 */
    private float ratio = 1.0f;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    //三个重载构造方法
    public compassView(Context context) {
        super(context);
        mDirection = 0.0f;// 默认不旋转
        compass = null;
    }


    public compassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDirection = 0.0f;
        compass = null;
    }

    public compassView(Context context, AttributeSet attrs, int defStyle) {
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
        invalidate();// 重新刷新一下
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 父容器传过来的宽度方向上的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 父容器传过来的宽度的值
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        // 父容器传过来的高度的值
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
                - getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY
                && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
            // 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
            // 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
            // 且图片的宽高比已经赋值完毕，不再是0.0f
            // 表示宽度确定，要测量高度
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY
                && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
            // 判断条件跟上面的相反，宽度方向和高度方向的条件互换
            // 表示高度确定，要测量宽度
            width = (int) (height * ratio + 0.5f);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                    MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

