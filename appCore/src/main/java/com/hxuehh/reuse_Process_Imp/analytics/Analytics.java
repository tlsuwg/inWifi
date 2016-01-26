package com.hxuehh.reuse_Process_Imp.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-10-25
 * Time: 下午4:48
 * Analytics agent
 */
public class Analytics {

    private static final EventCache eventCache = new EventCache();

    private static final Map<String, String> events = new HashMap<String, String>();
    private static final Map<String, String> uiNameMap = new HashMap<String, String>();

    private static String _logUrl;
    private static IAnalyticsInfo analyticsInfo;

    /**
     * 当进入activity的事件需要参数时，在intent中放入一个bundle
     */
    public static final String INTENT_PARAM_NAME = "_analytics_params";

    public static final String RESUME_EVENT_NAME = "ui_resume";
    public static final String PAUSE_EVENT_NAME = "ui_pause";


    public static void init(Map<String, String> es, Map<String, String> uiMap, String logUrl) {

        events.clear();
        events.putAll(es);

        uiNameMap.clear();
        uiNameMap.putAll(uiMap);

        _logUrl = logUrl;
    }

    public static String getLogUrl() {
        return _logUrl;
    }

    public static IAnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo;
    }

    public static void setAnalyticsInfo(IAnalyticsInfo analyticsInfo) {
        Analytics.analyticsInfo = analyticsInfo;
    }

    public static String getEventId(String name) {
        String id = events.get(name);
        if (null == id) id = name;
        // Default event id is _
        return StringUtil.isEmpty(id) ? "_" : id;
    }

    public static String getUINameByClassName(String className) {
        String name = uiNameMap.get(className);
        return StringUtil.isEmpty(name) ? className : name;
    }

    /**
     * UI enter event
     * @param context       ui context
     * @param args          event args, format: argName:argValue
     */
    public static void onResume(Context context, String... args) {
        try {
            if (!(context instanceof Activity)) return;
            Activity a = (Activity) context;
            List<String> data = new ArrayList<String>();
            data.add("n:" + getUINameByClassName(a.getClass().getName()));
            if (args.length > 0) {
                data.addAll(Arrays.asList(args));
            }
            // 某些UI界面的进入事件需要自定义参数，放到一个bundle里加入intent
            Bundle extras = a.getIntent().getBundleExtra(INTENT_PARAM_NAME);
            if (null != extras) {
                for (String key: extras.keySet()) {
                    data.add(key + ":" + extras.get(key));
                }
            }
            eventCache.insertEvent(getEventId(RESUME_EVENT_NAME), data);
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }

    /**
     * UI exit event
     * @param context       ui context
     */
    public static void onPause(Context context) {
        try {
            List<String> data = new ArrayList<String>();
            data.add("n:" + getUINameByClassName(context.getClass().getName()));
            eventCache.insertEvent(getEventId(PAUSE_EVENT_NAME), data);
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }

    /**
     * Common event method
     * @param context       ui context
     * @param eventTag      event tag
     * @param args          event args, format: argName:argValue
     */
    public static void onEvent(Context context, String eventTag,String... args) {
        try {
            String eventId = getEventId(eventTag);
            eventCache.insertEvent(eventId, Arrays.asList(args));
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }

    public static void flush() {
        eventCache.flush();
    }

    public static String d(String str) {
        String k = "testkey";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(new String(new char[] { 'M', 'D', '5' }));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.w(e);
            return "";
        }
        return StringUtil.fromBytes(md.digest((k + "_" + str).getBytes()));
    }

    //tyl modified 获取cache，测试时注册它的监听
    public static EventCache getEventCache(){
        return eventCache;
    }

}
