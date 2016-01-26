package com.hxuehh.reuse_Process_Imp.staicUtil.parse;

import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.JsonParseException;
import com.hxuehh.appCore.faceFramework.faceEcxeption.NoAddException;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Parseable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonModelParseUtil {

    public static boolean parseAsJSONArrayByObject(String response, int parseType, LoadCursorSetting mLoadSetting,
                                                   FaceLoadCallBack mFaceCallBack, String parseKey,long key) {
        return parseAsJSONArrayByObjectWhitTopic(response,parseType,mLoadSetting,mFaceCallBack,parseKey,false,key);
    }


    public static boolean parseAsJSONArrayByObject3(String response, int parseType, LoadCursorSetting mLoadSetting,
                                                    FaceLoadCallBack mFaceCallBack, String parseKey,long key) {
        if (StringUtil.isEmpty(response)) {
            if (mFaceCallBack != null)
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON_NULL, null);
            return false;
        }

        JSONArray jsonArray = null;
        JSONObject jsonAll = null;
        JSONObject jsonAll_1 = null;
        JSONObject jsonMeta = null;

        try {
            if (!response.startsWith("{")) {
                StringBuilder sb = new StringBuilder();
                sb.append("{").append(response).append("}");
                response = sb.toString();
            }
//            Su.log("json string:"+response);
            jsonAll = new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.w(e);
            if (mFaceCallBack != null)
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
            return false;
        }

        if (null != jsonAll) {
            try {
                jsonAll_1=jsonAll.getJSONObject("data");
            } catch (JSONException e) {
                if (mFaceCallBack != null)
                    mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
                return false;
            }
        }


        if (null != jsonAll_1) {

            try {
                if (parseKey == null) {
                    jsonArray = jsonAll_1.getJSONArray("objects");
                    jsonMeta = jsonAll_1.getJSONObject("meta");
                } else {
                    jsonMeta = jsonAll_1;
                    jsonArray = jsonAll_1.optJSONArray(parseKey);
                }
            } catch (JSONException e) {
                LogUtil.w(e);
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
                return false;
            }
        } else {
            Su.log(" 没有需要的对象json");
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException("没有需要的对象json"));
            return false;
        }

        return getJsonArray(jsonArray, jsonMeta, mLoadSetting, mFaceCallBack,key);
    }



    //直接解析一个数组
    public static boolean parseAsJSONArrayByArray(String response, int parseKey, LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack,long key) {
        if (StringUtil.isEmpty(response)) {
            if (mFaceCallBack != null)
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON_NULL, null);
            return false;
        }

        JSONArray jsonArray = null;
        JSONObject jsonMeta = null;//这个地方存在危险
        try {
            if (!response.startsWith("[")) {
                StringBuilder sb = new StringBuilder();
                sb.append("[").append(response).append("]");
                response = sb.toString();
            }
//          Su.log("json string:"+response);
            jsonArray = new JSONArray(response);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.w(e);
            if (mFaceCallBack != null)
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
            return false;
        }
        return getJsonArray(jsonArray, jsonMeta, mLoadSetting, mFaceCallBack, key);
    }



    //最单纯的解析出对象
    private static boolean getJsonArray(JSONArray jsonArray, JSONObject jsonMeta, LoadCursorSetting mLoadSetting,
                                        FaceLoadCallBack mFaceCallBack,long key) {

        ArrayList<Object> list = new ArrayList<Object>();


        boolean isHasErr = false;
        if (jsonArray == null) {
            Su.log(" 没有需要的对象json");
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException("没有需要的对象json"));
            isHasErr = true;
        } else {
            int length = jsonArray.length();
            Object mode = mLoadSetting.getObj();
            BaseAllImp mFaceParseCloneMode = (BaseAllImp) mode;

            for (int i = 0; i < length; i++) {
                BaseAllImp object = null;
                if(mLoadSetting.MustHybridizationLoadType
                        !=LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains) {//不是复制
                    try {
                        object = (BaseAllImp) mFaceParseCloneMode.clone();
                    } catch (CloneNotSupportedException e) {
                        LogUtil.w(e);
                        mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_clone, new JsonParseException(e));
                        isHasErr = true;
                        break;
                    }
                }else {
                    object=mFaceParseCloneMode;
                }
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) {
                        continue;
                    }
                    try {
                        Parseable oos=  object.getItemValue(mLoadSetting, jsonObject);
                        if (oos != null) {
                            list.add(oos);
                        }
                    }catch (NoAddException e){
                        Su.logPullView("不添加的元素");
                    }

                } catch (Exception e) {
                    LogUtil.w(e);
                    if (mFaceCallBack != null)
                        mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
                    isHasErr = true;
                    break;
                }
            }
        }

        if (!isHasErr)
            mFaceCallBack.onCallBackData(FaceLoadCallBack.FROM_DOMAIN,null, list, jsonMeta,key);

        return !isHasErr;
    }

    public static boolean parseAsJSONArrayByObjectWhitTopic(String response, int parseType, LoadCursorSetting mLoadSetting,
                                                            FaceLoadCallBack mFaceCallBack, String parseKey,boolean isTopic,long key) {

        if (StringUtil.isEmpty(response)) {
            if (mFaceCallBack != null)
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON_NULL, null);
            return false;
        }

        JSONArray jsonArray = null;
        JSONObject jsonAll = null;
        JSONObject jsonMeta = null;

        try {
            if (!response.startsWith("{")) {
                StringBuilder sb = new StringBuilder();
                sb.append("{").append(response).append("}");
                response = sb.toString();
            }
//            Su.log("json string:"+response);
            jsonAll = new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.w(e);
            if (mFaceCallBack != null)
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
            return false;
        }

        if (null != jsonAll) {
            try {
                if (parseKey == null) {
                    jsonArray = jsonAll.getJSONArray("objects");
                    if(isTopic&&jsonAll.has("tip")) {
                        JSONObject jsontip = jsonAll.getJSONObject("tip");
                        jsonMeta = jsonAll.getJSONObject("meta");
                        jsonMeta.put("tip",jsontip);
                    }else {
                        jsonMeta = jsonAll.getJSONObject("meta");
                    }
                } else {
                    jsonMeta = jsonAll;
                    jsonArray = jsonAll.optJSONArray(parseKey);
                }
            } catch (JSONException e) {
                LogUtil.w(e);
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException(e));
                return false;
            }
        } else {
            Su.log(" 没有需要的对象json");
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException("没有需要的对象json"));
            return false;
        }

        return getJsonArray(jsonArray, jsonMeta, mLoadSetting, mFaceCallBack,key);
    }
}
