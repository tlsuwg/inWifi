package com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces;

import com.hxuehh.appCore.develop.FaceTestforDlp;

/**
 * Created by suwg on 2014/5/23.
 */

//就是界面回调 分发
//    只能是个内部类存在了  用于视图层和数据层的数据回调 和数据控制的中间层
public interface ViewCallBackDispenser_A<Data, Err> {

    //    正在加载
    void loading();

    //    加载完成 回调数据到界面
    void loaded(Long loadTime, int thisPagenumber, boolean isFrom0, boolean isHasMoreToload, Data data);

    //    加载错误 错误码 次数 详情
    void loadErr(int errKey, int errTimes, boolean isFrom0, Err... err);

    //界面是不是刷新了，刷新到完成  hander延迟
    boolean isViewLoading();


    @FaceTestforDlp
        //stop
    void setViewLoading(boolean isViewLoading);
}
