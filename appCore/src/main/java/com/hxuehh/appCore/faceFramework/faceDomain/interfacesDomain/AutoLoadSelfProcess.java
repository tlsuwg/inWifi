package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;

import java.util.concurrent.Future;

/**
 * Created by suwg on 2014/5/20.
 */
public interface AutoLoadSelfProcess<LoadedParameters, LoadedObjects> {//主要是强调加载的过程


    //    从哪里加载
    Future loadSelfInWhich_Thread_Source(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack);


    //    加载设置项
    void loadSetting(LoadCursorSetting mLoadSetting, LoadedObjects data, FaceLoadCallBack mFaceCallBack, LoadedParameters json);






      //    这几个函数的不需要接口
    boolean loadTrueByMCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);

    boolean loadTrueByDBCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);

    boolean loadTrueByFileCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);

    boolean loadTrueByHttp(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);



}
