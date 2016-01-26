package com.hxuehh.rebirth.device.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.device.DeviceCapacityBuilder;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.util.Date;


/**
 * Created by suwg on 2015/8/14.
 */
public class DeviceService_TCPLongLink_ extends AbService_TCPLongLink_ implements IsOnTop {


    public static final String TCPLinkMainErr = "com.su.AbService_TCPLongLink_.TCPLinkMainErr." + getServiceNameStatic();
    public static final String TCPListenErr = "com.su.AbService_TCPLongLink_.TCPListenErr." + getServiceNameStatic();
    public static final String TCP_Succeed = "com.su.AbService_TCPLongLink_.TCP_Succeed." + getServiceNameStatic();
    public static final String TCP_heartbeat = "com.su.AbService_TCPLongLink_.TCP_heartbeat." + getServiceNameStatic();
    public static final String TCPLinkReLinked = "com.su.AbService_TCPLongLink_.TCPLinkReLinkedOK." + getServiceNameStatic();

    static String getServiceNameStatic() {
        return "device";
    }

    public int getServiceType() {
        return AbService_TCPLongLink_.AbService_TCPLongLink_Device;
    }

    protected String getTCPListenErr() {
        return TCPListenErr;
    }

    protected String getTCP_heartbeat() {
        return TCP_heartbeat;
    }

    protected String getTCPLinkMainErr() {
        return TCPLinkMainErr;
    }

    protected String getTCP_Succeed() {
        return TCP_Succeed;
    }

    @Override
    protected String getTCP_ReLinkedOk() {
        return TCPLinkReLinked;
    }


    protected void onAllStartOk() {
        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.DeviceService_TCPLongLink__ISRunning, BytesClassAidl.To_Me, true));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (DevRunningTime.isSendHeartbeat) {
            setAlarm();
        }
        sendtest();

    }

    public MidMessageHandler getmMidMessageHandler() {
        return new MidMessageHandler_Device(null, this);
    }

    protected long getHeartLong() {
        return UDPTCPkeys.HeartLong_Device;
    }

    protected void onServiceDestryedGetAlarm() {
        noSetAlarm();
        return;
    }

    @Override
    public IBinder onBind(Intent intent) {
        DeviceCapacityBuilder.getInstance().findHardware_SDK(null);
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        DeviceCapacityBuilder.getInstance().onDestroy();

        super.onDestroy();
        try {
            SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.DeviceService_TCPLongLink__ISRunning, BytesClassAidl.To_Me);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long getGetTCPTimeOutErrTime() {
        return UDPTCPkeys.TimeOutLong_Device;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    void setAlarm() {
        Intent intent = new Intent(getTCP_heartbeat());
        PendingIntent ac = PendingIntent.getService(this, 1024, intent, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime() + 1000 * 1, getHeartLong(), ac);
    }


    void noSetAlarm() {
        Intent intent = new Intent(getTCP_heartbeat());
        PendingIntent ac = PendingIntent.getService(this, 1024, intent, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(ac);
    }

    @Override
    protected void onLinkErr() {

    }
}
