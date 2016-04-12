package com.hxuehh.reuse_Process_Imp.exceptionHandler;

import android.content.Context;

import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.NetworkWorker;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.HttpRequester;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kait
 * Date: 12-12-17
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class ForceCloseSeedBack {

    private static final String FB_URL = "http://m.api.TuanDD.com/mobilelog/errorlog/android";

    private static ForceCloseSeedBack feedBack;
    public static ForceCloseSeedBack getInstance() {
        if(feedBack==null)feedBack = new ForceCloseSeedBack();
        return feedBack;
    }


    private Context mContext;
    private ForceCloseSeedBack() {}

    public void feedBack(Context context, String content) {
        mContext = context;
        HttpRequester requester = new HttpRequester();
        HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("content", content);
        requester.setParams(params);
         NetworkWorker.getInstance().post(FB_URL, requester);
    }

    private HashMap<String, Object> getForceCloseLogs() {
        File file = FileUtil.getDiskCacheDir(mContext, ForceCloseHandler.LOG_FILE_NAME);
        HashMap<String, Object> logs = new HashMap<String, Object>(1);
        try {
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            logs.put("content", new String(buffer));
        } catch (Exception e) {
            LogUtil.w(e);
        }

        return logs;
    }

    class FeedBackResult implements NetworkWorker.ICallback {

        @Override
        public void onResponse(int status, String result) {
            if (status == 200) {
                // clear log files
                FileUtil.getDiskCacheDir(mContext, ForceCloseHandler.LOG_FILE_NAME).delete();
            }
        }

    }

}
