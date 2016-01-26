package com.hxuehh.rebirth.capacity.screen;

import android.app.Service;
import android.content.Intent;
import android.os.PowerManager;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.screen.LockFaceAc.LockScreenAC;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Screen extends DeviceCapacityBase {

    transient LockScreen mLockScreen;

    public Screen(LockScreen mLockScreen) {
        this.mLockScreen = mLockScreen;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Screen;
    }


    @Override
    public boolean testHardware_SDK() {
        return true;
    }


    @Override
    public boolean isDevEnable() {
        return true;
    }

    @Override
    public String getDevUnEnableInfo() {
        return null;
    }


    transient LockScreen.LockScreenParameter mLockScreenParameterOFF = new LockScreen.LockScreenParameter(LockScreen.LockScreenParameter.TypeLockOff);
    transient LockScreen.LockScreenParameter mLockScreenParameterON = new LockScreen.LockScreenParameter(LockScreen.LockScreenParameter.TypeLockON);

    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {

        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3 = super.doChangeStatus(t);
        if (mDeviceCapacityOutResult_3 != null) return mDeviceCapacityOutResult_3;
        if (!(t instanceof ScreenParameter)) throw new FaceException("参数类型出错");


        final ScreenParameter tt = (ScreenParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);

        switch (tt.getType()) {
            case ScreenParameter.TypeLockON: {
                mLockScreen.doChangeStatus(mLockScreenParameterON);
            }
            break;
            case ScreenParameter.TypeLockOff: {
                mLockScreen.doChangeStatus(mLockScreenParameterOFF);
            }
            break;
            case ScreenParameter.TypeScreenON: {

                screeOpen();
                addDeviceStatus(new RunningStatus(RunningStatus.Running, tt.toString()));
//                if (tt.timeOut <= 0) {
//                    tt.timeOut = AppStaticSetting.ScreenWakelockTime;
//                }
//                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, tt.timeOut / 1000 + "秒后关闭");
//                ThreadManager.getInstance().getNewThread("close Screen", new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(tt.timeOut);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (tt.isTheSameTag(getStaticLastTagTime())) {
//                            stop();
//                        }
//                    }
//                }).start();
            }
            break;
            case ScreenParameter.TypeScreenOff: {
//                if(tt.isCmdLevelLow()){
//                    ScreenParameter mScreenParameter= (ScreenParameter) this.get_this_LastDeviceCapacityInParameter();
//                    if(mScreenParameter!=null&&mScreenParameter.getType()==ScreenParameter.TypeScreenON&&!mScreenParameter.isCmdLevelLow()){
//                        setmLastDeviceCapacityInParameter(mScreenParameter);
//                        throw new FaceException("正在执行用户指令，传感器指令失效", FaceException.sensorErrType_SameCMD);
//                    }
//                }
                mLockScreen.doChangeStatus(mLockScreenParameterOFF);
//                if (!isScreenOn()) throw new FaceException("已经关闭");
                stop();
            }
            break;
        }
        return commonDeviceCapacityOutResult;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Screen_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    transient PowerManager.WakeLock mWakelock;
    transient PowerManager pm;


    @Override
    public void onCreat() {
        pm = (PowerManager) SuApplication.getInstance().getSystemService(Service.POWER_SERVICE);

    }

    @Override
    public void stop() {

        Su.log("Screen stop");
        try {
            if (mWakelock != null) {
                if (mWakelock.isHeld()) {
                    mWakelock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        keyPower();
        addDeviceStatus(new RunningStatus(RunningStatus.Running, "关闭"));

    }

    private void keyPower() {
//        try
//        {
//            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_POWER;
//            Runtime runtime = Runtime.getRuntime();
//            Process proc = runtime.exec(keyCommand);
//        }
//        catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        Intent mIntent = new Intent(SuApplication.getInstance(), LockScreenAC.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SuApplication.getInstance().startActivity(mIntent);

    }

    @Override
    public void onDestry() {
        stop();
    }

    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_screen_ac, false);
    }


    public  boolean isScreenOn() {

        return pm.isScreenOn();
    }

    public void screeOpen() throws FaceException {
        mLockScreen.doChangeStatus(mLockScreenParameterON);
        if(mWakelock==null) mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        mWakelock.acquire();
        if(!isScreenOn()){
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            mWakelock.acquire();
        }
    }


    public static class ScreenParameter extends DeviceCapacityInParameter implements Serializable {


        public static final int TypeLockON = 1, TypeLockOff = 2;
        public static final int TypeScreenON = 3, TypeScreenOff = 4;

        public ScreenParameter(int type, long timeOut) {
            super(type);
            this.timeOut = timeOut;
        }

        public ScreenParameter(int type) {
            super(type);
        }

        long timeOut;
    }


}
