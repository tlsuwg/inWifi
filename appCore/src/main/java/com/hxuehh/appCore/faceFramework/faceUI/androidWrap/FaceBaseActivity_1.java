package com.hxuehh.appCore.faceFramework.faceUI.androidWrap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ViewKeyable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.AnalysisType_C;
import com.hxuehh.appCore.faceFramework.faceProcess.statistics.SetAnalysisType_C;
import com.hxuehh.reuse_Process_Imp.analytics.Analytics;
import com.hxuehh.appCore.develop.DevAppRun;
import com.hxuehh.appCore.develop.FaceEventViewINforDlp;
import com.hxuehh.appCore.develop.FaceTestforDlp;

import java.util.Iterator;
import java.util.HashMap;


/**
 * Created by suwg on 2014/5/20.
 * <p/>
 * 所有类别的超类 （所有类别  所以就全部都是 FragmentActivity了  开销巨大啊）
 */

public abstract class FaceBaseActivity_1 extends FragmentActivity implements HandlerWrap, IsOnTop, ViewKeyable, FaceContextWrap,
        SetAnalysisType_C {

    public static final String From = "from";

    protected int viewKey;

    public abstract int getViewKey();

    public void setViewKey(int key) {
        viewKey = key;
    }

    protected String mPageId = "";

    public String getPageId() {
        return mPageId;
    }

    public void setPageId(String pageId) {
        mPageId = pageId;
    }

    protected String mPageName = "";

    public String getPageName() {
        return mPageName;
    }

    public void setPageName(String pageName) {
        mPageName = pageName;
    }

    protected boolean isOnTop;//是不是在前台界面关系到是不是需要回调 尤其是handler

    public boolean isOnTop() {
        return isOnTop;
    }

    public void setOnTop(boolean isOnTop) {
        this.isOnTop = isOnTop;
        if (isOnTop) {
            setmReceiverListeners();
        } else {
            setDismReceiverListeners();
        }
    }


    private Handler mHandler;

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper());
        }
        return mHandler;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }


    private boolean enableAutoAnalysis = true;

    public void setEnableAutoAnalysis(boolean autoAnalysis) {
        enableAutoAnalysis = autoAnalysis;
    }


    public int mFrom;

    FaceBaseActivity_1 thisCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisCon = this;
        getDevAppRun();
        SuApplication.getInstance().setCurrentActivity(this);
        super.onCreate(savedInstanceState);

        Intent in = getIntent();
        if (in != null) {
            mFrom = in.getIntExtra(FaceBaseActivity_1.From, -1);
        }

        setViewKey(this.getViewKey());
        setHandler(new Handler());
        SuApplication.getInstance().addActivity(this);
//        initView();
//        addListeners();
    }


    @FaceTestforDlp
    private DevAppRun mDevAppRun;

    public DevAppRun getDevAppRun() {
        if (mDevAppRun == null) mDevAppRun = new DevAppRun(this);
        return mDevAppRun;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuApplication.getInstance().removeActivity(this);

    }

    @Override
    protected void onResume() {
        getDevAppRun().onResumeStart();
        super.onResume();
        if (enableAutoAnalysis) {
            Analytics.onResume(this);
        }
        this.setAnalysisType_C(getAnalyticsType_C());
        setOnTop(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (enableAutoAnalysis) {
            Analytics.onPause(this);
        }
        setOnTop(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        setOnTop(true);
    }

    public void initView() {
    }

    public void addListeners() {

    }


    public boolean setAnalysisType_C(AnalysisType_C mAnalysisType) {
        return SuApplication.getInstance().setAnalysisType_C(mAnalysisType);
    }

    public AnalysisType_C getAnalyticsType_C() {
        return new AnalysisType_C(this.getViewKey());
    }


    private HashMap<String, FaceCommCallBack> map;

    protected void setActionReceivers(String actions[], FaceCommCallBack faceCommCallBacks[]) {
        if (actions == null || actions.length == 0 || faceCommCallBacks == null || faceCommCallBacks.length == 0)
            return;
        if (map == null) map = new HashMap<String, FaceCommCallBack>();
        for (int i = 0; i < actions.length; i++) {
            map.put(actions[i], faceCommCallBacks[i]);
        }
    }


    @FaceEventViewINforDlp
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    @FaceEventViewINforDlp
    private void setmReceiverListeners() {
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


    private void setDismReceiverListeners() {
        if (mReceiver == null) return;
        this.unregisterReceiver(this.mReceiver);
    }


    @Override
    public FaceBaseActivity_1 getFaceContext() {
        return thisCon;
    }


}
