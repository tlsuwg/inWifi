package com.hxuehh.reuse_Process_Imp.staicUtil.parse;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;

/**
 * Created by suwg on 2014/5/20.
 */
public class FaceParesManganer {  public int MustEverTimeNumber = 20;//每次加载的数量
    public int MustLoadParmType = -1;//加载参数类型
    public int MustLoadMoreType = -1;//加载完毕解析是不是还存在继续的数据类型
    public int MustLoadedShowType = 1;//加载之后保存所有数据

    public static boolean  parseAsJSONArrayByObject(String json, int parseKey, LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack,String objKeys,long key) {
        return JsonModelParseUtil.parseAsJSONArrayByObject(json, parseKey, mLoadSetting, mFaceCallBack, objKeys, key);
    }

    public static boolean  parseAsJSONArrayByObject3(String json, int parseKey, LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack,String objKeys,long key) {
        return JsonModelParseUtil.parseAsJSONArrayByObject3(json, parseKey, mLoadSetting, mFaceCallBack, objKeys, key);
    }


    public static boolean  parseAsJSONArrayByObjectWhitTopic(String json, int parseKey, LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack,String objKeys,long key) {
        return JsonModelParseUtil.parseAsJSONArrayByObjectWhitTopic(json, parseKey, mLoadSetting, mFaceCallBack, objKeys, true, key);
    }



    public static boolean  parseAsJSONArrayByArray(String json, int parseKey, LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack,long key) {
        return JsonModelParseUtil.parseAsJSONArrayByArray(json, parseKey, mLoadSetting, mFaceCallBack, key);
    }
}
