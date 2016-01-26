package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;

import java.util.concurrent.Future;

/**
 * Created by suwg on 2014/5/20.
 */


public interface LoadProcess<LoadedSettingParameters, GetOOForDomain_Container> {//主要是强调加载的过程


    //    从哪里加载
    Future loadInWhich_Thread_Source(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack);

    //    加载设置项
    void loadSetting(LoadCursorSetting mLoadSetting, GetOOForDomain_Container data, FaceLoadCallBack mFaceCallBack, LoadedSettingParameters json);



    //    这几个函数的不需要接口
    boolean loadTrueByMCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);

    boolean loadTrueByDBCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);

    boolean loadTrueByFileCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);

    boolean loadTrueByHttp(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key);


}
