package com.hxuehh.reuse_Process_Imp.app.OSinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;

import java.util.ArrayList;
import java.util.List;


//网络环境
public class SuNetEvn {

    private Context mContext;
    // 是不是有网
    private boolean hasNet;

    public boolean isHasNet() {
        return hasNet;
    }

    private boolean isWifi;

    public static final String netAction = "android.net.conn.CONNECTIVITY_CHANGE";

    private static SuNetEvn feedBack;

    public static SuNetEvn getInstance() {
        if (feedBack == null) feedBack = new SuNetEvn(SuApplication.getInstance());
        return feedBack;
    }

    private SuNetEvn(Context mContext) {
        this.mContext = mContext;
        hasNet = NetStatusUtil.isNetWorkEnable();
        netType=NetStatusUtil.getNetType();
        setNetLinstener();
    }


    private void UnsetNetLinstener() {
        // 去掉网络监听
        mContext.unregisterReceiver(this.mConnectivityChangedReceiver);
        mConnectivityChangedReceiver = null;
    }

    private void setNetLinstener() {
        mContext.registerReceiver(this.mConnectivityChangedReceiver,
                new IntentFilter(netAction));
    }


    public int netType = -1;
    public int subType = -1;
    // 网络变化
    private BroadcastReceiver mConnectivityChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            hasNet = (networkInfo != null) && (networkInfo.isConnected());
            Su.log("接收网络监听  hasNet  " + hasNet);


            if (hasNet) {
                netType = networkInfo.getType();
                subType = networkInfo.getSubtype();
                if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // switch(subType){
                    // case TelephonyManager.NETWORK_TYPE_1xRTT:
                    // return NET_2G; // ~ 50-100 kbps
                    // case TelephonyManager.NETWORK_TYPE_CDMA:
                    // return NET_2G; // ~ 14-64 kbps
                    // case TelephonyManager.NETWORK_TYPE_EDGE:
                    // return NET_2G; // ~ 50-100 kbps
                    // case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    // return NET_3G; // ~ 400-1000 kbps
                    // case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    // return NET_3G; // ~ 600-1400 kbps
                    // case TelephonyManager.NETWORK_TYPE_GPRS:
                    // return NET_2G; // ~ 100 kbps
                    // case TelephonyManager.NETWORK_TYPE_HSDPA:
                    // return NET_3G; // ~ 2-14 Mbps
                    // case TelephonyManager.NETWORK_TYPE_HSPA:
                    // return NET_3G; // ~ 700-1700 kbps
                    // case TelephonyManager.NETWORK_TYPE_HSUPA:
                    // return NET_3G; // ~ 1-23 Mbps
                    // case TelephonyManager.NETWORK_TYPE_UMTS:
                    // return NET_3G; // ~ 400-7000 kbps
                    // NOT AVAILABLE YET IN API LEVEL 7
                    // case Connectivity.NETWORK_TYPE_EHRPD:
                    // return NET_3G; // ~ 1-2 Mbps
                    // case Connectivity.NETWORK_TYPE_EVDO_B:
                    // return NET_3G; // ~ 5 Mbps
                    // case Connectivity.NETWORK_TYPE_HSPAP:
                    // return NET_3G; // ~ 10-20 Mbps
                    // case Connectivity.NETWORK_TYPE_IDEN:
                    // return NET_2G; // ~25 kbps
                    // case Connectivity.NETWORK_TYPE_LTE:
                    // return NET_3G; // ~ 10+ Mbps
                    // Unknown
                    // case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    // return NET_2G;
                    // default:
                    // return NET_2G;
                    // }
                    isWifi = false;
                } else if (netType == ConnectivityManager.TYPE_WIFI) {
                    isWifi = true;
                }

            } else {
                netType = -1;
            }

            for (FaceCommCallBack mmFaceCommCall : mFaceCommCallBackList) {
                mmFaceCommCall.callBack();
            }

        }

    };


    private List<FaceCommCallBack> mFaceCommCallBackList = new ArrayList<FaceCommCallBack>();

    public void addNetChangeListener(FaceCommCallBack mBackGMessageListener) {
        mFaceCommCallBackList.add(mBackGMessageListener);
    }

    public void removeNetChangeListener(FaceCommCallBack faceCommCallBackForReLoad) {
        mFaceCommCallBackList.remove(faceCommCallBackForReLoad);
    }


}
