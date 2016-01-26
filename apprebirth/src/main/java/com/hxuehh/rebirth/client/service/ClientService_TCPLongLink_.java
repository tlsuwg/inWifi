package com.hxuehh.rebirth.client.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.client.faceAc.Client_InitAc;
import com.hxuehh.rebirth.client.service.mappingDeviceInfo.MappingDeviceInfoManager;
import com.hxuehh.rebirth.client.service.sensor.DeviceAccelerationSensor;
import com.hxuehh.rebirth.client.service.sensor.FaceCommCallBackForClient;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.ApiUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.util.Date;

/**
 * Created by suwg on 2015/8/14.
 */
public class ClientService_TCPLongLink_ extends AbService_TCPLongLink_ implements IsOnTop {


    public static final String TCPLinkMainErr = "com.su.AbService_TCPLongLink_.TCPLinkMainErr." + getServiceNameStatic();
    public static final String TCPListenErr = "com.su.AbService_TCPLongLink_.TCPListenErr." + getServiceNameStatic();
    public static final String TCP_Succeed = "com.su.AbService_TCPLongLink_.TCP_Succeed." + getServiceNameStatic();
    public static final String TCP_heartbeat = "com.su.AbService_TCPLongLink_.TCP_heartbeat." + getServiceNameStatic();
    public static final String TCPLinkReLinkedOK = "com.su.AbService_TCPLongLink_.TCPLinkReLinkedOK." + getServiceNameStatic();

    public static final String AccelerationSetting  = "com.su.AbService_TCPLongLink_.AccelerationSetting.setting";



    static String getServiceNameStatic() {
        return "client";
    }

    public int getServiceType() {
        return AbService_TCPLongLink_.AbService_TCPLongLink_Client;
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

    protected String getTCP_ReLinkedOk() {
        return TCPLinkReLinkedOK;
    }

    public MidMessageHandler getmMidMessageHandler() {
        return new MidMessageHandler_Client(null, this);
    }



    protected void onAllStartOk() {
        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.Client_Service_UDPreviceback_TCPLongLink, BytesClassAidl.To_Me, true));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (DevRunningTime.isSendHeartbeat) {
            setAlarm();
        }
        sendtest();
        addAccelerationSetting();
        addAccelerationSensor(true);

        MappingDeviceInfoManager.getInstance();//映射区域
    }



    private void addAccelerationSetting() {
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String info=intent.getStringExtra("open");
                if("0".equals(info)){
                    disAccelerationSensor();
                }else if ("1".equals(info)){
                    addAccelerationSensor(false);
                }
            }
        },
        new IntentFilter(AccelerationSetting));
    }

    FaceCommCallBack faceCommCallBack;
    DeviceAccelerationSensor mDeviceAccelerationSensor;

    private void addAccelerationSensor(boolean coreSharedPreferencesKeys) {
        if(coreSharedPreferencesKeys) {
            boolean isOpen = !"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.AccelerationSettingOpen));
            if (!isOpen) return;
        }

        if (faceCommCallBack == null) faceCommCallBack = new FaceCommCallBackForClient();
        if (mDeviceAccelerationSensor == null)
            mDeviceAccelerationSensor = new DeviceAccelerationSensor(faceCommCallBack);
        mDeviceAccelerationSensor.startLinstener();
    }
    private void disAccelerationSensor() {
        if (mDeviceAccelerationSensor != null) mDeviceAccelerationSensor.endLinstener();
    }




    protected long getHeartLong() {
        return UDPTCPkeys.HeartLong_Client;
    }

    protected void onServiceDestryedGetAlarm() {
        noSetAlarm();
        return;
    }

    @Override
    public void onDestroy() {
        disAccelerationSensor();
        MappingDeviceInfoManager.getInstance().clearAll();

        super.onDestroy();
        //        stopForeground(true);
        try {
            SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.Client_Service_UDPreviceback_TCPLongLink, BytesClassAidl.To_Me);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public long getGetTCPTimeOutErrTime() {
        return UDPTCPkeys.TimeOutLong_Client;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        startForeground();
    }

    @SuppressLint("NewApi")
    private void startForeground() {
        Intent in=new Intent(this, Client_InitAc.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_ONE_SHOT);
        String title="正在运行";
        String describe="正在运行";
        Notification notification=null;
        if(!ApiUtil.hasJellyBean()) {
            Notification.Builder builder = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(describe)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);
            notification = builder.getNotification();
        }else{
            notification = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(describe)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .build();

        }
        startForeground(Integer.MAX_VALUE, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    void setAlarm() {
        Intent intent = new Intent(getTCP_heartbeat());
        PendingIntent ac = PendingIntent.getService(this, 1025, intent, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime() + 1000 * 1, getHeartLong(), ac);
    }


    void noSetAlarm() {
        Intent intent = new Intent(getTCP_heartbeat());
        PendingIntent ac = PendingIntent.getService(this, 1025, intent, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(ac);
    }


    @Override
    protected void onLinkErr() {

    }
}
