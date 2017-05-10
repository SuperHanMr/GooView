package com.itheima.gooview;

import android.app.Activity;
import android.content.Context;
import android.net.sip.SipAudioCall;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    List<Msg> mDatalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatalist = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mDatalist.add(new Msg("QQ联系人" + i, i));
        }
        RecyclerView rlv = (RecyclerView) findViewById(R.id.rlv);
        // 获取RecylceViewg管理器
        rlv.setLayoutManager(new LinearLayoutManager(this));
        rlv.setAdapter(new MsgAdapter(mDatalist,this));


    }

    public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {

        private List<Msg> msglist;
        private OnGooViewTouchListener listener;

        public MsgAdapter(List<Msg> msglist, Context context) {
            listener = new OnGooViewTouchListener(context);
            this.msglist = msglist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rlv_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_title.setText(msglist.get(position).title);
            // 判断当前未读消息定于0；则隐藏对应的TextView控件
            int unReadMagcount = msglist.get(position).unReadMsgCount;
            if (unReadMagcount == 0) {
                holder.tv_num.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_num.setVisibility(View.VISIBLE);
                holder.tv_num.setText(unReadMagcount + "");
            }
            //监听对应控件的触摸事件
            //和重写onTouchEvent是有区别的
            //如果一个控件重写了onTouchEvent返回true,且设置触摸监听返回true,则MotionEvent交给OnTouchListener
                holder.tv_num.setOnTouchListener(listener);

        }

        @Override
        public int getItemCount() {
            return msglist.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_title;
            public TextView tv_num;

            public MyViewHolder(View itemView) {

                super(itemView);

                tv_title = (TextView) itemView.findViewById(R.id.tv_tilte);
                tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            }
        }
    }


}
