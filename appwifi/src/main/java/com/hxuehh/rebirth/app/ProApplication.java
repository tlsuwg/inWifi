package com.hxuehh.rebirth.app;

import android.app.Service;
import android.os.RemoteException;
import android.util.Log;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.image.image13.Image13lLoader;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.ScreenUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.DB.beans.DataPollingCache;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.DB.beans.Image;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.DB.beans.KeyValue;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;


public class ProApplication extends SuApplication {

    public static ProApplication getInstance() {
        return (ProApplication) instance;
    }

    public AppConfig loadMainAppConfig() {

        Su.log("获取配置");
        BytesClassAidl mClassKeyByte = null;
        try {
            mClassKeyByte = getAidlValue(AidlCacheKeys.AppConfig);
            if (mClassKeyByte != null) {
                AppConfig mAppConfig = (AppConfig) mClassKeyByte.getTrue();
                AppConfig.setInstance(mAppConfig);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConfig.getInstance();//如果没有，自己建造吧
    }


    @Override
    public void doBackTransaction() {
        LogUtil.w("Application doBackTransaction ");
        clearExpiredCache(2);

    }

    public static void clearExpiredCache(int cacheDay) {
        Image.getInstance().removeExpired(cacheDay);
        KeyValue.getInstance().removeExpired(cacheDay);
        DataPollingCache.getInstance().removeExpired(cacheDay);
    }

    @Override
    public void onLowMemory() {
        Image13lLoader.getInstance().flushCache();
        super.onLowMemory();
    }

    @Override
    public void checkService() {
        if (isMainAppPro()) {
            setMainAppConfig();
            initMiPUSH();
        } else {
//            loadMainAppConfig();
        }
    }


    public void setMainAppConfig() {//拉取接口配置之后还需要再去拉取一次

        Su.log("设置配置");
        AppConfig mAppConfig = AppConfig.getInstance();
        try {
            putAidlValue(new BytesClassAidl(AidlCacheKeys.AppConfig, BytesClassAidl.To_Me, mAppConfig));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void doBusyTransaction() {

        if (isMainAppPro()) {
            AppConfig mAppConfig = AppConfig.getInstance();
            ScreenUtil.setContextDisplay(this);
            LogUtil.w("Application doBusyTransaction 2");
            if (AppConfig.getInstance().LOG_ERR_FEED)
//                ForceCloseHandler.getInstance().init(instance);
                if (isNewVison()) {

                }
        } else {

        }
    }



    //    =====================
    private static long keysforMi = 5841739458920l;
    // user your appid the key.
    public static final String APP_ID = "2882303761517394925";
    // user your appid the key.
    public static final String APP_KEY = (keysforMi + 5) + "";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo

    private void initMiPUSH() {
        Su.logPush("initMiPUSH ");
        MiPushClient.registerPush(this, APP_ID, APP_KEY);
        //打开Log
//        LoggerInterface newLogger = new LoggerInterface() {
//
//            @Override
//            public void setTag(String tag) {
//                //
//
//            }
//
//            @Override
//            public void log(String content, Throwable t) {
////                Log.d(TAG, content, t);
//                Su.logPush(content + " " + t.fillInStackTrace());
//            }
//
//            @Override
//            public void log(String content) {
////                Log.d(TAG, content);
//                Su.logPush(content);
//            }
//        };
//        Logger.setLogger(this, newLogger);
    }

}
