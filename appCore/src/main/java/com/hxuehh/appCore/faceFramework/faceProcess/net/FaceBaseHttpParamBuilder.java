package com.hxuehh.appCore.faceFramework.faceProcess.net;

import android.net.Uri;
import android.text.TextUtils;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import java.util.HashMap;
import java.util.Iterator;

//http加载参数

public abstract class FaceBaseHttpParamBuilder {


    private HashMap<String, Object> paramsMap = new HashMap<String, Object>();

    public Object put(String param, Object value) {
        return paramsMap.put(param, value);
    }


    public void putStrings(String param[], Object value[]) {
        for(int i=0;i<param.length;i++) {
             paramsMap.put(param[i], value[i]);
        }
    }


    public void clear() {
        paramsMap.clear();
    }
    public FaceBaseHttpParamBuilder() {
        super();
//        paramsMap.put("platform", AppConfig.PRODUCT_TAG);
//        paramsMap.put("model", android.os.Build.MODEL);
    }

    public String parseGetUrl(String path) {
        StringBuffer sBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(path)) {
            sBuffer.append(path);
        }
        if (StringUtil.isEmpty(paramsMap)) return sBuffer.toString();
        Iterator it = paramsMap.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = paramsMap.get(key);
            if (i == 0) {
                sBuffer.append("?");
            } else {
                sBuffer.append("&");
            }
            i++;
            String u = (value == null) ? "" : value.toString();
            sBuffer.append(key).append("=").append(Uri.encode(u));
        }
        return sBuffer.toString();
    }

    public Object get(String limit) {
        return paramsMap.get(limit);
    }


    public Object remove(String limit) {
        return paramsMap.remove(limit);
    }


    public static  String DOMAIN = "/zhenCC9.com";
}

