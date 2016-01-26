package com.hxuehh.appCore.faceFramework.faceUI.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ViewKeyable;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrap;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;


/**
 * Created by suwg on 2014/5/20.
 */

public abstract class FaceBaseFragment extends Fragment implements  IsOnTop, ViewKeyable,FaceContextWrap {

    protected int viewKey;
    private FaceBaseActivity_1 mBaseActivity;

    public abstract int getViewKey();

    public void setViewKey(int key) {
        viewKey = key;
    }

    protected boolean isOnTop;//是不是在前台界面关系到是不是需要回调 尤其是handler

    public synchronized boolean isOnTop() {
        return isOnTop;
    }

    public abstract  void setOnTop(boolean isOnTop);


    public void onFaceFragmentResume() {
        if(!isBelieveFaceBaseFragment()&&!isOnTop())
            setOnTop(true);
    }
    public void onFaceFragmentPause() {
        if(!isBelieveFaceBaseFragment()&&isOnTop())
            setOnTop(false);
    }
    public FaceBaseFragment() {
        super();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mBaseActivity = (FaceBaseActivity_1) activity;
    }
    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

   private boolean isBelieveFaceBaseFragment=true;

    public boolean isBelieveFaceBaseFragment() {
        return isBelieveFaceBaseFragment;
    }

    public   void setBelieveFaceBaseFragment(boolean isBelieveFaceBaseFragment) {
        this.isBelieveFaceBaseFragment = isBelieveFaceBaseFragment;
    }

    public abstract boolean setBelieveFaceBaseFragment();

    @Override
    public void onResume() {//未必一定在前端
        super.onResume();
        if(isBelieveFaceBaseFragment()&&!isOnTop()) {
            setOnTop(true);
        }
    }

    @Override
    public void onPause() {////未必一定在前端
        super.onPause();
        if(isBelieveFaceBaseFragment()&&isOnTop()) {
            setOnTop(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBelieveFaceBaseFragment(setBelieveFaceBaseFragment());
    }


    public boolean isBelieveOnTop() {
        return (isBelieveFaceBaseFragment&&isOnTop)||!(!isBelieveFaceBaseFragment&&!isOnTop);
    }

    @Override
    public FaceBaseActivity_1 getFaceContext() {
        return mBaseActivity;
    }



}
