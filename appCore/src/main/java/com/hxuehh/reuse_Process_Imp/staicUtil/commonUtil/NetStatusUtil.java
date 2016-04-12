package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.app.SuApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by yubaojian on 15-5-15.
 */
public class NetStatusUtil {

    public static boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SuApplication.getInstance().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
//                TaoCCUtil.showTaoToast(context, "无法连接网络");
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }


    public static int getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SuApplication.getInstance().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
//                TaoCCUtil.showTaoToast(context, "无法连接网络");
            return -1;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) return networkInfo
                .getType();
        return -1;

    }


    public static boolean isNetWorkEnable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) SuApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
//                TaoCCUtil.showTaoToast(context, "无法连接网络");
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        TaoCCUtil.showTaoToast(context, "无法连接网络");
        return false;
    }


    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    private static final int NETWORK_CLASS_4_G = 3;

    private static DecimalFormat df = new DecimalFormat("#.##");

    // 适配低版本手机
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    /**
     * 格式化大小
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit;
    }

    public static String formatSizeBySecond(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit + "/s";
    }

    public static String format(long size) {
        String unit = "B";
        float len = size;
        if (len > 1000) {
            len /= 1024f;
            unit = "KB";
            if (len > 1000) {
                len /= 1024f;
                unit = "MB";
                if (len > 1000) {
                    len /= 1024f;
                    unit = "GB";
                }
            }
        }
        return df.format(len) + "\n" + unit + "/s";
    }

    /**
     * 获取运营商
     *
     * @return
     */
    public static String getProvider() {
        String provider = "未知";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) SuApplication.getInstance().getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            Log.v("tag", "getProvider.IMSI:" + IMSI);
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                        .getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    Log.v("tag", "getProvider.operator:" + operator);
                    if (operator != null) {
                        if (operator.equals("46000")
                                || operator.equals("46002")
                                || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getCurrentNetworkType() {
        int networkClass = getNetworkClass();
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "无";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi-Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "未知";
                break;
        }
        return type;
    }

    public static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    public static int getNetworkClass() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) SuApplication.getInstance().getBaseContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) SuApplication.getInstance().getBaseContext().getSystemService(
                            Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    public static void openNetSetting(Context context) {
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本C
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    public static List<ScanResult> getAllWifiAps() throws Exception {
        WifiManager wifiManager = (WifiManager) SuApplication.getInstance().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) throw new Exception("没有可以连接的wifi管理器");
        wifiManager.startScan();

        return wifiManager.getScanResults();
    }

    public static WifiInfo getWifiInfo() {
        WifiManager wifiManager = (WifiManager) SuApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }



    private static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    public  static String getWifiIp() throws Exception {
        WifiInfo wifiInfo = getWifiInfo();
        if(wifiInfo==null)throw new Exception("没有获取wifi连机器");
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }


    public static String getLocalIpAddress()
    {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }




    static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;

    static int getSecurityWay(WifiConfiguration config) {
        if(config==null)return -1;
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }


    public static String getSecurityWay(String ssid) {
        try {
            List<ScanResult> list=getAllWifiAps();
            if(list!=null){
                for(ScanResult mScanResult:list){
                    if(mScanResult.SSID.equals(ssid)){
                        return mScanResult.capabilities;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   /* public static CellInfo getNetInfo() {
        CellInfo info = new CellInfo();
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) ConfigManager
                    .getFaceContext().getSystemService(Context.TELEPHONY_SERVICE);
            String operator = mTelephonyManager.getNetworkOperator();
            if (operator != null) {
                *//** 通过operator获取 MCC 和MNC *//*
                if (operator.length() > 3) {
                    String mcc = operator.substring(0, 3);
                    String mnc = operator.substring(3);
                    info.setMcc(mcc);
                    info.setMnc(mnc);
                }
            }

            int lac = 0;
            int cellId = 0;
            int phoneType = mTelephonyManager.getPhoneType();
            if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation location = (GsmCellLocation) mTelephonyManager
                        .getCellLocation();
                *//** 通过GsmCellLocation获取中国移动和联通 LAC 和cellID *//*
                lac = location.getLac();
                cellId = location.getCid();
            } else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation location = (CdmaCellLocation) mTelephonyManager
                        .getCellLocation();
                lac = location.getNetworkId();
                cellId = location.getBaseStationId();
                cellId /= 16;
            }
            if (lac == 0 || cellId == 0) {
                List<NeighboringCellInfo> infos = mTelephonyManager
                        .getNeighboringCellInfo();
                int lc = 0;
                int ci = 0;
                int rssi = 0;
                for (NeighboringCellInfo cell : infos) {
                    // 根据邻区总数进行循环
                    if (lc == 0 || ci == 0) {
                        lc = cell.getLac();
                        ci = cell.getCid();
                        rssi = cell.getRssi();
                    }
                    // sb.append(" LAC : " + info.getLac());
                    // // 取出当前邻区的LAC
                    // sb.append(" CID : " + info.getCid());
                    // // 取出当前邻区的CID
                    // sb.append(" BSSS : " + (-113 + 2 * info.getRssi()) +
                    // "\n"); // 获取邻区基站信号强度
                }
                rssi = -113 + 2 * rssi;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
*/


}
