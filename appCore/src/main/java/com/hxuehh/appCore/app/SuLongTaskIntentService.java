package com.hxuehh.appCore.app;

import android.app.IntentService;
import android.content.Intent;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;

/**
 * Created by suwg on 2015/8/24.
 */
public class SuLongTaskIntentService extends IntentService {

    public SuLongTaskIntentService() {
        super("SuLongService");
    }
    private FaceCommCallBack mProxy;
//    public static final String classname=SuLongServiceFaceCallBackProxy.
    public static final String classname="com.hxuehh.rebirth.app.SuLongServiceFaceCallBackProxy";

    @Override
    protected void onHandleIntent(Intent intent) {
        Su.log("SuLongTaskIntentService"+intent+" "+intent.getAction());
        if (mProxy == null) {
            try {
                mProxy = (FaceCommCallBack) Class.forName(classname).newInstance();
                mProxy.callBack(this,intent);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
