package com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces;

/**
 * Created by suwg on 2014/5/23.
 */

//加载逻辑
public interface FaceAutoLoadDataView {

    //
    boolean loadNextPage(boolean from_UI);

    //    从头加载  //刷新数据
    boolean reLoadFromStart();

    //    重新加载刚才的一页  （不怎么可能出现）
    boolean reload();

    void stopLoadData() ;

    //    图片机制
    void stopLoadImage();
    void pauseLoadImage();
    void startLoadImage();
    void clearLoadImage();













//主要是进行预加载
//    第一个 最后一个 滑动方向
//
    boolean onScrollShow(int firstVisibleItem, int lastVisibleItem, boolean toBottom, int mVisibleItemCount);//方向向下



}
