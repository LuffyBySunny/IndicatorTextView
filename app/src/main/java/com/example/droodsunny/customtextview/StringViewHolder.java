package com.example.droodsunny.customtextview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DroodSunny on 2018/3/15.
 */

public class StringViewHolder extends RecyclerView.Adapter {

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            //实例化控件
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        //添加监听事件
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //设置布局资源

    }
    @Override
    public int getItemCount() {
        //获取传入数据的大小
        return 0;
    }
}
