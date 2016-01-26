package com.hxuehh.appCore.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.hxuehh.appCore.develop.FaceEventViewINforDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by suwg on 2015/8/20.
 */
public class SuSuperService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected  MyBinder mMyBinder=new MyBinder();

    public class MyBinder extends Binder {
        public SuSuperService getService() {
            return SuSuperService.this;
        }
    }


    private HashMap<String, FaceCommCallBack> map;
    protected void setActionReceivers(String actions[], FaceCommCallBack faceCommCallBacks[]) {
        if(actions==null||actions.length==0||faceCommCallBacks==null||faceCommCallBacks.length==0)return;
        if (map == null) map = new HashMap<String, FaceCommCallBack>();
        for (int i = 0; i < actions.length; i++) {
            map.put(actions[i], faceCommCallBacks[i]);
        }
        setListeners();
    }


    @FaceEventViewINforDlp
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    @FaceEventViewINforDlp
    private void setListeners() {
        if (map == null || map.size() == 0) return;
        // 网络变化
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    FaceCommCallBack mFaceCommCallBack = map.get(intent.getAction());
                    if (mFaceCommCallBack != null) mFaceCommCallBack.callBack(intent);
                }
            };
            mIntentFilter = new IntentFilter();
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String action = it.next();
                mIntentFilter.addAction(action);
            }
        }

        this.registerReceiver(this.mReceiver, mIntentFilter);
    }


    private void setDisListeners() {
        if (mReceiver == null) return;
        this.unregisterReceiver(this.mReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Su.log(this+" onCreate");
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Su.log(this+" bindService");
        return super.bindService(service, conn, flags);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setDisListeners();
        Su.log(this+" onDestry");
    }
}
