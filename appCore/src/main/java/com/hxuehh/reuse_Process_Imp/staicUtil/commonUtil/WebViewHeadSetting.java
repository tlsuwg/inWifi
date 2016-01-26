package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import java.util.HashMap;

/**
 * Created by suwg on 2014/5/13.
 */
public class WebViewHeadSetting {
    public static HashMap headMap;
    static {
        headMap=new HashMap();
        headMap.put("X-Requested-With",null);
//        headMap.put("X-Requested-With", null);
//        headMap.put("X-Wap-Proxy-Cookie","delete");
//        headMap.put("Content-Type","text/html; charset=utf-8; X-Wap-Proxy-Cookie=delete");
//        headMap.put("Accept"," text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8; X-Wap-Proxy-Cookie=none");
    }


}

