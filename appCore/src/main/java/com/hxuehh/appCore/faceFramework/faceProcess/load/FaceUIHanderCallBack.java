package com.hxuehh.appCore.faceFramework.faceProcess.load;

import android.os.Handler;

import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.HandlerWrap;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.DevException;
import com.hxuehh.appCore.faceFramework.faceEcxeption.JsonParseException;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.FaceHttpLoadForAbsViewGetProducer;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.NetworkWorker;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.FaceHttpParamBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Future;
/**
 * Created by suwg on 2014/12/5.
 */
//只是在UI上面需要回调的
public  class FaceUIHanderCallBack implements FaceLoadCallBack,HandlerWrap {
    public FaceUIHanderCallBack(boolean isCallInUI, Handler mHandler, FaceLoadCallBack mUIFaceCallBack) {
        this.isCallInUI = isCallInUI;
        this.mHandler = mHandler;
        this.mUIFaceCallBack = mUIFaceCallBack;
        if(this.isCallInUI){
            if(this.mHandler==null)
                throw  new DevException("开发错误2014120516");
        }else{
            this.mHandler=null;
        }
    }
    private boolean isCallInUI;
    public boolean isCallInUI() {
        return isCallInUI;
    }
    public void setCallInUI(boolean isCallInUI) {
        this.isCallInUI = isCallInUI;
    }
    @Override
    public boolean onCallBackErr(final int key, final Object params) {
        Su.logE("FaceUIHanderCallBack err == " + key + (params == null ? " ； null" : params.toString()));
        if(this.isCallInUI()){
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mUIFaceCallBack.onCallBackErr(key,params);
                }
            });
        }else{
            mUIFaceCallBack.onCallBackErr(key,params);
        }
        return false;
    }
    @Override
    public boolean onCallBackData(final int from,final  Object re,final Object data, JSONObject params,final long key) {
        Su.log("FaceUIHanderCallBack data: " + FaceLoadCallBack.fromNames[from - 1] + "  " + (data == null ? "data null" : "data:" + (data instanceof List ? "list" : (DevRunningTime.isShowHttPDataForShort ? data : "!http"))) + "   " + (params == null ? "params null" : "params:" + params));
        if(FaceLoadCallBack.FROM_HTTP==from&&data!=null&&params==null){
            String json=(String)data;
            if(json.startsWith("{")) {
                try {
                    params = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mUIFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException("201412180958" + e.getMessage()));
                    return false;
                }
            }else  if(json.startsWith("[")) {
                try {
                    params = new JSONObject("{array:"+json+"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                    mUIFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_JSON, new JsonParseException("201412180958" + e.getMessage()));
                    return false;
                }

            }
        }
        final   JSONObject params2=params;
        if(this.isCallInUI()){
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mUIFaceCallBack.onCallBackData(from,re,data,params2,key);
                }
            });
        }else{
            mUIFaceCallBack.onCallBackData(from,re,data,params2,key);
        }
        return true;
    }

    private Handler mHandler;
    private FaceLoadCallBack mUIFaceCallBack;
    public Handler getHandler() {
        return mHandler;
    }
    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }
    public FaceLoadCallBack getmUIFaceCallBack() {
        return mUIFaceCallBack;
    }
    public void setmUIFaceCallBack(FaceLoadCallBack mUIFaceCallBack) {
        this.mUIFaceCallBack = mUIFaceCallBack;
    }

    public Future sendHttpForGetCallBack(final String url, final FaceHttpParamBuilder param, final boolean isPost) {
        Future mFuture;
        Runnable mRunnable= new Runnable() {
            @Override
            public void run() {
                FaceHttpLoadForAbsViewGetProducer.getInstance().produceByHttp(url,param,FaceUIHanderCallBack.this,isPost,0);
            }
        };
        if(this.isCallInUI()){
            mFuture=  ThreadManager.getInstance().submitUIThread(mRunnable);
        }else{
            mFuture=   NetworkWorker.getInstance().getThreadPool().submit(mRunnable);
        }
        return mFuture;
    }
}
