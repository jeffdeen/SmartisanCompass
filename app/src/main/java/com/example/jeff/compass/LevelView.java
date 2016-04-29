package com.example.jeff.compass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jeffdeen on 2016/4/26.
 */
public class LevelView extends View{
    // 定义水平仪仪表盘图片
    //Bitmap back;
    // 定义水平仪中的气泡图标
    Bitmap bubble;
    // 定义水平仪中气泡 的X、Y座标
    int bubbleX, bubbleY;
    int length=160;

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载水平仪图片和气泡图片
        //back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        bubble = BitmapFactory
                .decodeResource(getResources(), R.drawable.bubble);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制水平仪表盘图片
        //canvas.drawBitmap(back, 0, 0, null);
        // 根据气泡座标绘制气泡
        canvas.drawBitmap(bubble, bubbleX, bubbleY, null);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }else{
            width=150;
        }
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }else{
            height=150;
        }
        setMeasuredDimension(width, height);
    }
}
