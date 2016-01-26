package com.hxuehh.appCore.faceFramework.faceDomain.utilDomain;

/**
 * Created by suwg on 2015/7/9.
 */
public class ViewAnalysisMode<T>  {
    int ActivityViewKey;
    int TabViewKey;
    int ViewKey;
    int indexView;
    Object[] oos;

    public ViewAnalysisMode(int activityViewKey, int tabViewKey, int indexView, int viewKey) {
        ActivityViewKey = activityViewKey;
        TabViewKey = tabViewKey;
        this.indexView = indexView;
        ViewKey = viewKey;
    }

    public ViewAnalysisMode setIndex(int i,T...oos) {
        indexView=i;
        this.oos=oos;
        return this;
    }

    public void commit(){

    }


}
