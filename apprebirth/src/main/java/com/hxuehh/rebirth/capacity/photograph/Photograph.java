package com.hxuehh.rebirth.capacity.photograph;

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
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by suwg on 2015/8/15.
 */
public class Photograph extends DeviceCapacityBase {
    transient Battery mBattery;
    transient USBLinkSenser mUSBLink;
    transient  LightSensor mLightSensor;
    transient  Screen mScreen;

    public Photograph(Battery mBattery, USBLinkSenser mUSBLink, LightSensor mLightSensor, Screen mScreen) {
        this.mBattery = mBattery;
        this.mUSBLink = mUSBLink;
        this.mLightSensor=mLightSensor;
        this.mScreen=mScreen;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Photograph;
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
        if ( !(t instanceof PhotographParameter)) throw new FaceException("参数类型出错");

        final PhotographParameter tt = (PhotographParameter) t;


        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);

        boolean isScreenOnHis=mScreen.isScreenOn();
        FaceBaseActivity_1 ac = SuApplication.getInstance().getCurrentActivity();
        if (ac != null && ac.isOnTop()) {
//            应该这样启动没有问题 别的Ac在前台
        } else {
            if (AppStaticSetting.isUSBMustLink && (mBattery != null && !mBattery.isCharging()) && !mUSBLink.isLink()) {//没有充电，没有启动
                throw new FaceException("屏幕锁死，无法启动功能页面，（插入电源供电，操作不容易失败哟）");
            }
        }

        mAccelerationSensorLockLong();
        mLightSensorLockLong();


        mScreen.screeOpen();//锁屏解开
        if (!mScreen.isScreenOn()) {
            throw new FaceException("屏幕锁死，无法启动功能页面");
        }

        OkInfo = null;
        errInfo = publicErr;
        addRevice();//添加监听
        addThreadToNotify();

        boolean isOpenLight=mLightSensor.isMinValue();
        if(!isOpenLight){
            isOpenLight=tt.isLightOpen;
        }
        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.LighterOpen,BytesClassAidl.To_Me,isOpenLight));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IntentChangeManger.openSingletonAc(getType());

//        直接锁死等待UI提交回来
        synchronized (Photograph.this) {
            try {
                Photograph.this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (OkInfo != null) {
            try {
                byte[] filebytes = FileUtil.getBytes(OkInfo);
                commonDeviceCapacityOutResult.setBytes(filebytes);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "已经存储到设备端；" + DateUtil.getImYMDHMMTime(this.waitKeyLongTime));
                addDeviceStatus(new RunningStatus(RunningStatus.Running, OkInfo));
            } catch (IOException e) {
                e.printStackTrace();
                errInfo = "没有获取到照片具体文件";
                addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, errInfo));
                throw new FaceException(errInfo);
            }finally {
                recoverScreenOn(isScreenOnHis);
            }

        } else {
            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, errInfo));
            recoverScreenOn(isScreenOnHis);
            throw new FaceException(errInfo);
        }

        return commonDeviceCapacityOutResult;
    }

    void recoverScreenOn(boolean isScreenOn){
        if(isScreenOn){

        }else{
            mScreen.stop();
        }

        mLightSensorUnLock100();
        mAccelerationSensorUnLock100();

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
                    Thread.sleep(AppStaticSetting.PhotographGetSleepTime);
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
    transient BroadcastReceiver mBroadcastReceiverForgetPhoto;
    transient long waitKeyLongTime;

    private void addRevice() {
        if (mBroadcastReceiverForgetPhoto == null) {
            mBroadcastReceiverForgetPhoto = new BroadcastReceiver() {
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
                                Object oo = mBytesClassAidl.getTrue();
                                if (oo != null) {
                                    errInfo = (String) oo;
                                    notifyThis();
                                }
                            } catch (RemoteException e) {
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
            SuApplication.getInstance().registerReceiver(mBroadcastReceiverForgetPhoto, mIntentFilter);
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
        if(mBroadcastReceiverForgetPhoto !=null)
        SuApplication.getInstance().unregisterReceiver(mBroadcastReceiverForgetPhoto);
    }

    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_photograph_ac, false);
    }


    public static class PhotographParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int GetPhotoGraoh=1;

        boolean isLightOpen;
        public PhotographParameter(int type,boolean isLightOpen) {
            super(type);
            this.isLightOpen=isLightOpen;
        }
    }


}
