package com.itheima.myreclcerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<String> mDatas;
    private MyRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 造假数据
        initData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // 创建管理器
        LinearLayoutManager lyoutManager = new LinearLayoutManager(this);
        // 给recyclerView 设置管理器
        recyclerView.setLayoutManager(lyoutManager);
        //lyoutManager.setOrientation(OrientationHelper.VERTICAL);
        StaggeredGridLayoutManager sta = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(sta);


        // 设置Adapter
        recyclerAdapter = new MyRecyclerAdapter(MainActivity.this,mDatas);
        recyclerView.setAdapter(recyclerAdapter);
        // 设置分割线i
        // TODO: recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置增加或者删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            mDatas.add("测试"+i);
        }
    }


}
