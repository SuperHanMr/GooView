package com.itheima.gooview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by hanyonghui on 2017/1/16.
 */

public class GooView extends View {

    private Paint paint;
    private Path path;
    private int statusarHeight;

    public GooView(Context context) {

        this(context,null);
    }

    public GooView(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }




    private Rect textRect;
    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        // 创建一个路径 绘制贝塞尔曲线
        path = new Path();
        paint.setTextSize(25);
        textRect = new Rect();
    }
    // 修改文字

    private String text = "";
    public void setText(String text){
        this.text =text;
    }

    public void  initGooViewPosition(float x,float y){
        // 在item条目里初始化GooView的位置
        stableCenter.set(x,y);
        dragCenter.set(x,y);
        invalidate();
    }

    // 先画两个圆
    // 这个是固定圆圆心
    private PointF stableCenter = new PointF(200f,200f);
    // 拖拽圆圆心
    private PointF dragCenter = new PointF(200f,200f);
    //固定圆半径
    private  float stableRadius = 30f;
    //拖拽圆半径
    private  float dragRabdius = 30f;

    //固定圆变化的最小半径
    private float minStableRadius = 5f;

    // 定义最大范围
    private float maxDistance = 380f;
    // 定义一个布尔值 来判断拖拽圆的距离 超过距离 就不绘制固定圆和贝塞尔曲线
    private    boolean isOutOfRange = false;
    // 判断是否全部消失
    private boolean isDisappear = false;




    // 固定圆的两个附着点用工具类获取
    private  PointF[] stablePoints = new PointF[]{new PointF(200f,300f),new PointF(200f,350f)};
    // 拖拽圆的两个附着点
    private PointF[] dragPoints = new PointF[]{new PointF(100f,300f),new PointF(100f,350f)};

    // 贝塞尔控制点
    private  PointF controlPoint = new PointF(150f,325f);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 平移整个画布
        canvas.save();
        canvas.translate(0,-statusarHeight);
        //随着拖拽圆和固定圆的圆心距离越来越大,固定圆的半径越来越小
        //拖拽圆和固定圆圆心的距离百分比变化==固定圆半径的百分比变化
        float distance = GeometryUtil.getDistanceBetween2Points(dragCenter,stableCenter);
        float percent = distance/maxDistance;
        float tempradius = GeometryUtil.evaluateValue(percent,stableRadius,minStableRadius);
        if (tempradius<minStableRadius){
            tempradius = minStableRadius;
        }



        if (!isDisappear) {

            if (!isOutOfRange) {


            // 计算两组的附着点 获取两个圆心连接线的斜率
            float dx = dragCenter.x - stableCenter.x;
            float dy = dragCenter.y - stableCenter.y;
            // 定义斜率
            double lineK = 0;
            if (dx != 0) {
                lineK = dy / dx;
            }

            // 将计算后的数据赋值给原有的静态数据
            dragPoints = GeometryUtil.getIntersectionPoints(dragCenter, dragRabdius, lineK);
            stablePoints = GeometryUtil.getIntersectionPoints(stableCenter, tempradius, lineK);
            controlPoint = GeometryUtil.getMiddlePoint(dragCenter, stableCenter);

            // 绘制中间图形的步骤：
            //1.移动到固定圆的附着点1
            path.moveTo(stablePoints[0].x, stablePoints[0].y);
            //2.向拖拽圆的附着点1制贝塞尔曲线
            path.quadTo(controlPoint.x, controlPoint.y, dragPoints[0].x, dragPoints[0].y);
            //3.向拖拽圆的附着点2绘制直线
            path.lineTo(dragPoints[1].x, dragPoints[1].y);
            //4.向固定圆的附着点2绘制贝塞尔曲线
            path.quadTo(controlPoint.x, controlPoint.y, stablePoints[1].x, stablePoints[1].y);
            //5.闭合
            path.close();
            canvas.drawPath(path, paint);
            path.reset();
            canvas.drawCircle(stableCenter.x, stableCenter.y, tempradius, paint);
            }

        canvas.drawCircle(dragCenter.x, dragCenter.y, dragRabdius, paint);
            drawText(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
               isOutOfRange = false;
                isDisappear = false;

                // 获取相对于屏幕的x和y坐标
                float rawX= event.getRawX();
                float rawY = event.getRawY();
                // 设置拖拽圆的圆形坐标为按下去的点的坐标
                dragCenter.set(rawX,rawY);
                //刷新页面
                invalidate();


                break;
            case MotionEvent.ACTION_MOVE:
                // 移动的时候和按下去的处理一致
                 rawX = event.getRawX();
                 rawY = event.getRawY();
                dragCenter.set(rawX,rawY);
                float distance = GeometryUtil.getDistanceBetween2Points(stableCenter,dragCenter);
                // 超出最大范围，断开链接
                if (distance>maxDistance){
                    isOutOfRange = true;
                }

                // 刷新页面
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                distance = GeometryUtil.getDistanceBetween2Points(dragCenter,stableCenter);
                Log.d("+++++++++++","1111111");
                if (isOutOfRange){
                    // 判断抬手的时候是否在最大范围内
                    Log.d("+++++++++++","333333");
                    if (distance>maxDistance){
                        Log.d("+++++++++++","444444");
                        isDisappear = true;
                        if(listener!=null){
                            listener.onDisappear();
                        }
                    }else{
                        // 抬手后，未超出最大范围
                        // 进行重置操作
                        Log.d("+++++++++++","555555");
                        dragCenter.set(stableCenter.x,stableCenter.y);
                        //做重置操作
                        if(listener!=null){
                            listener.onReset();
                        }
                    }
                }else{
                    //抬起手前没有超出最大范围,抬起手后也没有超出最大范围
                    //执行平移动画
                    // 记录松开手的瞬间拖拽圆的圆心
                    final PointF oldDragCenterPoint = new PointF(event.getRawX(),event.getRawY());
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(distance,0);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            // 获得值变化的百分比
                            float percent = animation.getAnimatedFraction();
                            // 根据百分比获取新的圆心点
                            PointF newPointF = GeometryUtil.getPointByPercent(oldDragCenterPoint,stableCenter,percent);
                            // 将新的圆心作为拖拽圆的圆心
                            dragCenter.set(newPointF.x,newPointF.y);
                            invalidate();
                        }
                    });

                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //重置操作
                            if(listener!=null){
                                listener.onReset();
                            }
                        }
                    });



                    // 设置动画插值器，使动画执行到目标点后有一个超出的效果
                    valueAnimator.setInterpolator(new OvershootInterpolator(5));
                    valueAnimator.setDuration(1500);
                    valueAnimator.start();
                }

                invalidate();
                break;
        }



        return true;
    }



    //绘制文本
    private void drawText(Canvas canvas){
        paint.setColor(Color.WHITE);
        //获取文本的边界:原理是将文本套入一个"空壳"矩形,这个矩形的宽高就是文本的宽高
        paint.getTextBounds(text,0,text.length(),textRect);
        float x = dragCenter.x-textRect.width()*0.5f;// 为拖拽圆心圆心横坐标+文本宽度*0.5f
        float y = dragCenter.y+textRect.height()*0.5f;// 为拖拽圆圆心纵坐标+文本宽度*0.5f
        canvas.drawText(text,x,y,paint);
        paint.setColor(Color.RED);
    }



// 出现一个BUG 就是落下的点和拖拽圆圆心不一致
    // 原因是状态栏的高度导致的 需要计算出状态栏的高度 然后平移整个画布 就ok了

    // 获取状态栏的高度
    private int getStatusarHeight(View view){
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        statusarHeight = getStatusarHeight(this);
    }


    public OnGooViewChangeListener listener;
    public interface OnGooViewChangeListener {
        /*
        Gooview的消失
         */
        void onDisappear();

        /*
        GooView重置处理
         */
        void onReset();
        /*

         */

    }

    public void setOnGooViewChangListener(OnGooViewChangeListener listener){
        this.listener = listener;
    }




}
