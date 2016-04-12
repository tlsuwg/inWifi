package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain;

import com.hxuehh.appCore.faceFramework.faceProcess.net.FaceBaseHttpParamBuilder;

import java.util.HashMap;
import java.util.Map;

//http加载参数

public class FaceHttpParamBuilder extends FaceBaseHttpParamBuilder{





    SessionCookie cookie;
    public void setCookie(SessionCookie s) {
        this.cookie=s;
    }
    public SessionCookie getCookie() {
        return cookie;
    }






    Map<String, String> headers;
    public Map<String, String> getHead() {
        return headers;
    }


    boolean setUserInfo;
    public void setUserInfoBoolean() {
        setUserInfo=true;
        setUserInfo();
    }

    public boolean isSetUserInfo() {
        return setUserInfo;
    }

    public void setUserInfo(){
//        put(ParamBuilder.USER_TYPE, TaoCCUtil.isOldUesr() ? 1 : 0);
//        put(ParamBuilder.USER_ROLE, TaoCCUtil.getUserRole());
//        put(ParamBuilder.STUDENT, (PreferencesUtils.getBoolean(IntentBundleFlag.ISSTUDENT) ? 1 : 0));
    }

}

