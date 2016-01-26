package com.hxuehh.rebirth.capacity.vibration;

import android.annotation.TargetApi;
import android.app.Service;
import android.os.Build;
import android.os.RemoteException;
import android.os.Vibrator;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.ApiUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Vibration extends DeviceCapacityBase {

    transient Vibrator vib;

    @Override
    public boolean isShowStatus() {
        return false;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Vibration;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean testHardware_SDK() {
        vib = (Vibrator) SuApplication.getInstance().getSystemService(Service.VIBRATOR_SERVICE);
        if (ApiUtil.hasHoneycomb()) {
            if (vib != null && vib.hasVibrator()) return true;
        }
        if (vib != null) return true;
        return false;
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
        if (!(t instanceof VibrationParameter)) throw new FaceException("参数类型出错");

        if (vib == null) onCreat();
        if (vib == null) throw new FaceException("不支持震动设备");
        if (t == null) throw new FaceException("没有设置震动参数");
        final VibrationParameter mVibrationP = (VibrationParameter) t;
        if(mVibrationP.lastTime<0)throw new FaceException("不能使用负数");
        if(mAccelerationSensorLock !=null){
            mAccelerationSensorLock.lockByInTime(this,mVibrationP.lastTime+ 200);
        }
        vib.vibrate(mVibrationP.lastTime);
        addDeviceStatus(new RunningStatus(RunningStatus.Running, null));
        return new CommonDeviceCapacityOutResult(true);
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Type_Vibration_his_max;
    }


    @Override
    public void activeReportOfEvent(Object[] f) {

    }

    @Override
    public void onCreat() {
        vib = (Vibrator) SuApplication.getInstance().getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void stop() {
        vib.cancel();
    }

    @Override
    public void onDestry() {
        if(vib!=null)
        vib.cancel();
        vib = null;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_vibration_ac, false);
    }


    public static class VibrationParameter extends DeviceCapacityInParameter implements Serializable {
        long lastTime;

        public VibrationParameter(long lastTime) {
            this.lastTime = lastTime;
        }
    }
}
