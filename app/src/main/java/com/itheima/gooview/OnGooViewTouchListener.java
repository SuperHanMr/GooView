package com.itheima.gooview;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sszz on 2016/12/14.
 */

public class OnGooViewTouchListener implements View.OnTouchListener, GooView.OnGooViewChangeListener {

	private TextView tv_unread_msg_count;
	private WindowManager manager;
	private final GooView gooView;
	private final WindowManager.LayoutParams params;

	//WindowManager:这个类可以在任何的界面情况下添加一个额外的视图
	public OnGooViewTouchListener(Context context) {
		manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		gooView = new GooView(context);
		gooView.setOnGooViewChangListener(this);
		params = new WindowManager.LayoutParams();
		//宽度和高度是
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.format = PixelFormat.TRANSLUCENT;//类型是透明


	}


	//参数1:被触摸的那个视图
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//获取TextView控件的父布局即条目的根部局,通过根部局,让RecyclerView不要拦截事件
		v.getParent().requestDisallowInterceptTouchEvent(true);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//让TextView消失
				tv_unread_msg_count = (TextView) v;
				tv_unread_msg_count.setVisibility(View.INVISIBLE);
				float rawX = event.getRawX();
				float rawY = event.getRawY();
				gooView.initGooViewPosition(rawX, rawY);
				gooView.setText(tv_unread_msg_count.getText().toString());
				Log.i("test", "addView");
				manager.addView(gooView, params);
				break;
			case MotionEvent.ACTION_MOVE:
				//Log.i("test", "ACTION_MOVE");

				break;
			case MotionEvent.ACTION_UP:
				//Log.i("test", "ACTION_UP");

				break;
		}
		gooView.onTouchEvent(event);
		//表示自己想要处理事件
		return true;
	}

	private boolean isCanTouch;

	@Override
	public void onDisappear() {
		manager.removeView(gooView);

	}


	public void onReset() {
		//重置操作
		//1,移除GooView
		//WindowManager的addView方法是将GooView添加到root根部局中
		//removeView是将GooView从root根部局中移除,当移除后的GooView再次尝试从root中移除就会抛出
		//View not attached to window manager这样的异常
		//是由已经被WindowManager移除的视图,再此被移除
		//如果已经添加到root上的GooView会有一个Parent(父视图),判断父视图是否为空,就可以规避这个bug
		if (gooView.getParent() != null) {
			Log.i("test", "removeView");
			manager.removeView(gooView);
			//2,让TextView显示出来
			tv_unread_msg_count.setVisibility(View.VISIBLE);
		}
	}
}
