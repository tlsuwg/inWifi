package com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ViewKeyable;

/**
 * Created by suwg on 2014/10/17.
 */

public abstract class GetMainView implements ViewKeyable, IsOnTop {

    protected View mainView;
    public View getMainView() {
        return mainView;
    }

    public View findViewById(int id) {
        return mainView.findViewById(id);
    }




    @Deprecated
    protected Context context;
    @Deprecated
    public Context getContext() {
        return context;
    }
    @Deprecated
    protected GetMainView(Context context) {
        this.context = context;
    }




    protected abstract void initView();
    protected int VISIBLE = View.VISIBLE;
    public void addIntoView(Activity activity, int v_title) {
        LinearLayout mLinearLayout = (LinearLayout) (activity.findViewById(v_title));
        mLinearLayout.addView(mainView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
    }

//    public void addIntoView(int v_title, Activity activity) {
//        LinearLayout mLinearLayout = (LinearLayout) (activity.findViewById(v_title));
//        mLinearLayout.addView(mainView, new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT)
//        );
//    }

    public void addIntoView(View viewP, int v_title) {
        ((LinearLayout) viewP.findViewById(v_title)).addView(mainView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
        );
    }





    protected int viewKey;
    @Override
    public int getViewKey() {
        return viewKey;
    }

    @Override
    public void setViewKey(int key) {
        viewKey = key;
    }




    protected boolean isOnTop;//是不是在前台界面关系到是不是需要回调 尤其是handler

    public boolean isOnTop() {
        return isOnTop;
    }

    public void setOnTop(boolean isOnTop) {
        this.isOnTop = isOnTop;
    }
}
