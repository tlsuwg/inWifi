package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;

/**
 * Created by suwg on 2014/5/20.
 */
public interface Parseable<GetOOForParse_Base, GetOOForParse_Container> {

    //    整体的解析 流程
    void parseAll(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, GetOOForParse_Base jsonString, long key);


    //    //主要是强调自己的获取过程
    Parseable getItemValue(LoadCursorSetting mLoadSetting, GetOOForParse_Container mJsonObject) throws Exception;


}
