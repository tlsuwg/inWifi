package com.hxuehh.reuse_Process_Imp.analytics;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-10-26
 * Time: 下午4:46
 * Event cache
 */
public class EventCache {
    private List<Event> cache;
    private Handler handler;

    private static final Object lock = new Object();

    public static int SIZE_TO_FLUSH = 20;
    public static int IDLE_TIME_TO_FLUSH = 5 * 60 * 1000;

    private static final int MSG_FLUSH = 0;
    private static final int MSG_TIMEOUT = 1;

    private ExecutorService executorService;

    protected EventCache() {
        cache = new ArrayList<Event>();
        executorService = Executors.newSingleThreadExecutor();
        HandlerThread ht = new HandlerThread("_tuan800_analytics");
        ht.start();
        handler = new Handler(ht.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_FLUSH:
                        removeMessages(MSG_TIMEOUT);
                    case MSG_TIMEOUT:
                        sendToServer();
                        break;
                }
            }
        };
    }

    public void insertEvent(String eventTag, List<String> args) {
        insertEvent(new Event(eventTag, args));
    }

    public void insertEvent(final Event event) {
        handler.removeMessages(MSG_TIMEOUT);
        executorService.execute(new Runnable() {
            public void run() {
                synchronized (lock) {
                    cache.add(event);
                    if (cache.size() >= SIZE_TO_FLUSH) {
                        handler.sendEmptyMessage(MSG_FLUSH);
                    } else {
                        handler.sendEmptyMessageDelayed(MSG_TIMEOUT, IDLE_TIME_TO_FLUSH);
                    }
                }
            }
        });
    }

    public void flush() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_FLUSH);
            }
        });
    }

    private void sendToServer() {
        synchronized (lock) {
            if (cache.size() == 0) return;
            String logHeader = "";
            String body = "";
            try {
                logHeader = Analytics.getAnalyticsInfo().getLogHeader();
                List<String> es = new ArrayList<String>(cache.size());
                for (Event event: cache) {
                    es.add(event.toString());
                }
                cache.clear();
                body = StringUtil.join(es, ";");

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(Analytics.getLogUrl());

                List<NameValuePair> params = new ArrayList<NameValuePair>(3);
                params.add(new BasicNameValuePair("key", Analytics.d(body)));
                params.add(new BasicNameValuePair("header", logHeader));
                params.add(new BasicNameValuePair("data", body));

                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(entity);

                httpClient.execute(post);
            } catch (Exception e) {
                if(listener != null){
                    listener.onConnTimeOutListener(e);
                }
            }
            LogUtil.i(logHeader + " ====== " + body);
        }
    }

    //modified by tyl
    private ConnTimeOutListener listener;
    public void setListener(ConnTimeOutListener listener){
        this.listener = listener;
    }

    public ConnTimeOutListener getListener(){
        return listener;
    }

    public interface ConnTimeOutListener{
        void onConnTimeOutListener(Exception e);
    }
}
