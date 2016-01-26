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



    boolean isSetZhe800Filter;

    public void setZhe800Filter() {
        isSetZhe800Filter=true;
        setZhe800FilterParm();
    }

    public boolean isSetZhe800Filter() {
        return isSetZhe800Filter;
    }

    public void setZhe800FilterParm() {
        if (headers == null)
            headers = new HashMap<String, String>();
//        headers.put("X-Zhe800out", Zhe800Filter.getOutCount());
//        headers.put("X-Zhe800filter", Zhe800Filter.getFilterStr());
//        headers.put("X-Zhe800userid", Session2.isLogin() ? Session2.getLoginUser().getId() : "");
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
//        put(ParamBuilder.USER_TYPE, Tao800Util.isOldUesr() ? 1 : 0);
//        put(ParamBuilder.USER_ROLE, Tao800Util.getUserRole());
//        put(ParamBuilder.STUDENT, (PreferencesUtils.getBoolean(IntentBundleFlag.ISSTUDENT) ? 1 : 0));
    }

}

