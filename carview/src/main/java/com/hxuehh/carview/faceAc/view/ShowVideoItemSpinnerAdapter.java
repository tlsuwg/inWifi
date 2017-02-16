package com.hxuehh.carview.faceAc.view;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hxuehh.carview.domain.VideoXHItem;

import java.util.List;

/**
 * Created by suweiguang on 2016-09-01.
 */
public class ShowVideoItemSpinnerAdapter extends BaseAdapter implements SpinnerAdapter  {

    List<VideoXHItem> list;
    public ShowVideoItemSpinnerAdapter(List<VideoXHItem> list) {
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VideoXHItem mVideoXHItem=list.get(position);
        TextView mTextView=new TextView(parent.getContext());
        mTextView.setText(mVideoXHItem.w+"x"+mVideoXHItem.h+"参考流量:"+mVideoXHItem.liu+"KB/s");
        mTextView.setBackgroundColor(Color.parseColor("#FF7256"));

        return mTextView;
    }



}
