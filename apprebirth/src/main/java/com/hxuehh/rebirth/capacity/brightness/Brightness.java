package com.hxuehh.rebirth.capacity.brightness;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.OnTopActivity;
import com.hxuehh.rebirth.capacity.USBLinkSenser.USBLinkSenser;
import com.hxuehh.rebirth.capacity.battery.Battery;
import com.hxuehh.rebirth.capacity.screen.Screen;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Brightness extends DeviceCapacityBase {

    transient Battery mBattery;
    transient USBLinkSenser mUSBLink;
    transient Screen mScreen;
    public Brightness(Battery mBattery, USBLinkSenser mUSBLink, Screen mScreen) {
        this.mBattery = mBattery;
        this.mScreen=mScreen;
        this.mUSBLink = mUSBLink;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Brightness;
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

    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Brightness_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    transient ContentResolver mContentResolver;

    @Override
    public void onCreat() {
        mContentResolver = SuApplication.getInstance().getContentResolver();
    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {

    }

    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if ( !(t instanceof BrightnessParameter)) throw new FaceException("参数类型出错");
        final BrightnessParameter tt = (BrightnessParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        switch (tt.getType()) {
            case BrightnessParameter.Type_addition: {
                try {
                    if (isAutoBrightness()) {
                        throw new FaceException("自动调节模式");
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                int now = getScreenBrightness();
                if (now == 255) {
                    throw new FaceException("已经是最亮");
                }
                OnTopActivity ac = (OnTopActivity) openScree();
                int set = now + AppStaticSetting.BrightnessSetting;
                if (set > 255) set = 255;
                ac.setBrightness(set);
                saveBrightness(set);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/255");
                backScreen();
            }
            break;
            case BrightnessParameter.Type_addition_max: {
                try {
                    if (isAutoBrightness()) {
                        throw new FaceException("自动调节模式");
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                int now = getScreenBrightness();
                if (now == 255) {
                    throw new FaceException("已经是最亮");
                }
                OnTopActivity ac = (OnTopActivity) openScree();
                int set = 255;
                ac.setBrightness(set);
                saveBrightness(set);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/255");
                backScreen();
            }
            break;
            case BrightnessParameter.Type_subtraction: {
                try {
                    if (isAutoBrightness()) {
                        throw new FaceException("自动调节模式");
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                int now = getScreenBrightness();
                if (now == 1) {
                    throw new FaceException("已经是最暗");
                }
                OnTopActivity ac = (OnTopActivity) openScree();
                int set = now - AppStaticSetting.BrightnessSetting;
                if (set < 1) set = 1;
                ac.setBrightness(set);
                saveBrightness(set);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/255");
                backScreen();
            }
            break;
            case BrightnessParameter.Type_subtraction_mid: {
                try {
                    if (isAutoBrightness()) {
                        throw new FaceException("自动调节模式");
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                int now = getScreenBrightness();
                if (now == 0) {
                    throw new FaceException("已经是最暗");
                }
                OnTopActivity ac = (OnTopActivity) openScree();
                int set = 0;
                ac.setBrightness(set);
                saveBrightness(set);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/255");
                backScreen();
            }
            break;
            case BrightnessParameter.Type_AUTO: {
                startAutoBrightness();
            }
            break;
            case BrightnessParameter.Type_UNAUTO: {
                stopAutoBrightness();
            }
            break;
            default:
                throw new FaceException("未知参数");

        }

        return commonDeviceCapacityOutResult;
    }

    private void backScreen() {
        if(isScreenOnHis){

        }else{
            mScreen.stop();
        }

    }

    boolean isScreenOnHis;

    private FaceBaseActivity_1 openScree() throws FaceException {
         isScreenOnHis = mScreen.isScreenOn();

        FaceBaseActivity_1 ac = SuApplication.getInstance().getCurrentActivity();
        if (ac != null && ac.isOnTop()) {
//            应该这样启动没有问题 别的Ac在前台
        } else {
            if (AppStaticSetting.isUSBMustLink && (mBattery != null && !mBattery.isCharging()) && !mUSBLink.isLink()) {//没有充电，没有启动
                throw new FaceException("屏幕锁死，无法启动功能页面，（插入电源供电，操作不容易失败哟）");
            }
        }
        mScreen.screeOpen();//锁屏解开
        if (!mScreen.isScreenOn()) {
            throw new FaceException("屏幕锁死，无法启动功能页面");
        }

        IntentChangeManger.openSingletonAc(getType());
        int i = 0;
        for (; ; ) {
            i++;
            try {
                Thread.sleep(AppStaticSetting.openScreeTestSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ac = SuApplication.getInstance().getCurrentActivity();
            if (ac != null && ac.isOnTop()) {
                return ac;
            }
            if (i == 10) {
                break;
            }
        }
        throw new FaceException("没有打开功能页面,无法操作");



    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_brightness_ac, false);
    }


    public static class BrightnessParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int Type_addition = 1;
        public static final int Type_subtraction = 2;

        public static final int Type_AUTO = 3;
        public static final int Type_UNAUTO = 4;
        public static final int Type_addition_max = 5;
        public static final int Type_subtraction_mid = 6;

        public BrightnessParameter(int type) {
            super(type);
        }

        @Override
        public String getTypeName() {
            switch (getType()) {
                case Type_addition:
                    return "增亮";
                case Type_subtraction:
                    return "变暗";
                case Type_AUTO:
                    return "自动调节";
                case Type_UNAUTO:
                    return "手动调节";
                case Type_addition_max:
                    return "最亮";
                case Type_subtraction_mid:
                    return "最暗";
            }
            return super.getTypeName();
        }
    }


    //    =================================


    public void saveBrightness(int brightness) {
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(mContentResolver, "screen_brightness", brightness);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        mContentResolver.notifyChange(uri, null);
    }

    public int getScreenBrightness() {
        return android.provider.Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
    }

    public void startAutoBrightness() {
        Settings.System.putInt(mContentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    public void stopAutoBrightness() {
        Settings.System.putInt(mContentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public boolean isAutoBrightness() throws Settings.SettingNotFoundException {
        return Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
    }


}
