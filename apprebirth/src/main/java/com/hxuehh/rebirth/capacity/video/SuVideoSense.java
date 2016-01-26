package com.hxuehh.rebirth.capacity.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.OnTopActivity;
import com.hxuehh.rebirth.capacity.USBLinkSenser.USBLinkSenser;
import com.hxuehh.rebirth.capacity.battery.Battery;
import com.hxuehh.rebirth.capacity.lightSensor.LightSensor;
import com.hxuehh.rebirth.capacity.screen.Screen;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by suwg on 2015/8/15.
 */
public class SuVideoSense extends DeviceCapacityBase {

    public static final String End="com.hxuehh.rebirth.video.end";
    transient Battery mBattery;
    transient USBLinkSenser mUSBLink;
    transient LightSensor mLightSensor;
    transient Screen mScreen;

    public SuVideoSense(Battery mBattery, USBLinkSenser mUSBLink, LightSensor mLightSensor, Screen mScreen) {
        this.mBattery = mBattery;
        this.mUSBLink = mUSBLink;
        this.mLightSensor=mLightSensor;
        this.mScreen=mScreen;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Video_Surveillance_Sense;
    }

    @Override
    public boolean testHardware_SDK() {
        return SuApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    public boolean isDevEnable() {
        return true;
    }

    @Override
    public String getDevUnEnableInfo() {
        return null;
    }

    @Override
    @Face_UnsolvedForDlp //没有办法判断USB是不是供电中
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (!(t instanceof VideoParameter)) throw new FaceException("参数类型出错");

        final VideoParameter tt = (VideoParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);

        switch (tt.getType()){
            case VideoParameter.Type_show_live:{
                showLive(commonDeviceCapacityOutResult,tt);
            }
            break;
            case VideoParameter.Type_End:{
                SuApplication.getInstance().sendBroadcast(new Intent(End));
                mScreen.doChangeStatus(screenParameterOFF);

            }
            break;
        }

        return commonDeviceCapacityOutResult;
    }

    transient Screen.ScreenParameter screenParameterON = new Screen.ScreenParameter(Screen.ScreenParameter.TypeScreenON);
    transient Screen.ScreenParameter screenParameterOFF = new Screen.ScreenParameter(Screen.ScreenParameter.TypeScreenOff);

    private void showLive(CommonDeviceCapacityOutResult commonDeviceCapacityOutResult, VideoParameter tt) throws FaceException {
        boolean isScreenOnHis = mScreen.isScreenOn();
        FaceBaseActivity_1 ac = SuApplication.getInstance().getCurrentActivity();
        if (ac != null && ac.isOnTop()) {
//            应该这样启动没有问题 别的Ac在前台
        } else {
            if (AppStaticSetting.isUSBMustLink && (mBattery != null && !mBattery.isCharging()) && !mUSBLink.isLink()) {//没有充电，没有启动
                throw new FaceException("屏幕锁死，无法启动功能页面，（插入电源供电，操作不容易失败哟）");
            }
        }

        mScreen.doChangeStatus(screenParameterON);
        if (!mScreen.isScreenOn()) {
            throw new FaceException("屏幕锁死，无法启动功能页面");
        }

        OkInfo = null;
        errInfo = publicErr;
        addErrRevice();//添加监听
        addThreadToNotify();

        boolean isLightOpen=mLightSensor.isMinValue();
        if(!isLightOpen){
            isLightOpen=tt.isLightOpen;
        }

        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.openSingletonAcForDeviceCap_VideoIP, BytesClassAidl.To_Me, tt.getTargetIP()));
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.openSingletonAcForDeviceCap_VideoPort, BytesClassAidl.To_Me, tt.getUdpPortForGet()));
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.LighterOpen, BytesClassAidl.To_Me, isLightOpen));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        IntentChangeManger.openSingletonAc(getType());
//        直接锁死等待UI提交回来
        synchronized (SuVideoSense.this) {
            try {
                SuVideoSense.this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (OkInfo != null) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, OkInfo);
            addDeviceStatus(new RunningStatus(RunningStatus.Running, OkInfo));
        } else {
            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, errInfo));
            recoverScreenOn(isScreenOnHis);
            throw new FaceException(errInfo);
        }

    }

    void recoverScreenOn(boolean isScreenOn) {
        if (isScreenOn) {

        } else {
//            mScreen.stop();

        }
    }


    private void addThreadToNotify() {
        waitKeyLongTime = new Date().getTime();
//        唤起线程
        ThreadManager.getInstance().getNewThread("SuVideoSense notifi", new Runnable() {
            long waitKeythis;

            @Override
            public void run() {
                this.waitKeythis = waitKeyLongTime;
                try {
                    Thread.sleep(AppStaticSetting.VideoraphGetSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (waitKeythis == waitKeyLongTime) {
                    notifyThis();
                }
            }
        }).start();
    }


    public static transient String publicErr = "您的手机可能已经被锁屏，没有办法操作摄像头,(一般连接充电器可直接唤醒)";
    transient String errInfo;
    transient String OkInfo;
    transient BroadcastReceiver mBroadcastReceiverForRes;
    transient long waitKeyLongTime;

    private void addErrRevice() {
        if (mBroadcastReceiverForRes == null) {
            mBroadcastReceiverForRes = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String ac = intent.getAction();
                    if (ac == null) {

                    } else {
                        if ((OnTopActivity.ActionOK + getType()).equals(ac)) {
                            BytesClassAidl mBytesClassAidl = null;
                            try {
                                mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.OnTopActivityRes, BytesClassAidl.To_Me);
                                Object oo = mBytesClassAidl.getTrue();
                                if (oo != null) {
                                    OkInfo = (String) oo;
                                    notifyThis();
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                                errInfo = "没有获取到结果";
                                notifyThis();
                            }

                        } else if ((OnTopActivity.ActionErr + getType()).equals(ac)) {
                            BytesClassAidl mBytesClassAidl = null;
                            try {
                                mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.OnTopActivityRes, BytesClassAidl.To_Me);
                                if(mBytesClassAidl!=null) {
                                    Object oo = mBytesClassAidl.getTrue();
                                    if (oo != null) {
                                        errInfo = (String) oo;
                                        notifyThis();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                errInfo = "没有获取到结果";
                                notifyThis();
                            }
                        }
                    }
                }
            };
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(OnTopActivity.ActionOK + getType());
            mIntentFilter.addAction(OnTopActivity.ActionErr + getType());
            SuApplication.getInstance().registerReceiver(mBroadcastReceiverForRes, mIntentFilter);
        }
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Screen_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }


    @Override
    public void onCreat() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {
        if(mBroadcastReceiverForRes!=null)
        SuApplication.getInstance().unregisterReceiver(mBroadcastReceiverForRes);
        mBroadcastReceiverForRes=null;
    }

    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_video_ac, false);
    }


    public static class VideoParameter extends DeviceCapacityInParameter implements Serializable {
        public VideoParameter(int type) {
            super(type);
        }
        public static final int Type_show_live = 1;//直播
        public static final int Type_End = 2;//结束
        String targetIP;
        int udpPortForGet;//目标端口
        boolean isLightOpen;

        public boolean isLightOpen() {
            return isLightOpen;
        }

        public void setIsLightOpen(boolean isLightOpen) {
            this.isLightOpen = isLightOpen;
        }

        public String getTargetIP() {
            return targetIP;
        }

        public void setTargetIP(String targetIP) {
            this.targetIP = targetIP;
        }

        public int getUdpPortForGet() {
            return udpPortForGet;
        }

        public void setUdpPortForGet(int udpPortForGet) {
            this.udpPortForGet = udpPortForGet;
        }
    }
}