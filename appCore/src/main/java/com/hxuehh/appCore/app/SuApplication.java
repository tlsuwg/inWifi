package com.hxuehh.appCore.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.hxuehh.appCore.aidl.AidlServiceSetGetClass_CheckMain;
import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.aidl.IMyServiceSetGetClass;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.DevShowErrAc;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.AnalysisType_C;
import com.hxuehh.appCore.faceFramework.faceProcess.statistics.SetAnalysisType_C;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class SuApplication extends android.app.Application implements SetAnalysisType_C {

    protected static SuApplication instance;

    public static SuApplication getInstance() {
        return instance;
    }

    private int myPid = -1;
    private List<Activity> mActivityList = new ArrayList<Activity>();
    private IMyServiceSetGetClass myService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (DevRunningTime.lastSuApplication) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.instance = this;
        myPid = android.os.Process.myPid();
        mActivityList.clear();
        setErrHandler();
        Su.logApp("app启动onCreate " + this.getCurProcessName() + " " + this.hashCode() + " " + DateUtil.getCurrentTime());
        bindAidlCache();//都需要和服务链接
        doBusyTransaction();
        ThreadManager.getInstance().submitBackThread(new Runnable() {
            @Override
            public void run() {
                doBackTransaction();
            }
        });
    }

    private Thread.UncaughtExceptionHandler mDefaultHandler;


    private void setErrHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                ex.printStackTrace();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String info = getErrInfo(ex);
                        Intent in = new Intent(getInstance(), DevShowErrAc.class);
                        in.putExtra(AidlCacheKeys.Provisionality + "", info);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getInstance().startActivity(in);
                    }
                }).start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mDefaultHandler.uncaughtException(thread,ex);
            }
        });
    }



    private String getErrInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        return result;
    }


    private ServiceConnection serviceConnectionAidl = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = IMyServiceSetGetClass.Stub.asInterface(service);
            sendBroadcast(new Intent(AidlServiceSetGetClass_CheckMain.MyServiceSetGetClassStartOK));
            Su.log("启动Aidl完成");
            Su.logApp("serviceConnectionAidl onServiceConnected " + this.hashCode() + " " + DateUtil.getCurrentTime());
            checkService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            Su.log("onServiceDisconnected  onServiceDisconnected");
            myService = null;
            bindAidlCache();
        }
    };


    public void bindAidlCache() {
        Su.log("启动Aidl");
        bindService(new Intent(this, AidlServiceSetGetClass_CheckMain.class),
                serviceConnectionAidl, Context.BIND_AUTO_CREATE);
    }

    public abstract void checkService();

    public abstract void doBusyTransaction();

    public abstract void doBackTransaction();


    public String getTruePackageName() {
        return super.getPackageName();
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }


    private int notmain = 0;

    public boolean isMainAppPro() {
        if (notmain == 1) return false;
        String info = getCurProcessName();
        boolean ismain = (!StringUtil.isEmpty(info) && info.equals(getTruePackageName()));
        if (!ismain) {
            notmain = 1;
        }
        return ismain;
    }


    private boolean isLoadRunningAppProcessInfo;

    String currentProcessName;
    public String getCurProcessName() {
        if(!StringUtil.isEmpty(currentProcessName))return currentProcessName;
        try {
            int pid = android.os.Process.myPid();
            Object oo = getSystemService(Context.ACTIVITY_SERVICE);
            if (oo == null) return null;
            ActivityManager mActivityManager = (ActivityManager) oo;
            if (oo == null) return null;
            if (isLoadRunningAppProcessInfo) return null;
            isLoadRunningAppProcessInfo = true;
            List<ActivityManager.RunningAppProcessInfo> list = mActivityManager.getRunningAppProcesses();
            isLoadRunningAppProcessInfo = false;
            if (list == null) return null;
            for (ActivityManager.RunningAppProcessInfo appProcess : list) {
                if (appProcess.pid == pid) {
                    return currentProcessName=appProcess.processName;
                }
            }
        } catch (Exception e) {
//            LogUtil.w(e);
        }
        return null;
    }

    private Object oo;//临时存储

    public synchronized Object getOo() {
        return oo;
    }

    public synchronized void setOo(Object oo) {
        this.oo = oo;
    }

    public int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getTruePackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getTruePackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public boolean isNewVison() {
        int thisVison = getVersionCode();
        int dbVison = SharedPreferencesUtils.getInteger("current_app_vison");
        return thisVison != dbVison;
    }


    public void exit() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivityList.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }


    volatile AnalysisType_C mAnalysisTypeC;

    @Override
    public boolean setAnalysisType_C(AnalysisType_C mAnalysisType) {
        Su.log("mAnalysisTypeC");
        this.mAnalysisTypeC = mAnalysisType;
        return false;
    }

    @Override
    public synchronized AnalysisType_C getAnalyticsType_C() {
        return mAnalysisTypeC;
    }

    public synchronized String getSourceType_C() {
        if (mAnalysisTypeC == null) {
            Su.logE("出错 app mAnalysisTypeC==null");
            return null;
        }
        return mAnalysisTypeC.typeName;
    }

    public boolean isRunningForeground() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName componentName = mActivityManager.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = componentName.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getTruePackageName())) {
            return true;
        }
        return false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Su.logApp("app启动  onConfigurationChanged" + newConfig);
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Su.logApp("app启动 onLowMemory ");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Su.logApp("app启动 onTerminate ");
    }

    protected Map<Integer, Service> srvicesMap = new LinkedHashMap();
    private Map<Integer, ServiceConnection> serviceConnectionsMap = new LinkedHashMap();

    public void bindSuService(Intent in, final int flag) {
        ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder bb) {
                srvicesMap.put(flag, ((SuSuperService.MyBinder) bb).getService());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                srvicesMap.remove(flag);
            }
        };
        serviceConnectionsMap.put(flag, mServiceConnection);
        bindService(in, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(int flag) {
        if (serviceConnectionsMap.containsKey(flag)) {
            unbindService(serviceConnectionsMap.get(flag));
        }
    }


    //aidl============================================
    private void checkAildService() {
        if (myService == null) throw new RuntimeException("aidl Service null");
    }

    public BytesClassAidl getAidlValue(int key) throws RemoteException {
        checkAildService();
        return myService.getValue(key);
    }

    public BytesClassAidl putAidlValue(BytesClassAidl mClassKeyByte) throws RemoteException {
        checkAildService();
        return myService.putValue(mClassKeyByte);
    }

    public BytesClassAidl getReMoveAidlValue(int key, int meOrFile) throws RemoteException {
        checkAildService();
        return myService.getReMoveValue(key, meOrFile);
    }

    public int getAidlMemoryCacheSize() throws RemoteException {
        checkAildService();
        return myService.getMemoryCacheSize();
    }


    FaceBaseActivity_1 mCurrentActivity;

    public FaceBaseActivity_1 getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(FaceBaseActivity_1 mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    //    ==================================

    public long getOSFreeMemory() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public long getOSAllMemory() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.totalMem;
    }


}

