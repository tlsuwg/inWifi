//package com.hxuehh.appCore.app;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.RemoteException;
//
//import com.hxuehh.appCore.aidl.BytesClass;
//import com.hxuehh.appCore.develop.DevRunningTime;
//import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
//
///**
// * Created by suwg on 2015/8/13.
// */
//public class IntentChangeManger<T> {
//
//    public IntentChangeManger getIntentChangeMangerProxy() {
//        return mIntentChangeMangerProxy;
//    }
//
//    public void setIntentChangeMangerProxy(IntentChangeManger mIntentChangeMangerProxy) {
//        this.mIntentChangeMangerProxy = mIntentChangeMangerProxy;
//    }
//
//  private IntentChangeManger mIntentChangeMangerProxy;
//
//
//    public void jumpTo(Activity thisCon, int flag, boolean b) {
//        mIntentChangeMangerProxy.jumpTo(thisCon, flag, b);
//    }
//
//
//    public void jumpToForResult(Activity thisCon, int flag, int req) {
//        mIntentChangeMangerProxy.jumpToForResult(thisCon, flag, req);
//    }
//
//    public void startService(Context thisCon, int flag) {
//        mIntentChangeMangerProxy.startService(thisCon, flag);
//    }
//
//    public void stopService(Context thisCon, int flag) {
//        mIntentChangeMangerProxy.stopService(thisCon, flag);
//    }
//
//
//    public void bindServiceToApp(Context thisCon, int flag, BytesClass mClassKeyByte) {
//
//        mIntentChangeMangerProxy.bindServiceToApp(thisCon, flag, mClassKeyByte);
//
//    }
//
//
//    public void unbindServiceToApp(int flag) {
//        mIntentChangeMangerProxy.unbindServiceToApp(flag);
//    }
//
//
////    ===============================================================================
//
//    public boolean isDevServiceStart() {
//        try {
//            return SuApplication.getInstance().getAidlValue(AidlCacheKeys.DeviceService_TCPLongLink__ISRunning) != null;
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    public boolean isClientServiceStart() {
//        try {
//            return SuApplication.getInstance().getAidlValue(AidlCacheKeys.Client_Service_UDPreviceback_TCPLongLink) != null;
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    public boolean isMainServiceStart() {
//        try {
//            return SuApplication.getInstance().getAidlValue(AidlCacheKeys.MainService_UDPreviceback_TCPLongLink_ISRunning) != null;
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean isClient_Dev_Main() {
//        return isDevServiceStart() && isMainServiceStart() && DevRunningTime.isCanDevServerClientInThis;
//    }
//}
