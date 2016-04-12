package com.hxuehh.reuse_Process_Imp.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;


import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.appCore.develop.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-12-8
 * Time: 下午7:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAnalyticsInfo implements IAnalyticsInfo {

    protected static final String separator = "|";

    protected String deviceId;
    protected Context context;

    public AbstractAnalyticsInfo(Context context) {
        this.context = context;
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = DeviceUtil.getMacAddress();
        }
    }

    public String getLogHeader() {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();

        // requestKey never use
        sb.append("");
        sb.append(separator);
        // time
        // sb.append(System.currentTimeMillis() / 1000);
        sb.append(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));

        sb.append(separator);
        // device id
        sb.append(deviceId);
        sb.append(separator);
        // net type
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            if ("wifi".equalsIgnoreCase(typeName)) {
                sb.append(typeName);
            } else {
                sb.append(typeName);
                sb.append("-");
                sb.append(info.getSubtypeName());
                sb.append("-");
                sb.append(info.getExtraInfo());
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }
        sb.append(separator);
        // app version
        sb.append(SuApplication.getInstance().getVersionName());
        sb.append(separator);
        // system version
        sb.append(Build.VERSION.RELEASE);
        sb.append(separator);
        // user id
        sb.append(getUserId());
        sb.append(separator);
        // longitude
        sb.append(getLongitude());
        sb.append(separator);
        // latitude
        sb.append(getLatitude());
        sb.append(separator);
        // client tag
        sb.append(AppConfig.getInstance().CLIENT_TAG);
        sb.append(separator);
        // partner id
        sb.append(AppConfig.PARTNER_ID);
        sb.append(separator);
        // city id
        sb.append(getCityId());
        sb.append(separator);
        // operator
        sb.append(tm.getNetworkOperator());
        sb.append(separator);
        // phone number
        String phone = getPhoneNum();
        if (null != phone)
            sb.append(phone);
        sb.append(separator);
        // mac address
        sb.append(DeviceUtil.getMacAddress());
        sb.append(separator);
        // 系统类型
        sb.append("android");
        sb.append(separator);
        // 手机型号
        sb.append(Build.MODEL);
        sb.append(separator);
        // 分辨率
        try {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            sb.append(dm.widthPixels);
            sb.append("x");
            sb.append(dm.heightPixels);
        } catch (Exception e) {
            LogUtil.w(e);
        }

        return sb.toString();
    }

    protected abstract String getLongitude();

    protected abstract String getLatitude();

    /**
     * Get TuanDD user id
     * @return
     */
    protected abstract String getUserId();

    /**
     * Get TuanDD city id
     * @return
     */
    protected abstract String getCityId();

    protected abstract String getPhoneNum();
}
