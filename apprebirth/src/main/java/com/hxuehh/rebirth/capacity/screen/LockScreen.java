package com.hxuehh.rebirth.capacity.screen;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class LockScreen extends DeviceCapacityBase {

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_LockScreen;
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
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (!(t instanceof LockScreenParameter)) throw new FaceException("参数类型出错");
        final LockScreenParameter tt = (LockScreenParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.getType() == LockScreenParameter.TypeLockOff) {
            mKeyguardLock.reenableKeyguard();
        } else if (tt.getType() == LockScreenParameter.TypeLockON) {
            mKeyguardLock.disableKeyguard();
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

    transient KeyguardManager km;
    transient KeyguardManager.KeyguardLock mKeyguardLock;

    @Override
    public void onCreat() {
        km = (KeyguardManager) SuApplication.getInstance().getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = km.newKeyguardLock("unLockNull");

    }

    @Override
    public void stop() {
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
    public boolean isShowInClient() {
        return false;
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
//        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
//        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_screen_ac, false);
    }

    public static class LockScreenParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int TypeLockON = 1, TypeLockOff = 2;

        public LockScreenParameter(int type) {
            super(type);
        }
    }


}
