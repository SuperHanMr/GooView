package com.itheima.gooviewtext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hanyonghui on 2017/1/17.
 */

public class Gooview extends View {

    private Paint paint;

    public Gooview(Context context) {
        this(context,null);
    }

    public Gooview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Gooview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intt();
    }

    private void intt() {
        // 让画笔没有锯齿 ANTI_ALIAS_FLAG
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    //定义两个圆的圆心和半径
    // 固定圆圆心
    private PointF stableCenter = new PointF(200f,200f);
    // 固定圆的半径
    private float stableRadius = 20f;
    // 拖拽圆的圆心
    private PointF dragCenter = new PointF(80f,200f);
    // 拖拽圆的半径
    private float dragRadius  = 20f;
    //固定圆的两个附着点
    private PointF[] stablPoints = new PointF[]{new PointF(200f,300f),
            new PointF(200f,350f)};
    private PointF[] dragPoints = new PointF[]{new PointF(100f,300),
            new PointF(100f,350f)};
    // 拖拽圆的最大距离


    private float maxDragDistance = 200f;
    private  float minstableRadius = 5f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float distance = GeometryUtil.getDistanceBetween2Points(dragCenter,stableCenter);
        float percent = distance/maxDragDistance;
        float tempRadius = GeometryUtil.evaluateValue(percent,stableRadius,minstableRadius);
        if(tempRadius<minstableRadius){
            tempRadius = minstableRadius;
        }
        canvas.drawCircle(stableCenter.x,stableCenter.y,stableRadius,paint);
        canvas.drawCircle(dragCenter.x,dragCenter.y,dragRadius,paint);

        // 计算两个圆圆心连接的斜率
        float dx = dragCenter.x-stableCenter.x;
        float dy = dragCenter.y-dragCenter.y;
        // 定义斜率
        double lineK = 0;
        if (dx!=0){
            lineK = dx/dy;
        }




    }
}
