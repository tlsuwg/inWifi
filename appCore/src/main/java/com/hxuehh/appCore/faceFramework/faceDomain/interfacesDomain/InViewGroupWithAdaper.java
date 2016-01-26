package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by suwg on 2014/5/20.
 */

// 在list View 的domain 类 需要实现的
// Params 为参数  主要是切换 list grid （或许还有其他需求）

public interface InViewGroupWithAdaper<InViewShow_Params> {

    View getView(Activity mContext, int i, View view, ViewGroup viewGroup, LayoutInflater inflater, int viewItemKey, InViewShow_Params... params) throws Exception;
    int getItemViewType(int position);
    int getViewTypeCount();

}
