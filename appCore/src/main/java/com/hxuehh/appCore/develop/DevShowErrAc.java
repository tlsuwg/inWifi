package com.hxuehh.appCore.develop;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;

/**
 * Created by suwg on 2015/9/17.
 */
public class DevShowErrAc extends FaceBaseActivity_1 {
    @Override
    public int getViewKey() {
        return 0;
    }

    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String info=getIntent().getStringExtra(AidlCacheKeys.Provisionality+"");
        ScrollView mScrollView=new ScrollView(getFaceContext());
        mTextView=new TextView(getFaceContext());
        mTextView.setText(info+"");
        mScrollView.addView(mTextView);
        setContentView(mScrollView);
    }
}
