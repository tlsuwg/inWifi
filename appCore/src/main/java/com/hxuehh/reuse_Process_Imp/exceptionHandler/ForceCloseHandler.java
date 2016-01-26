package com.hxuehh.reuse_Process_Imp.exceptionHandler;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.appCore.develop.LogUtil;

import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;


import com.hxuehh.reuse_Process_Imp.staticKey.IntentBundleFlagStatic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;


public class ForceCloseHandler implements Thread.UncaughtExceptionHandler {

    public static final String LOG_FILE_NAME = "forceclose.log";

    private static ForceCloseHandler inst ;
    public static ForceCloseHandler getInstance() {
        if(inst==null)inst = new ForceCloseHandler();
        return inst;
    }


    private Context mContext;
    private Thread.UncaughtExceptionHandler mExceptionHandler;//默认
    private JSONObject jsonObject;
    private ForceCloseHandler() {}

    public void init(Context context) {
        mContext = context;
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);//设置自己
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handleUncaughtException(throwable);
        if (throwable != null) LogUtil.w(throwable);
        mExceptionHandler.uncaughtException(thread, throwable);
    }


    private void handleUncaughtException(Throwable ex) {
        if (ex == null) return;
        try {
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                saveDeviceInfo();
            }
            saveForceCloseInfo2File(ex);
            ForceCloseSeedBack.getInstance().feedBack(mContext, jsonObject.toString());
            // 记录发生崩溃
            saveCrashedState();
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }

    /**
     * 保存崩溃的状态,下次进入客户端显示对话框
     */
    private void saveCrashedState() {
        SharedPreferencesUtils.putBoolean(IntentBundleFlagStatic.HAS_CRASHED, true);
    }


    public void saveDeviceInfo() throws JSONException {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        jsonObject.put("platform", "android");
        jsonObject.put("model", Build.MODEL);
        jsonObject.put("trackid", AppConfig.PARTNER_ID);
        jsonObject.put("product", AppConfig.getInstance().CLIENT_TAG);
        jsonObject.put("os_version", Build.VERSION.RELEASE);
        jsonObject.put("deviceid", DeviceUtil.getimei());
        jsonObject.put("net_type", tm.getNetworkOperator());
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("app_version", SuApplication.getInstance().getVersionCode());
    }

    private void saveForceCloseInfo2File(Throwable ex) throws Exception {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString() + "\n";
        jsonObject.put("errorDetail", result);
        if (AppConfig.getInstance().LOG_ERR_SAVE) {
            File file = FileUtil.getDiskCacheDir(mContext, LOG_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(jsonObject.toString().getBytes());
            fos.close();
        }
    }

}
