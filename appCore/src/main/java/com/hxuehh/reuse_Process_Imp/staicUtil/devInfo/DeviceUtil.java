package com.hxuehh.reuse_Process_Imp.staicUtil.devInfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.LogUtil;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.ProsRunTime;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class DeviceUtil {

    private static String mac;
    private static String imsi;
    private static String imei;
    private static String brand;

    public static String getimei() {
        if (!StringUtil.isNull(imei)) return imei;

        String ret = "";
        if (AppStaticSetting.DEV_TEST_SWITCH == 1&& AppStaticSetting.LOG_CLOSED==0) {
            ret = SharedPreferencesUtils.getString("testDeciceID");
            if (!StringUtil.isEmpty(ret)) {
                imei = ret;
                return ret;
            }
        }

        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) SuApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            ret = telephonyManager.getDeviceId();
            if (!StringUtil.isNull(ret)) imei = ret;
        } catch (Exception e) {
            LogUtil.w(e);
        }

        return ret;
    }

    public static String getImsi() {
        if (!StringUtil.isNull(imsi)) return imsi;

        String ret = "";
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) SuApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            ret = telephonyManager.getSubscriberId();
            if (!StringUtil.isNull(ret)) imsi = ret;
        } catch (Exception e) {
            LogUtil.w(e);
        }

        return ret;
    }

    public static String getMacAddress() {
        if (!StringUtil.isNull(mac)) return mac;

        String ret = "";
        try {
            WifiManager wifiManager = (WifiManager) SuApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
            ret = wifiManager.getConnectionInfo().getMacAddress();
            if (!StringUtil.isNull(ret)) mac = ret;
        } catch (Exception e) {
            LogUtil.w(e);
        }

        return ret;
    }


    public static String getBrand() {
        return Build.BRAND;
    }


    public static int getSDKINT() {
        return Build.VERSION.SDK_INT ;
    }


    public static String getCPU_ABI() {
        return Build.CPU_ABI ;
    }

    public static String getModel() {
        return Build.MODEL;
    }




    /**
     * mac + device id
     * 前面12位为mac地址，后15位为device id
     *
     * @return
     */
    public static String getUID() {
        String deviceId = getimei();
        String mac = getMacAddress();
        if (!StringUtil.isNull(mac)) {
            return mac.replace(":", "") + deviceId;
        } else {
            return "";
        }
    }

    /*
    判断是不是MIUI系统
     */
    private static int iMIUI = -1; //-1 未初始化 0 不是MIUI 1 是MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    public static boolean isMIUI() {
        try {
            if(-1 == iMIUI){
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));

                if(properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                        || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                        || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null){
                    iMIUI = 1;
                }else{
                    iMIUI = 0;
                }
            }
        } catch (final IOException e) {
        }

        return iMIUI == 1;
    }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        if (context == null) {
            return "";
        }

        String localMac = null;
        if (NetStatusUtil.isWifiAvailable()) {
            localMac = getWifiMacAddress(context);
        }

        if (localMac != null && localMac.length() > 0) {
            localMac = localMac.replace(":", "-").toLowerCase();
            return localMac;
        }

        localMac = getMacFromCallCmd();
        if (localMac != null) {
            localMac = localMac.replace(":", "-").toLowerCase();
        }

        return localMac;
    }

    private static String getWifiMacAddress(Context context) {
        String localMac = null;
        try {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (wifi.isWifiEnabled()) {
                localMac = info.getMacAddress();
                if (localMac != null) {
                    localMac = localMac.replace(":", "-").toLowerCase();
                    return localMac;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private static String getMacFromCallCmd() {
        String result = "";
        result = ProsRunTime.callCmd("busybox ifconfig", "HWaddr");

        if (result == null || result.length() <= 0) {
            return null;
        }

        // DebugLog.v("tag", "cmd result : " + result);

        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            String Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.replaceAll(" ", "");
            }
        }

        return result;
    }

    public static String getWifiRssi() {
        int asu = 85;
        try {
            final NetworkInfo network = ((ConnectivityManager)  SuApplication.getInstance().getBaseContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager)  SuApplication.getInstance().getBaseContext()
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        asu = wifiInfo.getRssi();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asu + "dBm";
    }

    public static String getWifiSsid() {
        String ssid = "";
        try {
            final NetworkInfo network = ((ConnectivityManager) SuApplication.getInstance().getBaseContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager)  SuApplication.getInstance().getBaseContext()
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        ssid = wifiInfo.getSSID();
                        if (ssid == null) {
                            ssid = "";
                        }
                        ssid = ssid.replaceAll("\"", "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssid;
    }


    /**
     * 检查sim卡状态
     *
     * @return
     */
    public static boolean checkSimState() {
        TelephonyManager tm = (TelephonyManager) SuApplication.getInstance().getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT
                || tm.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN) {
            return false;
        }

        return true;
    }


    public static String getWifiIP() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager)SuApplication.getInstance(). getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
