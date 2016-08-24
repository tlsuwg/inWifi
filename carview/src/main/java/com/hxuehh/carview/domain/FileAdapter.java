package com.hxuehh.carview.domain;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxuehh.carview.R;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by suweiguang on 2016-08-16.
 */
public class FileAdapter extends BaseAdapter {

    List<File> list;
    private LayoutInflater mInflater;

    public FileAdapter(List<File> list,Context mContext) {
        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.video_file_item_lay, null);
            holder.title = (TextView) convertView.findViewById(R.id.video_item_name);
            holder.info = (TextView) convertView.findViewById(R.id.video_item_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f=list.get(position);
        holder.title.setText(f.getName());
        holder.info.setText(FileUtil.convertFileSize(f.length()));
        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list.size() == 0;
    }


    private class ViewHolder {
        public TextView title;
        public TextView info;
    }
}
